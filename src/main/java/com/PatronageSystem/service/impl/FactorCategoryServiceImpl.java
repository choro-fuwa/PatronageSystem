package com.PatronageSystem.service.impl;

import com.PatronageSystem.entity.FactorCategory;
import com.PatronageSystem.mapper.FactorCategoryMapper;
import com.PatronageSystem.service.FactorCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 因子分类Service实现类
 */
@Service
public class FactorCategoryServiceImpl implements FactorCategoryService {
    
    @Autowired
    private FactorCategoryMapper factorCategoryMapper;
    
    @Override
    public List<FactorCategory> getAllCategories() {
        return factorCategoryMapper.selectAll();
    }
    
    @Override
    public FactorCategory getCategoryById(Long id) {
        return factorCategoryMapper.selectById(id);
    }
    
    @Override
    public List<FactorCategory> getCategoriesByParentId(Long parentId) {
        return factorCategoryMapper.selectByParentId(parentId);
    }
    
    @Override
    public boolean addCategory(FactorCategory category) {
        // 设置默认值
        if (category.getStatus() == null) {
            category.setStatus(1);
        }
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }
        if (category.getParentId() == null) {
            category.setParentId(0L);
        }
        
        return factorCategoryMapper.insert(category) > 0;
    }
    
    @Override
    public boolean updateCategory(FactorCategory category) {
        return factorCategoryMapper.update(category) > 0;
    }
    
    @Override
    public boolean deleteCategory(Long id) {
        // 检查是否有子分类
        List<FactorCategory> children = factorCategoryMapper.selectByParentId(id);
        if (!children.isEmpty()) {
            throw new RuntimeException("该分类下还有子分类，无法删除");
        }
        
        return factorCategoryMapper.deleteById(id) > 0;
    }
    
    @Override
    public List<FactorCategory> getCategoryTree() {
        List<FactorCategory> allCategories = factorCategoryMapper.selectAll();
        
        // 构建父子关系
        Map<Long, List<FactorCategory>> parentChildMap = allCategories.stream()
                .collect(Collectors.groupingBy(category -> 
                    category.getParentId() != null ? category.getParentId() : 0L));
        
        // 获取顶级分类
        return parentChildMap.getOrDefault(0L, new ArrayList<>());
    }
} 