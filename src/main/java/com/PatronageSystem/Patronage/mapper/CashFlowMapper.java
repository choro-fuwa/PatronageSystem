package com.PatronageSystem.Patronage.mapper;

import com.PatronageSystem.Patronage.entity.CashFlow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CashFlowMapper {
    List<CashFlow> selectAll();

    CashFlow selectById(@Param("id") Long id);

    CashFlow selectByFlowCode(@Param("flowCode") String flowCode);

    List<CashFlow> selectByAccountId(@Param("accountId") Long accountId);

    List<CashFlow> selectByFlowType(@Param("flowType") String flowType);

    List<CashFlow> selectByTimeRange(@Param("startTime") LocalDateTime startTime,
                                     @Param("endTime") LocalDateTime endTime);

    int insert(CashFlow cashFlow);

    int update(CashFlow cashFlow);

    int deleteById(@Param("id") Long id);
} 