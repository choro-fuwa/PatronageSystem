package com.PatronageSystem.service.impl;

import com.PatronageSystem.entity.Factor;
import com.PatronageSystem.mapper.FactorMapper;
import com.PatronageSystem.service.FactorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 因子Service实现类
 */
@Service
public class FactorServiceImpl implements FactorService {
    
    @Autowired
    private FactorMapper factorMapper;
    
    @Override
    public List<Factor> getAllFactors() {
        return factorMapper.selectAll();
    }
    
    @Override
    public Factor getFactorById(Long id) {
        return factorMapper.selectById(id);
    }
    
    @Override
    public List<Factor> getFactorsByCategoryId(Long categoryId) {
        return factorMapper.selectByCategoryId(categoryId);
    }
    
    @Override
    public Factor getFactorByCode(String factorCode) {
        return factorMapper.selectByFactorCode(factorCode);
    }
    
    @Override
    public List<Factor> getFactorsByCondition(String factorType, String riskLevel, Integer isPublic) {
        return factorMapper.selectByCondition(factorType, riskLevel, isPublic);
    }
    
    @Override
    public List<Factor> getFactorsByCreatorId(Long creatorId) {
        return factorMapper.selectByCreatorId(creatorId);
    }
    
    @Override
    public boolean addFactor(Factor factor) {
        // 检查因子代码是否已存在
        if (isFactorCodeExists(factor.getFactorCode())) {
            throw new RuntimeException("因子代码已存在");
        }
        
        // 设置默认值
        if (factor.getStatus() == null) {
            factor.setStatus(1);
        }
        if (factor.getIsPublic() == null) {
            factor.setIsPublic(1);
        }
        if (factor.getUpdateFrequency() == null) {
            factor.setUpdateFrequency("daily");
        }
        
        return factorMapper.insert(factor) > 0;
    }
    
    @Override
    public boolean updateFactor(Factor factor) {
        // 检查因子代码是否已被其他因子使用
        Factor existingFactor = factorMapper.selectByFactorCode(factor.getFactorCode());
        if (existingFactor != null && !existingFactor.getId().equals(factor.getId())) {
            throw new RuntimeException("因子代码已被其他因子使用");
        }
        
        return factorMapper.update(factor) > 0;
    }
    
    @Override
    public boolean deleteFactor(Long id) {
        return factorMapper.deleteById(id) > 0;
    }
    
    @Override
    public boolean isFactorCodeExists(String factorCode) {
        return factorMapper.selectByFactorCode(factorCode) != null;
    }
} 