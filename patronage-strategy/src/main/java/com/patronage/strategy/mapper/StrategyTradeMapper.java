package com.patronage.strategy.mapper;

import com.patronage.strategy.entity.StrategyTrade;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 策略交易记录Mapper接口
 */
@Mapper
public interface StrategyTradeMapper {

    /**
     * 根据策略ID查询交易记录
     */
    List<StrategyTrade> selectByStrategyId(@Param("strategyId") Long strategyId);

    /**
     * 根据策略ID和日期范围查询交易记录
     */
    List<StrategyTrade> selectByStrategyIdAndDateRange(@Param("strategyId") Long strategyId,
                                                       @Param("startDate") LocalDate startDate,
                                                       @Param("endDate") LocalDate endDate);

    /**
     * 根据交易类型查询
     */
    List<StrategyTrade> selectByTradeType(@Param("strategyId") Long strategyId,
                                          @Param("tradeType") String tradeType);

    /**
     * 根据基金代码查询交易记录
     */
    List<StrategyTrade> selectByFundCode(@Param("fundCode") String fundCode);

    /**
     * 新增交易记录
     */
    int insert(StrategyTrade trade);

    /**
     * 批量新增交易记录
     */
    int batchInsert(@Param("trades") List<StrategyTrade> trades);

    /**
     * 更新交易记录
     */
    int update(StrategyTrade trade);

    /**
     * 删除交易记录
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据策略ID删除交易记录
     */
    int deleteByStrategyId(@Param("strategyId") Long strategyId);

    /**
     * 获取交易统计信息
     */
    Map<String, Object> selectTradeStats(@Param("strategyId") Long strategyId);

    /**
     * 按策略统计交易记录
     */
    List<Map<String, Object>> selectTradeStatsByStrategy();
}