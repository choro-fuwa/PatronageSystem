package com.patronage.strategy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.patronage.strategy.entity.BacktestResult;
import com.patronage.strategy.service.BacktestEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * 回测管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/backtest")
public class BacktestController {

    @Autowired
    private BacktestEngine backtestEngine;

    /**
     * 执行回测
     */
    @PostMapping("/execute")
    public Map<String, Object> executeBacktest(
            @RequestParam Long strategyId,
            @RequestParam String backtestName,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(required = false) String parameters) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            
            Long backtestId = backtestEngine.executeBacktest(strategyId, backtestName, start, end, parameters);
            result.put("success", true);
            result.put("message", "回测任务已提交");
            result.put("data", backtestId);
        } catch (Exception e) {
            log.error("执行回测失败", e);
            result.put("success", false);
            result.put("message", "执行失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取回测进度
     */
    @GetMapping("/progress/{backtestId}")
    public Map<String, Object> getBacktestProgress(@PathVariable Long backtestId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> progress = backtestEngine.getBacktestProgress(backtestId);
            result.put("success", true);
            result.put("data", progress);
        } catch (Exception e) {
            log.error("获取回测进度失败", e);
            result.put("success", false);
            result.put("message", "获取进度失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 停止回测
     */
    @PostMapping("/stop/{backtestId}")
    public Map<String, Object> stopBacktest(@PathVariable Long backtestId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = backtestEngine.stopBacktest(backtestId);
            result.put("success", success);
            result.put("message", success ? "回测已停止" : "停止回测失败");
        } catch (Exception e) {
            log.error("停止回测失败", e);
            result.put("success", false);
            result.put("message", "停止失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询回测结果
     */
    @GetMapping("/list")
    public Map<String, Object> getBacktestResultList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long strategyId,
            @RequestParam(required = false) Integer status) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            IPage<BacktestResult> page = backtestEngine.getBacktestResultPage(pageNum, pageSize, strategyId, status);
            result.put("success", true);
            result.put("data", page);
        } catch (Exception e) {
            log.error("查询回测结果失败", e);
            result.put("success", false);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取回测结果详情
     */
    @GetMapping("/detail/{backtestId}")
    public Map<String, Object> getBacktestResultDetail(@PathVariable Long backtestId) {
        Map<String, Object> result = new HashMap<>();
        try {
            BacktestResult backtestResult = backtestEngine.getBacktestResultDetail(backtestId);
            result.put("success", true);
            result.put("data", backtestResult);
        } catch (Exception e) {
            log.error("获取回测结果详情失败", e);
            result.put("success", false);
            result.put("message", "获取详情失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取回测结果图表数据
     */
    @GetMapping("/chart/{backtestId}")
    public Map<String, Object> getBacktestChartData(@PathVariable Long backtestId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> chartData = backtestEngine.getBacktestChartData(backtestId);
            result.put("success", true);
            result.put("data", chartData);
        } catch (Exception e) {
            log.error("获取回测图表数据失败", e);
            result.put("success", false);
            result.put("message", "获取图表数据失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 删除回测结果
     */
    @DeleteMapping("/delete/{backtestId}")
    public Map<String, Object> deleteBacktestResult(@PathVariable Long backtestId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = backtestEngine.deleteBacktestResult(backtestId);
            result.put("success", success);
            result.put("message", success ? "回测结果删除成功" : "删除失败");
        } catch (Exception e) {
            log.error("删除回测结果失败", e);
            result.put("success", false);
            result.put("message", "删除失败: " + e.getMessage());
        }
        return result;
    }
} 