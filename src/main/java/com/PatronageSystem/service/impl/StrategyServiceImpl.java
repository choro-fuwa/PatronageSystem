package com.PatronageSystem.service.impl;

import com.PatronageSystem.entity.Strategy;
import com.PatronageSystem.mapper.StrategyMapper;
import com.PatronageSystem.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 策略Service实现类
 */
@Service
public class StrategyServiceImpl implements StrategyService {
    
    @Autowired
    private StrategyMapper strategyMapper;
    
    @Override
    public List<Strategy> getAllStrategies() {
        return strategyMapper.selectAll();
    }
    
    @Override
    public Strategy getStrategyById(Long id) {
        return strategyMapper.selectById(id);
    }
    
    @Override
    public Strategy getStrategyByCode(String strategyCode) {
        return strategyMapper.selectByStrategyCode(strategyCode);
    }
    
    @Override
    public List<Strategy> getStrategiesByCreatorId(Long creatorId) {
        return strategyMapper.selectByCreatorId(creatorId);
    }
    
    @Override
    public List<Strategy> getStrategiesByCondition(String strategyType, String riskLevel, Integer isPublic) {
        return strategyMapper.selectByCondition(strategyType, riskLevel, isPublic);
    }
    
    @Override
    public List<Strategy> searchStrategies(String keyword) {
        return strategyMapper.selectByKeyword(keyword);
    }
    
    @Override
    public boolean addStrategy(Strategy strategy) {
        // 检查策略代码是否已存在
        if (isStrategyCodeExists(strategy.getStrategyCode())) {
            throw new RuntimeException("策略代码已存在");
        }
        
        // 设置默认值
        if (strategy.getStatus() == null) {
            strategy.setStatus(1);
        }
        if (strategy.getIsPublic() == null) {
            strategy.setIsPublic(1);
        }
        
        return strategyMapper.insert(strategy) > 0;
    }
    
    @Override
    public boolean updateStrategy(Strategy strategy) {
        // 检查策略代码是否已被其他策略使用
        Strategy existingStrategy = strategyMapper.selectByStrategyCode(strategy.getStrategyCode());
        if (existingStrategy != null && !existingStrategy.getId().equals(strategy.getId())) {
            throw new RuntimeException("策略代码已被其他策略使用");
        }
        
        return strategyMapper.update(strategy) > 0;
    }
    
    @Override
    public boolean deleteStrategy(Long id) {
        return strategyMapper.deleteById(id) > 0;
    }
    
    @Override
    public boolean isStrategyCodeExists(String strategyCode) {
        return strategyMapper.checkStrategyCodeExists(strategyCode) > 0;
    }
    
    @Override
    public Map<String, Object> getStrategyStats() {
        return strategyMapper.selectStrategyStats();
    }
    
    @Override
    public List<Map<String, Object>> getStrategyCountByType() {
        return strategyMapper.selectStrategyCountByType();
    }
    
    @Override
    public List<Map<String, Object>> getStrategyCountByRiskLevel() {
        return strategyMapper.selectStrategyCountByRiskLevel();
    }
} 