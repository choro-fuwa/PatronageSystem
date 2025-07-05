package com.PatronageSystem.Patronage.controller;

import com.PatronageSystem.Patronage.entity.Position;
import com.PatronageSystem.Patronage.entity.RebalancePlan;
import com.PatronageSystem.Patronage.service.PositionService;
import com.PatronageSystem.Patronage.service.RebalancePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/trade/account")
@CrossOrigin(origins = "*")
public class AccountRebalanceController {
    @Autowired
    private PositionService positionService;
    @Autowired
    private RebalancePlanService rebalancePlanService;

    @GetMapping("/{accountId}/positions")
    public List<Position> getAccountPositions(@PathVariable Long accountId) {
        return positionService.getPositionsByAccountId(accountId);
    }

    @PostMapping("/rebalance")
    public String createAccountRebalance(@RequestBody RebalancePlan plan) {
        plan.setStatus("PENDING");
        rebalancePlanService.createRebalancePlan(plan);
        return "success";
    }
} 