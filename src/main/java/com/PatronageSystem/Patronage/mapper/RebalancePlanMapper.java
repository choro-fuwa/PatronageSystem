package com.PatronageSystem.Patronage.mapper;

import com.PatronageSystem.Patronage.entity.RebalancePlan;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface RebalancePlanMapper {
    int insert(RebalancePlan plan);
    int update(RebalancePlan plan);
    int deleteById(Long id);
    RebalancePlan selectById(Long id);
    List<RebalancePlan> selectAll();
} 