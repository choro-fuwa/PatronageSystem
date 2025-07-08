package com.PatronageSystem.service;

import com.PatronageSystem.domain.IpInfo;
import com.PatronageSystem.repository.IpInfoRepository;
import com.PatronageSystem.service.IpInfoService;
import com.PatronageSystem.utils.DateUtil;
import com.PatronageSystem.utils.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IpInfoservice {
    @Autowired
    private IpInfoService ipInfoService;
    @Autowired
    private IpInfoRepository ipInfoRepository;
    @Autowired
    private RedisUtils redisUtils;

    @Test
    public void queryAll() {
//        List<IpInfo> ipInfos = ipInfoService.queryAll();

        Pageable request = PageRequest.of(0, 1);
        List<IpInfo> ipInfos = ipInfoRepository.findAllByOrderByIdDesc(request);
        ipInfos.forEach(System.out::println);
    }

    @Test
    public void findByTime() {
        System.out.println("今天日期：" + DateUtil.initDateByDay());
        System.out.println("昨天日期：" + DateUtil.getYesday());
        List<IpInfo> ipInfos = ipInfoRepository.findAllByTimeIsBetween(DateUtil.getYesday(), new Date());
        ipInfos.forEach(System.out::println);
    }

    @Test
    public void save() {
        IpInfo ipInfo = new IpInfo("192.168.3.181", "深圳", "chome", new Date(), "181,23", 10L, 1);
        ipInfoRepository.save(ipInfo);
    }

    @Test
    public void saveRedis() {
        ipInfoService.saveRedis("183.17.232.207", "Chome");
    }

    @Test
    public void queryAllRedis() {
        List<Object> ipInfos = ipInfoService.hgetAllRedis("ipInfo");
        List<IpInfo> strs = ipInfos.stream().map(obj -> (IpInfo) obj).collect(Collectors.toList());
        strs.forEach(System.out::println);
    }

    @Test
    public void delRedis() {
        redisUtils.del("ipInfo");
    }


}