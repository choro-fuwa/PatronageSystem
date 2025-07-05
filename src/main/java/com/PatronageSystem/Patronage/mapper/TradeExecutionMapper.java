package com.PatronageSystem.Patronage.mapper;

import com.PatronageSystem.Patronage.entity.TradeExecution;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TradeExecutionMapper {
    List<TradeExecution> selectAll();

    TradeExecution selectById(@Param("id") Long id);

    TradeExecution selectByExecutionCode(@Param("executionCode") String executionCode);

    List<TradeExecution> selectByOrderId(@Param("orderId") Long orderId);

    List<TradeExecution> selectByAccountId(@Param("accountId") Long accountId);

    List<TradeExecution> selectByTimeRange(@Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    int insert(TradeExecution tradeExecution);

    int update(TradeExecution tradeExecution);

    int deleteById(@Param("id") Long id);
}
