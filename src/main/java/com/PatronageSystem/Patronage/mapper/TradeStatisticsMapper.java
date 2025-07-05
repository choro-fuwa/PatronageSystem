package com.PatronageSystem.Patronage.mapper;

import com.PatronageSystem.Patronage.entity.TradeStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface TradeStatisticsMapper {
    List<TradeStatistics> selectAll();

    TradeStatistics selectById(@Param("id") Long id);

    List<TradeStatistics> selectByAccountId(@Param("accountId") Long accountId);

    TradeStatistics selectByAccountAndDate(@Param("accountId") Long accountId, @Param("statDate") LocalDate statDate);

    List<TradeStatistics> selectByDateRange(@Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);

    int insert(TradeStatistics tradeStatistics);

    int update(TradeStatistics tradeStatistics);

    int deleteById(@Param("id") Long id);
}
