package com.PatronageSystem.Patronage.service.impl;

import com.PatronageSystem.Patronage.entity.RebalancePlan;
import com.PatronageSystem.Patronage.mapper.RebalancePlanMapper;
import com.PatronageSystem.Patronage.service.RebalancePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RebalancePlanServiceImpl implements RebalancePlanService {
    @Autowired
    private RebalancePlanMapper rebalancePlanMapper;

    @Override
    public int createRebalancePlan(RebalancePlan plan) {
        return rebalancePlanMapper.insert(plan);
    }

    @Override
    public int updateRebalancePlan(RebalancePlan plan) {
        return rebalancePlanMapper.update(plan);
    }

    @Override
    public int deleteRebalancePlan(Long id) {
        return rebalancePlanMapper.deleteById(id);
    }

    @Override
    public RebalancePlan getRebalancePlanById(Long id) {
        return rebalancePlanMapper.selectById(id);
    }

    @Override
    public List<RebalancePlan> getAllRebalancePlans() {
        return rebalancePlanMapper.selectAll();
    }
} 