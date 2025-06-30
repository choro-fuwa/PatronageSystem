package src.java.com.patronage.strategy.service;

import src.java.com.patronage.strategy.entity.BacktestResult;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 回测引擎接口
 */
public interface BacktestEngine {

    /**
     * 执行回测
     */
    BacktestResult runBacktest(Long strategyId, LocalDateTime startDate, LocalDateTime endDate, Map<String, Object> parameters);

    /**
     * 获取回测结果
     */
    BacktestResult getBacktestResult(Long backtestId);

    /**
     * 获取策略的回测历史
     */
    java.util.List<BacktestResult> getBacktestHistory(Long strategyId);

    /**
     * 删除回测结果
     */
    boolean deleteBacktestResult(Long backtestId);

    /**
     * 获取回测进度
     */
    Map<String, Object> getBacktestProgress(Long backtestId);
} 