package com.PatronageSystem.webSocket;

import com.PatronageSystem.filter.WebSocketConfigurator;
import com.PatronageSystem.service.IpInfoService;
import com.PatronageSystem.utils.IPUtil;
import com.PatronageSystem.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 描述:
 * WebSocket 服务端
 *
 * @see ServerEndpoint WebSocket服务端 需指定端点的访问路径
 * @see Session   WebSocket会话对象 通过它给客户端发送消息
 */

@Component
@ServerEndpoint(value = "/websocket", configurator = WebSocketConfigurator.class)
@Slf4j
public class WebSocketServer {
    //  这里使用静态，让 service 属于类
    @Autowired
    private static IpInfoService ipInfoService;

    // 注入的时候，给类的 service 注入
    @Autowired
    public void setChatService(IpInfoService ipInfoService) {
        WebSocketServer.ipInfoService = ipInfoService;
    }

    private Session session;

    // private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();
    private static ConcurrentHashMap<String, Set<WebSocketServer>> serverMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        Map<String, Object> userProperties = session.getUserProperties();
        String ipAddr = (String) userProperties.get(WebSocketConfigurator.IP_ADDR);
        String ua = (String) userProperties.get(WebSocketConfigurator.IP_UA);
        // 每次创建连接需要更新redis
        ipInfoService.saveRedis(ipAddr, ua);
        this.session = session;
        // webSocketSet.add(this);

        Set<WebSocketServer> webSocketServers = serverMap.containsKey(ipAddr) ? serverMap.get(ipAddr) : new HashSet<>();
        webSocketServers.add(this);
        serverMap.put(ipAddr, webSocketServers);
        webSocketServers.forEach(System.out::println);
        log.info("【websocket消息】有新的连接, 总数:{}", serverMap.size());
        sendMessage(serverMap.size() + "");
    }

    @OnClose
    public void onClose(Session session) {
        Map<String, Object> userProperties = session.getUserProperties();
        String ipAddr = (String) userProperties.get(WebSocketConfigurator.IP_ADDR);
        // webSocketSet.remove(this);

        Set<WebSocketServer> webSocketServers = serverMap.get(ipAddr);
        webSocketServers.remove(this);
        serverMap.put(ipAddr, webSocketServers);
        // 更新redis数据为下线状态
        if (webSocketServers.size() == 0) {
            ipInfoService.saveRedisStatusDown(ipAddr);
            serverMap.remove(ipAddr);
        }

        sendMessage(serverMap.size() + "");
        log.info("【websocket消息】连接断开, 总数:{}", serverMap.size());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("【websocket消息】收到客户端发来的消息:{}", message);
    }

    /**
     * 当通信发生异常：打印错误日志
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        for (String s : serverMap.keySet()) {
            for (WebSocketServer webSocketServer : serverMap.get(s)) {
                log.info("【websocket消息】广播消息, message={}", message);
                try {
                    webSocketServer.session.getBasicRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        /*for (WebSocketServer webSocket : webSocketSet) {
            log.info("【websocket消息】广播消息, message={}", message);
            try {
                webSocket.session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }
}
