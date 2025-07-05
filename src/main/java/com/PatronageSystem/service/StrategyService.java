package com.PatronageSystem.service;

import com.PatronageSystem.entity.Strategy;
import java.util.List;
import java.util.Map;

/**
 * 策略Service接口
 */
public interface StrategyService {
    
    /**
     * 获取所有策略
     */
    List<Strategy> getAllStrategies();
    
    /**
     * 根据ID获取策略
     */
    Strategy getStrategyById(Long id);
    
    /**
     * 根据策略代码获取策略
     */
    Strategy getStrategyByCode(String strategyCode);
    
    /**
     * 根据创建者获取策略
     */
    List<Strategy> getStrategiesByCreatorId(Long creatorId);
    
    /**
     * 条件查询策略
     */
    List<Strategy> getStrategiesByCondition(String strategyType, String riskLevel, Integer isPublic);
    
    /**
     * 关键词搜索策略
     */
    List<Strategy> searchStrategies(String keyword);
    
    /**
     * 新增策略
     */
    boolean addStrategy(Strategy strategy);
    
    /**
     * 更新策略
     */
    boolean updateStrategy(Strategy strategy);
    
    /**
     * 删除策略
     */
    boolean deleteStrategy(Long id);
    
    /**
     * 检查策略代码是否存在
     */
    boolean isStrategyCodeExists(String strategyCode);
    
    /**
     * 获取策略统计信息
     */
    Map<String, Object> getStrategyStats();
    
    /**
     * 按策略类型统计
     */
    List<Map<String, Object>> getStrategyCountByType();
    
    /**
     * 按风险等级统计
     */
    List<Map<String, Object>> getStrategyCountByRiskLevel();
} 