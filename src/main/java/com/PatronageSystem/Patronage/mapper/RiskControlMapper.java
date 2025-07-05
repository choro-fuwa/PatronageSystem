package com.PatronageSystem.Patronage.mapper;

import com.PatronageSystem.Patronage.entity.RiskControl;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RiskControlMapper {
    List<RiskControl> selectAll();

    RiskControl selectById(@Param("id") Long id);

    List<RiskControl> selectByAccountId(@Param("accountId") Long accountId);

    RiskControl selectByAccountAndType(@Param("accountId") Long accountId, @Param("riskType") String riskType);

    List<RiskControl> selectByRiskType(@Param("riskType") String riskType);

    List<RiskControl> selectActive();

    int insert(RiskControl riskControl);

    int update(RiskControl riskControl);

    int deleteById(@Param("id") Long id);

    int updateCurrentValue(@Param("id") Long id, @Param("currentValue") java.math.BigDecimal currentValue);
}
