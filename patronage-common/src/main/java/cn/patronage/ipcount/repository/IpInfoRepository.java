package cn.patronage.ipcount.repository;

import cn.patronage.ipcount.domain.IpInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface IpInfoRepository extends JpaRepository<IpInfo, Long>, JpaSpecificationExecutor<IpInfo> {
    List<IpInfo> findAllByOrderByIdDesc(Pageable pageable);

    List<IpInfo> findAllByTimeIsBetween(Date start, Date end);

    IpInfo findByIp(String ip);

}
