package com.patronage.strategy.controller;

import com.patronage.strategy.entity.BacktestResult;
import com.patronage.strategy.service.BacktestEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 回测控制器
 */
@RestController
@RequestMapping("/api/backtest")
public class BacktestController {

    @Autowired
    private BacktestEngine backtestEngine;

    /**
     * 执行回测
     */
    @PostMapping("/run")
    public Map<String, Object> runBacktest(@RequestBody Map<String, Object> request) {
        Long strategyId = Long.valueOf(request.get("strategyId").toString());
        String startDateStr = request.get("startDate").toString();
        String endDateStr = request.get("endDate").toString();
        @SuppressWarnings("unchecked")
        Map<String, Object> parameters = (Map<String, Object>) request.get("parameters");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
        LocalDateTime endDate = LocalDateTime.parse(endDateStr, formatter);
        
        try {
            BacktestResult result = backtestEngine.runBacktest(strategyId, startDate, endDate, parameters);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "回测执行成功");
            response.put("data", result);
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "回测执行失败: " + e.getMessage());
            return response;
        }
    }

    /**
     * 获取回测结果
     */
    @GetMapping("/{id}")
    public Map<String, Object> getBacktestResult(@PathVariable Long id) {
        BacktestResult result = backtestEngine.getBacktestResult(id);
        
        Map<String, Object> response = new HashMap<>();
        if (result != null) {
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", result);
        } else {
            response.put("code", 404);
            response.put("message", "回测结果不存在");
        }
        return response;
    }

    /**
     * 获取策略的回测历史
     */
    @GetMapping("/history/{strategyId}")
    public Map<String, Object> getBacktestHistory(@PathVariable Long strategyId) {
        List<BacktestResult> history = backtestEngine.getBacktestHistory(strategyId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", history);
        return response;
    }

    /**
     * 删除回测结果
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteBacktestResult(@PathVariable Long id) {
        boolean success = backtestEngine.deleteBacktestResult(id);
        
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "删除成功");
        } else {
            response.put("code", 500);
            response.put("message", "删除失败");
        }
        return response;
    }

    /**
     * 获取回测进度
     */
    @GetMapping("/{id}/progress")
    public Map<String, Object> getBacktestProgress(@PathVariable Long id) {
        Map<String, Object> progress = backtestEngine.getBacktestProgress(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", progress);
        return response;
    }
} 