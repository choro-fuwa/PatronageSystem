package com.patronage.strategy.controller;

import com.patronage.strategy.entity.Strategy;
import com.patronage.strategy.entity.StrategyBacktest;
import com.patronage.strategy.entity.StrategyMonitor;
import com.patronage.strategy.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 策略管理Controller
 */
@RestController
@RequestMapping("/api/strategy")
@CrossOrigin(origins = "*")
public class StrategyController {

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private com.patronage.strategy.mapper.StrategyBacktestMapper strategyBacktestMapper;

    @Autowired
    private com.patronage.strategy.mapper.StrategyMonitorMapper strategyMonitorMapper;

    /**
     * 获取所有策略
     */
    @GetMapping("/list")
    public Map<String, Object> getStrategies() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Strategy> strategies = strategyService.getAllStrategies();
            result.put("success", true);
            result.put("data", strategies);
            result.put("message", "获取策略列表成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取策略列表失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 根据ID获取策略详情
     */
    @GetMapping("/detail/{id}")
    public Map<String, Object> getStrategyDetail(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Strategy strategy = strategyService.getStrategyById(id);
            if (strategy != null) {
                result.put("success", true);
                result.put("data", strategy);
                result.put("message", "获取策略详情成功");
            } else {
                result.put("success", false);
                result.put("message", "策略不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取策略详情失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 条件查询策略
     */
    @GetMapping("/search")
    public Map<String, Object> searchStrategies(@RequestParam(required = false) String strategyType,
                                                @RequestParam(required = false) String riskLevel,
                                                @RequestParam(required = false) Integer isPublic) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Strategy> strategies = strategyService.getStrategiesByCondition(strategyType, riskLevel, isPublic);
            result.put("success", true);
            result.put("data", strategies);
            result.put("message", "查询策略成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询策略失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 关键词搜索策略
     */
    @GetMapping("/search/keyword")
    public Map<String, Object> searchStrategiesByKeyword(@RequestParam String keyword) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Strategy> strategies = strategyService.searchStrategies(keyword);
            result.put("success", true);
            result.put("data", strategies);
            result.put("message", "搜索策略成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "搜索策略失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 新增策略
     */
    @PostMapping("/add")
    public Map<String, Object> addStrategy(@RequestBody Strategy strategy) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = strategyService.addStrategy(strategy);
            result.put("success", success);
            result.put("message", success ? "新增策略成功" : "新增策略失败");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "新增策略失败：" + e.getMessage());
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
            result.put("message", success ? "更新策略成功" : "更新策略失败");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新策略失败：" + e.getMessage());
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
            result.put("message", success ? "删除策略成功" : "删除策略失败");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除策略失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 检查策略代码是否存在
     */
    @GetMapping("/check-code/{strategyCode}")
    public Map<String, Object> checkStrategyCode(@PathVariable String strategyCode) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean exists = strategyService.isStrategyCodeExists(strategyCode);
            result.put("success", true);
            result.put("exists", exists);
            result.put("message", exists ? "策略代码已存在" : "策略代码可用");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "检查策略代码失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取策略统计信息
     */
    @GetMapping("/stats")
    public Map<String, Object> getStrategyStats() {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> stats = strategyService.getStrategyStats();
            result.put("success", true);
            result.put("data", stats);
            result.put("message", "获取策略统计成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取策略统计失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 按策略类型统计
     */
    @GetMapping("/stats/by-type")
    public Map<String, Object> getStrategyCountByType() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> stats = strategyService.getStrategyCountByType();
            result.put("success", true);
            result.put("data", stats);
            result.put("message", "获取策略类型统计成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取策略类型统计失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 按风险等级统计
     */
    @GetMapping("/stats/by-risk-level")
    public Map<String, Object> getStrategyCountByRiskLevel() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> stats = strategyService.getStrategyCountByRiskLevel();
            result.put("success", true);
            result.put("data", stats);
            result.put("message", "获取风险等级统计成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取风险等级统计失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/backtest/{strategyId}")
    public List<StrategyBacktest> getStrategyBacktests(@PathVariable Long strategyId) {
        return strategyBacktestMapper.selectByStrategyId(strategyId);
    }

    @GetMapping("/monitor/{strategyId}")
    public List<StrategyMonitor> getStrategyMonitors(@PathVariable Long strategyId) {
        return strategyMonitorMapper.selectByStrategyId(strategyId);
    }
} 
