package com.PatronageSystem.config;

import com.PatronageSystem.domain.IpInfo;
import com.PatronageSystem.service.IpInfoService;
import com.PatronageSystem.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class Jobs {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private IpInfoService ipInfoService;

    private final static long ONE_Minute = 60 * 1000;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Scheduled(fixedDelay = ONE_Minute)
    public void fixedDelayJob() {
        //System.out.println(sdf.format(new Date()) + " >>fixedDelay执行....");
    }

    @Scheduled(fixedRate = ONE_Minute)
    public void fixedRateJob() {
        //System.out.println(sdf.format(new Date()) + " >>fixedRate执行....");
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void cronJob() throws ParseException {
        //定时任务数据持久化存储

        // 1. 获取redis中的数据，并且存入数据库
        List<Object> ipInfos = redisUtils.hgetAll("ipInfo");
        List<IpInfo> result = ipInfos.stream().map(obj -> (IpInfo) obj).collect(Collectors.toList());
        for (IpInfo ipInfo : result) {
            ipInfo.setStatus(0);
            ipInfoService.save(ipInfo);
        }
        // 2. 清除redis中的数据
        redisUtils.del("ipInfo");

        // 3. 打个LOG
        log.info("【Redis每天持久化数据完成】, 数据总数:{}", result.size());

    }


}

