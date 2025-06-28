package com.patronage.strategy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.patronage.strategy.common.Result;
import com.patronage.strategy.entity.Strategy;
import com.patronage.strategy.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}