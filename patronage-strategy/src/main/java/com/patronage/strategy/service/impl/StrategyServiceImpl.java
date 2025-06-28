package com.patronage.strategy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.patronage.strategy.entity.Strategy;
import com.patronage.strategy.mapper.StrategyMapper;
import com.patronage.strategy.service.StrategyService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 策略服务实现类
 */
@Service
public class StrategyServiceImpl extends ServiceImpl<StrategyMapper, Strategy> implements StrategyService {

    @Override
    public IPage<Strategy> getStrategyPage(Integer pageNum, Integer pageSize, String strategyName, Integer strategyType, Integer status) {
        Page<Strategy> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Strategy> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(strategyName)) {
            wrapper.like(Strategy::getStrategyName, strategyName);
        }
        if (strategyType != null) {
            wrapper.eq(Strategy::getStrategyType, strategyType);
        }
        if (status != null) {
            wrapper.eq(Strategy::getStatus, status);
        }
        
        wrapper.orderByDesc(Strategy::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public boolean createStrategy(Strategy strategy) {
        strategy.setStatus(0); // 设置为草稿状态
        return this.save(strategy);
    }

    @Override
    public boolean updateStrategy(Strategy strategy) {
        return this.updateById(strategy);
    }

    @Override
    public boolean deleteStrategy(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean startStrategy(Long id) {
        Strategy strategy = this.getById(id);
        if (strategy != null) {
            strategy.setStatus(1); // 设置为运行中
            return this.updateById(strategy);
        }
        return false;
    }

    @Override
    public boolean stopStrategy(Long id) {
        Strategy strategy = this.getById(id);
        if (strategy != null) {
            strategy.setStatus(3); // 设置为已停止
            return this.updateById(strategy);
        }
        return false;
    }

    @Override
    public boolean pauseStrategy(Long id) {
        Strategy strategy = this.getById(id);
        if (strategy != null) {
            strategy.setStatus(2); // 设置为暂停
            return this.updateById(strategy);
        }
        return false;
    }

    @Override
    public Strategy getStrategyDetail(Long id) {
        return this.getById(id);
    }

    @Override
    public List<Strategy> getRunningStrategies() {
        LambdaQueryWrapper<Strategy> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Strategy::getStatus, 1); // 运行中的策略
        return this.list(wrapper);
    }
} 