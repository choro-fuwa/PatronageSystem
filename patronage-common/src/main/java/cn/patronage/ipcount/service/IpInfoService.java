package cn.patronage.ipcount.service;

import cn.patronage.ipcount.domain.IpInfo;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface IpInfoService {
    List<IpInfo> queryAll();

    List<IpInfo> queryYesterday(Date start, Date end);

    List<Object> hgetAllRedis(String key);

    List<IpInfo> findAllByOrderByIdDesc(Pageable pageable);

    IpInfo save(IpInfo ipInfo);

    void saveRedis(String ip, String ua);

    void saveRedisStatusDown(String ip);
}
