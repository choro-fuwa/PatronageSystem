package com.PatronageSystem.service;

import com.PatronageSystem.entity.Factor;
import java.util.List;

/**
 * 因子Service接口
 */
public interface FactorService {
    
    /**
     * 查询所有因子
     */
    List<Factor> getAllFactors();
    
    /**
     * 根据ID查询因子
     */
    Factor getFactorById(Long id);
    
    /**
     * 根据分类ID查询因子
     */
    List<Factor> getFactorsByCategoryId(Long categoryId);
    
    /**
     * 根据因子代码查询
     */
    Factor getFactorByCode(String factorCode);
    
    /**
     * 条件查询因子
     */
    List<Factor> getFactorsByCondition(String factorType, String riskLevel, Integer isPublic);
    
    /**
     * 根据创建者查询因子
     */
    List<Factor> getFactorsByCreatorId(Long creatorId);
    
    /**
     * 新增因子
     */
    boolean addFactor(Factor factor);
    
    /**
     * 更新因子
     */
    boolean updateFactor(Factor factor);
    
    /**
     * 删除因子
     */
    boolean deleteFactor(Long id);
    
    /**
     * 检查因子代码是否已存在
     */
    boolean isFactorCodeExists(String factorCode);
} 