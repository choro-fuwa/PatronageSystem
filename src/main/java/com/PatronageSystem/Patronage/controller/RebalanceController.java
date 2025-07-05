package com.PatronageSystem.Patronage.controller;

import com.PatronageSystem.Patronage.entity.RebalancePlan;
import com.PatronageSystem.Patronage.service.RebalancePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trade/rebalance/plan")
@CrossOrigin(origins = "*")
public class RebalanceController {
    @Autowired
    private RebalancePlanService rebalancePlanService;

    @PostMapping
    public String createRebalancePlan(@RequestBody RebalancePlan plan) {
        int result = rebalancePlanService.createRebalancePlan(plan);
        return result > 0 ? "success" : "fail";
    }

    @GetMapping("/{id}")
    public RebalancePlan getRebalancePlanById(@PathVariable Long id) {
        return rebalancePlanService.getRebalancePlanById(id);
    }

    @GetMapping
    public List<RebalancePlan> getAllRebalancePlans() {
        return rebalancePlanService.getAllRebalancePlans();
    }

    @PutMapping
    public String updateRebalancePlan(@RequestBody RebalancePlan plan) {
        int result = rebalancePlanService.updateRebalancePlan(plan);
        return result > 0 ? "success" : "fail";
    }

    @DeleteMapping("/{id}")
    public String deleteRebalancePlan(@PathVariable Long id) {
        int result = rebalancePlanService.deleteRebalancePlan(id);
        return result > 0 ? "success" : "fail";
    }

    @PostMapping("/with-instructions")
    public RebalancePlan createPlanAndGenerateInstructions(@RequestBody RebalancePlan plan) {
        // 简单模拟：根据当前持仓和组合类型生成交易指令
        String instructionsJson = "[{\"action\":\"买入\",\"asset\":\"A股\",\"amount\":10000},{\"action\":\"卖出\",\"asset\":\"债券\",\"amount\":5000}]";
        plan.setRebalanceInstructions(instructionsJson);
        plan.setStatus("PENDING");
        rebalancePlanService.createRebalancePlan(plan);
        return plan;
    }

    // 下发交易指令到外部系统（模拟）
    @PostMapping("/{id}/dispatch")
    public String dispatchInstructions(@PathVariable Long id) {
        RebalancePlan plan = rebalancePlanService.getRebalancePlanById(id);
        if (plan == null) {
            return "plan not found";
        }
        // 模拟下发：打印日志或返回成功
        plan.setStatus("DISPATCHED");
        rebalancePlanService.updateRebalancePlan(plan);
        return "dispatched";
    }
} 