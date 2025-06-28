package com.patronage.strategy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.patronage.strategy.entity.Strategy;
import com.patronage.strategy.mapper.StrategyMapper;
import com.patronage.strategy.service.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 策略服务实现类
 */
@Slf4j
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
        // TODO: 验证策略参数
        strategy.setStatus(0); // 设置为草稿状态
        return this.save(strategy);
    }

    @Override
    public boolean updateStrategy(Strategy strategy) {
        // TODO: 验证策略参数
        return this.updateById(strategy);
    }

    @Override
    public boolean deleteStrategy(Long id) {
        // TODO: 检查策略是否可以删除（如运行中的策略不能删除）
        return this.removeById(id);
    }

    @Override
    public boolean startStrategy(Long id) {
        Strategy strategy = this.getById(id);
        if (strategy == null) {
            return false;
        }
        
        // TODO: 调用Python脚本启动策略
        strategy.setStatus(1); // 设置为运行中
        return this.updateById(strategy);
    }

    @Override
    public boolean stopStrategy(Long id) {
        Strategy strategy = this.getById(id);
        if (strategy == null) {
            return false;
        }
        
        // TODO: 调用Python脚本停止策略
        strategy.setStatus(3); // 设置为已停止
        return this.updateById(strategy);
    }

    @Override
    public boolean pauseStrategy(Long id) {
        Strategy strategy = this.getById(id);
        if (strategy == null) {
            return false;
        }
        
        // TODO: 调用Python脚本暂停策略
        strategy.setStatus(2); // 设置为暂停
        return this.updateById(strategy);
    }

    @Override
    public Strategy getStrategyDetail(Long id) {
        return this.getById(id);
    }

    @Override
    public Map<String, Object> getStrategyStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 统计各状态策略数量
        LambdaQueryWrapper<Strategy> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Strategy::getStatus, 0);
        statistics.put("draftCount", this.count(wrapper));
        
        wrapper.clear();
        wrapper.eq(Strategy::getStatus, 1);
        statistics.put("runningCount", this.count(wrapper));
        
        wrapper.clear();
        wrapper.eq(Strategy::getStatus, 2);
        statistics.put("pausedCount", this.count(wrapper));
        
        wrapper.clear();
        wrapper.eq(Strategy::getStatus, 3);
        statistics.put("stoppedCount", this.count(wrapper));
        
        // 统计各类型策略数量
        wrapper.clear();
        wrapper.eq(Strategy::getStrategyType, 1);
        statistics.put("trendCount", this.count(wrapper));
        
        wrapper.clear();
        wrapper.eq(Strategy::getStrategyType, 2);
        statistics.put("meanReversionCount", this.count(wrapper));
        
        wrapper.clear();
        wrapper.eq(Strategy::getStrategyType, 3);
        statistics.put("arbitrageCount", this.count(wrapper));
        
        wrapper.clear();
        wrapper.eq(Strategy::getStrategyType, 4);
        statistics.put("quantitativeCount", this.count(wrapper));
        
        return statistics;
    }
} 