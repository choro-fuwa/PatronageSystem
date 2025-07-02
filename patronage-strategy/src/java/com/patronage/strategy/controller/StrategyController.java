package com.patronage.strategy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.patronage.strategy.common.Result;
import com.patronage.strategy.entity.Strategy;
import com.patronage.strategy.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 策略控制器
 */
@RestController
@RequestMapping("/api/strategy")
public class StrategyController {

    @Autowired
    private StrategyService strategyService;

    /**
     * 分页查询策略列表
     */
    @GetMapping("/list")
    public Result<IPage<Strategy>> getStrategyList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String strategyName,
            @RequestParam(required = false) Integer strategyType,
            @RequestParam(required = false) Integer status) {
        
        IPage<Strategy> page = strategyService.getStrategyPage(pageNum, pageSize, strategyName, strategyType, status);
        return Result.success("查询成功", page);
    }

    /**
     * 获取策略详情
     */
    @GetMapping("/{id}")
    public Result<Strategy> getStrategyDetail(@PathVariable Long id) {
        Strategy strategy = strategyService.getStrategyDetail(id);
        if (strategy != null) {
            return Result.success("查询成功", strategy);
        } else {
            return Result.error(404, "策略不存在");
        }
    }

    /**
     * 创建策略
     */
    @PostMapping
    public Result<Void> createStrategy(@RequestBody Strategy strategy) {
        boolean success = strategyService.createStrategy(strategy);
        if (success) {
            return Result.success();
        } else {
            return Result.error("创建失败");
        }
    }

    /**
     * 更新策略
     */
    @PutMapping("/{id}")
    public Result<Void> updateStrategy(@PathVariable Long id, @RequestBody Strategy strategy) {
        strategy.setId(id);
        boolean success = strategyService.updateStrategy(strategy);
        if (success) {
            return Result.success();
        } else {
            return Result.error("更新失败");
        }
    }

    /**
     * 删除策略
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteStrategy(@PathVariable Long id) {
        boolean success = strategyService.deleteStrategy(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除失败");
        }
    }

    /**
     * 启动策略
     */
    @PostMapping("/{id}/start")
    public Result<Void> startStrategy(@PathVariable Long id) {
        boolean success = strategyService.startStrategy(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("启动失败");
        }
    }

    /**
     * 停止策略
     */
    @PostMapping("/{id}/stop")
    public Result<Void> stopStrategy(@PathVariable Long id) {
        boolean success = strategyService.stopStrategy(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("停止失败");
        }
    }

    /**
     * 暂停策略
     */
    @PostMapping("/{id}/pause")
    public Result<Void> pauseStrategy(@PathVariable Long id) {
        boolean success = strategyService.pauseStrategy(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("暂停失败");
        }
    }

    /**
     * 获取运行中的策略列表
     */
    @GetMapping("/running")
    public Result<List<Strategy>> getRunningStrategies() {
        List<Strategy> strategies = strategyService.getRunningStrategies();
        return Result.success("查询成功", strategies);
    }

    /**
     * 获取策略统计信息
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStrategyStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalCount", strategyService.getTotalCount());
        statistics.put("runningCount", strategyService.getRunningCount());
        statistics.put("pausedCount", strategyService.getPausedCount());
        statistics.put("stoppedCount", strategyService.getStoppedCount());
        return Result.success("查询成功", statistics);
    }

    /**
     * 获取策略持仓
     */
    @GetMapping("/{id}/positions")
    public Result<Map<String, Object>> getStrategyPositions(@PathVariable Long id) {
        Map<String, Object> positions = strategyService.getStrategyPositions(id);
        return Result.success("查询成功", positions);
    }

    /**
     * 获取策略风险指标
     */
    @GetMapping("/{id}/risk-metrics")
    public Result<Map<String, Object>> getStrategyRiskMetrics(@PathVariable Long id) {
        Map<String, Object> riskMetrics = strategyService.getStrategyRiskMetrics(id);
        return Result.success("查询成功", riskMetrics);
    }

    /**
     * 执行再平衡
     */
    @PostMapping("/{id}/rebalance")
    public Result<Void> executeRebalance(@PathVariable Long id, @RequestParam String adjustmentType) {
        boolean success = strategyService.executeRebalance(id, adjustmentType);
        if (success) {
            return Result.success();
        } else {
            return Result.error("再平衡执行失败");
        }
    }

    /**
     * 手动调整持仓
     */
    @PostMapping("/{id}/adjust")
    public Result<Void> adjustPositions(@PathVariable Long id, @RequestBody Map<String, Object> adjustmentData) {
        boolean success = strategyService.adjustPositions(id, adjustmentData);
        if (success) {
            return Result.success();
        } else {
            return Result.error("持仓调整失败");
        }
    }

    /**
     * 获取调仓历史
     */
    @GetMapping("/{id}/adjustments")
    public Result<List<Map<String, Object>>> getAdjustmentHistory(@PathVariable Long id) {
        List<Map<String, Object>> history = strategyService.getAdjustmentHistory(id);
        return Result.success("查询成功", history);
    }

    /**
     * 启动模拟运行
     */
    @PostMapping("/{id}/simulate")
    public Result<Void> startSimulation(@PathVariable Long id) {
        boolean success = strategyService.startSimulation(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("模拟运行启动失败");
        }
    }

    /**
     * 复制策略
     */
    @PostMapping("/{id}/copy")
    public Result<Void> copyStrategy(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String name = request.get("name");
        boolean success = strategyService.copyStrategy(id, name);
        if (success) {
            return Result.success();
        } else {
            return Result.error("策略复制失败");
        }
    }

    /**
     * 导出策略配置
     */
    @GetMapping("/{id}/export")
    public Result<Map<String, Object>> exportStrategyConfig(@PathVariable Long id) {
        Map<String, Object> config = strategyService.exportStrategyConfig(id);
        return Result.success("导出成功", config);
    }

    /**
     * 导入策略配置
     */
    @PostMapping("/import")
    public Result<Void> importStrategyConfig(@RequestBody Map<String, Object> config) {
        boolean success = strategyService.importStrategyConfig(config);
        if (success) {
            return Result.success();
        } else {
            return Result.error("策略导入失败");
        }
    }

    /**
     * 策略审核
     */
    @PostMapping("/{id}/approve")
    public Result<Void> approveStrategy(@PathVariable Long id, @RequestBody Map<String, Object> approvalData) {
        boolean success = strategyService.approveStrategy(id, approvalData);
        if (success) {
            return Result.success();
        } else {
            return Result.error("策略审核失败");
        }
    }

    /**
     * 策略拒绝
     */
    @PostMapping("/{id}/reject")
    public Result<Void> rejectStrategy(@PathVariable Long id, @RequestBody Map<String, Object> rejectionData) {
        boolean success = strategyService.rejectStrategy(id, rejectionData);
        if (success) {
            return Result.success();
        } else {
            return Result.error("策略拒绝失败");
        }
    }

    /**
     * 调整策略
     */
    @PostMapping("/adjust")
    public Result<Void> adjustStrategy(@RequestBody Map<String, Object> adjustmentData) {
        boolean success = strategyService.adjustStrategy(adjustmentData);
        if (success) {
            return Result.success();
        } else {
            return Result.error("策略调整失败");
        }
    }
}