package com.patronage.strategy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.patronage.strategy.entity.Strategy;
import com.patronage.strategy.service.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 策略管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/strategy")
public class StrategyController {

    @Autowired
    private StrategyService strategyService;

    /**
     * 分页查询策略列表
     */
    @GetMapping("/list")
    public Map<String, Object> getStrategyList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String strategyName,
            @RequestParam(required = false) Integer strategyType,
            @RequestParam(required = false) Integer status) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            IPage<Strategy> page = strategyService.getStrategyPage(pageNum, pageSize, strategyName, strategyType, status);
            result.put("success", true);
            result.put("data", page);
        } catch (Exception e) {
            log.error("查询策略列表失败", e);
            result.put("success", false);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 创建策略
     */
    @PostMapping("/create")
    public Map<String, Object> createStrategy(@RequestBody Strategy strategy) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = strategyService.createStrategy(strategy);
            result.put("success", success);
            if (success) {
                result.put("message", "策略创建成功");
                result.put("data", strategy);
            } else {
                result.put("message", "策略创建失败");
            }
        } catch (Exception e) {
            log.error("创建策略失败", e);
            result.put("success", false);
            result.put("message", "创建失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 更新策略
     */
    @PutMapping("/update")
    public Map<String, Object> updateStrategy(@RequestBody Strategy strategy) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = strategyService.updateStrategy(strategy);
            result.put("success", success);
            result.put("message", success ? "策略更新成功" : "策略更新失败");
        } catch (Exception e) {
            log.error("更新策略失败", e);
            result.put("success", false);
            result.put("message", "更新失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 删除策略
     */
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> deleteStrategy(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = strategyService.deleteStrategy(id);
            result.put("success", success);
            result.put("message", success ? "策略删除成功" : "策略删除失败");
        } catch (Exception e) {
            log.error("删除策略失败", e);
            result.put("success", false);
            result.put("message", "删除失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 启动策略
     */
    @PostMapping("/start/{id}")
    public Map<String, Object> startStrategy(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = strategyService.startStrategy(id);
            result.put("success", success);
            result.put("message", success ? "策略启动成功" : "策略启动失败");
        } catch (Exception e) {
            log.error("启动策略失败", e);
            result.put("success", false);
            result.put("message", "启动失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 停止策略
     */
    @PostMapping("/stop/{id}")
    public Map<String, Object> stopStrategy(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = strategyService.stopStrategy(id);
            result.put("success", success);
            result.put("message", success ? "策略停止成功" : "策略停止失败");
        } catch (Exception e) {
            log.error("停止策略失败", e);
            result.put("success", false);
            result.put("message", "停止失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 暂停策略
     */
    @PostMapping("/pause/{id}")
    public Map<String, Object> pauseStrategy(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = strategyService.pauseStrategy(id);
            result.put("success", success);
            result.put("message", success ? "策略暂停成功" : "策略暂停失败");
        } catch (Exception e) {
            log.error("暂停策略失败", e);
            result.put("success", false);
            result.put("message", "暂停失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取策略详情
     */
    @GetMapping("/detail/{id}")
    public Map<String, Object> getStrategyDetail(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Strategy strategy = strategyService.getStrategyDetail(id);
            result.put("success", true);
            result.put("data", strategy);
        } catch (Exception e) {
            log.error("获取策略详情失败", e);
            result.put("success", false);
            result.put("message", "获取详情失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取策略统计信息
     */
    @GetMapping("/statistics")
    public Map<String, Object> getStrategyStatistics() {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> statistics = strategyService.getStrategyStatistics();
            result.put("success", true);
            result.put("data", statistics);
        } catch (Exception e) {
            log.error("获取策略统计信息失败", e);
            result.put("success", false);
            result.put("message", "获取统计信息失败: " + e.getMessage());
        }
        return result;
    }
} 