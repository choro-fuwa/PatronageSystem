package com.PatronageSystem.filter;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import java.util.Enumeration;
import java.util.Map;


public class WebSocketConfigurator extends ServerEndpointConfig.Configurator {

    public static final String IP_ADDR = "IP.ADDR";
    public static final String IP_UA = "IP.UA";

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {

        Map<String, Object> attributes = sec.getUserProperties();
        HttpSession session = (HttpSession) request.getHttpSession();
        if (session != null) {
            attributes.put(IP_ADDR, session.getAttribute("ip"));
            attributes.put(IP_UA, session.getAttribute("ua"));
            Enumeration<String> names = session.getAttributeNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                attributes.put(name, session.getAttribute(name));
            }
        }
    }
}