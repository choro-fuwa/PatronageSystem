package src.java.com.patronage.strategy.service.impl;

import com.alibaba.fastjson2.JSON;
import src.java.com.patronage.strategy.entity.BacktestResult;
import src.java.com.patronage.strategy.entity.Strategy;
import src.java.com.patronage.strategy.mapper.BacktestResultMapper;
import src.java.com.patronage.strategy.service.BacktestEngine;
import src.java.com.patronage.strategy.service.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 回测引擎实现类
 */
@Slf4j
@Service
public class BacktestEngineImpl implements BacktestEngine {

    @Autowired
    private BacktestResultMapper backtestResultMapper;

    @Autowired
    private StrategyService strategyService;

    @Override
    public BacktestResult runBacktest(Long strategyId, LocalDateTime startDate, LocalDateTime endDate, Map<String, Object> parameters) {
        log.info("开始执行回测，策略ID: {}, 开始日期: {}, 结束日期: {}", strategyId, startDate, endDate);
        
        // 获取策略信息
        Strategy strategy = strategyService.getById(strategyId);
        if (strategy == null) {
            throw new RuntimeException("策略不存在");
        }

        // 创建回测结果对象
        BacktestResult result = new BacktestResult();
        result.setStrategyId(strategyId);
        result.setStartDate(startDate);
        result.setEndDate(endDate);
        result.setInitialCapital(strategy.getInitialCapital());
        result.setParameters(JSON.toJSONString(parameters));
        result.setCreateTime(LocalDateTime.now());
        result.setCreateBy("system");

        try {
            // 调用Python回测脚本
            Map<String, Object> backtestData = executePythonBacktest(strategy, startDate, endDate, parameters);
            
            // 设置回测结果
            result.setFinalCapital((BigDecimal) backtestData.get("finalCapital"));
            result.setTotalReturn((BigDecimal) backtestData.get("totalReturn"));
            result.setAnnualReturn((BigDecimal) backtestData.get("annualReturn"));
            result.setMaxDrawdown((BigDecimal) backtestData.get("maxDrawdown"));
            result.setSharpeRatio((BigDecimal) backtestData.get("sharpeRatio"));
            result.setWinRate((BigDecimal) backtestData.get("winRate"));
            result.setTradeCount((Integer) backtestData.get("tradeCount"));
            result.setResultData(JSON.toJSONString(backtestData.get("dailyReturns")));

            // 保存回测结果
            backtestResultMapper.insert(result);
            
            log.info("回测执行完成，回测ID: {}", result.getId());
            return result;
            
        } catch (Exception e) {
            log.error("回测执行失败", e);
            throw new RuntimeException("回测执行失败: " + e.getMessage());
        }
    }

    @Override
    public BacktestResult getBacktestResult(Long backtestId) {
        return backtestResultMapper.selectById(backtestId);
    }

    @Override
    public List<BacktestResult> getBacktestHistory(Long strategyId) {
        return backtestResultMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<BacktestResult>()
                .eq(BacktestResult::getStrategyId, strategyId)
                .orderByDesc(BacktestResult::getCreateTime)
        );
    }

    @Override
    public boolean deleteBacktestResult(Long backtestId) {
        return backtestResultMapper.deleteById(backtestId) > 0;
    }

    @Override
    public Map<String, Object> getBacktestProgress(Long backtestId) {
        // 这里可以实现回测进度查询逻辑
        Map<String, Object> progress = new HashMap<>();
        progress.put("status", "completed");
        progress.put("progress", 100);
        return progress;
    }

    /**
     * 执行Python回测脚本
     */
    private Map<String, Object> executePythonBacktest(Strategy strategy, LocalDateTime startDate, LocalDateTime endDate, Map<String, Object> parameters) {
        // 这里应该调用Python回测脚本
        // 暂时返回模拟数据
        Map<String, Object> result = new HashMap<>();
        result.put("finalCapital", new BigDecimal("110000"));
        result.put("totalReturn", new BigDecimal("0.10"));
        result.put("annualReturn", new BigDecimal("0.12"));
        result.put("maxDrawdown", new BigDecimal("0.05"));
        result.put("sharpeRatio", new BigDecimal("1.2"));
        result.put("winRate", new BigDecimal("0.65"));
        result.put("tradeCount", 50);
        
        // 模拟每日收益数据
        Map<String, BigDecimal> dailyReturns = new HashMap<>();
        dailyReturns.put("2024-01-01", new BigDecimal("0.01"));
        dailyReturns.put("2024-01-02", new BigDecimal("-0.005"));
        result.put("dailyReturns", dailyReturns);
        
        return result;
    }
} 