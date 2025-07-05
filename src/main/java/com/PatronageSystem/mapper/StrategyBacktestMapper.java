package com.PatronageSystem.mapper;

import com.PatronageSystem.entity.StrategyBacktest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 策略回测Mapper接口
 */
@Mapper
public interface StrategyBacktestMapper {

    /**
     * 根据策略ID查询回测结果
     */
    List<StrategyBacktest> selectByStrategyId(@Param("strategyId") Long strategyId);

    /**
     * 根据策略ID和日期范围查询
     */
    List<StrategyBacktest> selectByStrategyIdAndDateRange(@Param("strategyId") Long strategyId,
                                                          @Param("startDate") LocalDate startDate,
                                                          @Param("endDate") LocalDate endDate);

    /**
     * 查询最新的回测结果
     */
    StrategyBacktest selectLatestByStrategyId(@Param("strategyId") Long strategyId);

    /**
     * 根据回测日期查询
     */
    List<StrategyBacktest> selectByBacktestDate(@Param("backtestDate") LocalDate backtestDate);

    /**
     * 新增回测结果
     */
    int insert(StrategyBacktest backtest);

    /**
     * 更新回测结果
     */
    int update(StrategyBacktest backtest);

    /**
     * 删除回测结果
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据策略ID删除回测结果
     */
    int deleteByStrategyId(@Param("strategyId") Long strategyId);
}
