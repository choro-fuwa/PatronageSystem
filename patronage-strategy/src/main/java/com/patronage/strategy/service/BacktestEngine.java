package com.patronage.strategy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.patronage.strategy.entity.BacktestResult;

import java.time.LocalDate;
import java.util.Map;

/**
 * 回测引擎服务接口
 */
public interface BacktestEngine extends IService<BacktestResult> {

    /**
     * 执行回测
     */
    Long executeBacktest(Long strategyId, String backtestName, LocalDate startDate, LocalDate endDate, String parameters);

    /**
     * 获取回测进度
     */
    Map<String, Object> getBacktestProgress(Long backtestId);

    /**
     * 停止回测
     */
    boolean stopBacktest(Long backtestId);

    /**
     * 分页查询回测结果
     */
    IPage<BacktestResult> getBacktestResultPage(Integer pageNum, Integer pageSize, Long strategyId, Integer status);

    /**
     * 获取回测结果详情
     */
    BacktestResult getBacktestResultDetail(Long backtestId);

    /**
     * 获取回测结果图表数据
     */
    Map<String, Object> getBacktestChartData(Long backtestId);

    /**
     * 删除回测结果
     */
    boolean deleteBacktestResult(Long backtestId);
} 