package com.PatronageSystem.service.impl;

import com.PatronageSystem.domain.IpInfo;
import com.PatronageSystem.domain.vo.IpDataVo;
import com.PatronageSystem.repository.IpInfoRepository;
import com.PatronageSystem.service.IpInfoService;
import com.PatronageSystem.utils.OkHttpClientUtil;
import com.PatronageSystem.utils.RedisUtils;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class IpInfoServiceImpl implements IpInfoService {

    @Autowired
    private IpInfoRepository ipInfoRepository;

    @Autowired
    private RedisUtils redisUtils;

    private static final String GET_IPDATA_URL = "https://restapi.amap.com/v5/ip?key=ccd6ab556d3504d42ab31d84b8c95967&type=4&ip=";
    private static final int ONLINE_STATUS_UP = 1;
    private static final int ONLINE_STATUS_DOWN = 0;

    @Override
    public List<IpInfo> queryAll() {
        return ipInfoRepository.findAll();
    }

    @Override
    public List<IpInfo> queryYesterday(Date start, Date end) {
        return ipInfoRepository.findAllByTimeIsBetween(start, end);
    }

    @Override
    public List<Object> hgetAllRedis(String key) {
        return redisUtils.hgetAll(key);
    }

    @Override
    public List<IpInfo> findAllByOrderByIdDesc(Pageable pageable) {
        return ipInfoRepository.findAllByOrderByIdDesc(pageable);
    }

    @Override
    public IpInfo save(IpInfo ipInfo) {
        IpInfo exist = ipInfoRepository.findByIp(ipInfo.getIp());
        if (exist != null) {
            // 已存在，count+1
            exist.setCount(exist.getCount() + 1);
            return ipInfoRepository.save(exist);
        } else {
            // 新IP，count=1
            ipInfo.setCount(1L);
            return ipInfoRepository.save(ipInfo);
        }
    }

    @SneakyThrows
    @Override
    @Async
    public void saveRedis(String ip, String ua) {
        // 查询redis里面是否存该IP数据
        IpInfo ipInfo = (IpInfo) redisUtils.hget("ipInfo", ip);
        long count = 1L;
        if (ipInfo != null) {
            // 如果存在，访问量加一
            count = ipInfo.getCount() + 1;
            ipInfo.setStatus(ONLINE_STATUS_UP);
            ipInfo.setUa(ua);
            ipInfo.setCount(count);
            ipInfo.setTime(new Date());
        } else {
            // 如果不存在，新建数据
            String url = GET_IPDATA_URL + ip;
            // 获取相关信息
            Response response = OkHttpClientUtil.getData(url);
            String json = new String(response.body().bytes());
            Gson gson = new Gson();
            IpDataVo ipData = gson.fromJson(json, IpDataVo.class);
            String address = ipData.getCountry() + " " + ipData.getProvince() + " " + ipData.getCity() + " " + ipData.getDistrict();
            ipInfo = new IpInfo(ip, address, ua, new Date(), ipData.getLocation(), count, ONLINE_STATUS_UP);
        }

        //  redis新增/更新数据
        boolean ipFlag = redisUtils.hset("ipInfo", ip, ipInfo);
        log.info("【Redis消息】, redis上线状态:{}", ipFlag ? "更新成功" : "更新失败");

        // 发送websocket消息

    }

    @SneakyThrows
    @Override
    @Async
    public void saveRedisStatusDown(String ip) {
        IpInfo ipInfo = (IpInfo) redisUtils.hget("ipInfo", ip);
        ipInfo.setStatus(ONLINE_STATUS_DOWN);
        boolean ipFlag = redisUtils.hset("ipInfo", ip, ipInfo);
        log.info("【Redis消息】, redis下线状态:{}", ipFlag ? "更新成功" : "更新失败");

        // 发送websocket消息

    }
}
