package com.PatronageSystem.Patronage.service;

import com.PatronageSystem.Patronage.entity.RebalancePlan;
import java.util.List;

public interface RebalancePlanService {
    int createRebalancePlan(RebalancePlan plan);
    int updateRebalancePlan(RebalancePlan plan);
    int deleteRebalancePlan(Long id);
    RebalancePlan getRebalancePlanById(Long id);
    List<RebalancePlan> getAllRebalancePlans();
} 