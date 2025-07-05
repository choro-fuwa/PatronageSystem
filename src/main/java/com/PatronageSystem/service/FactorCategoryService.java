package com.PatronageSystem.service;

import com.PatronageSystem.entity.FactorCategory;
import java.util.List;

/**
 * 因子分类Service接口
 */
public interface FactorCategoryService {
    
    /**
     * 查询所有分类
     */
    List<FactorCategory> getAllCategories();
    
    /**
     * 根据ID查询分类
     */
    FactorCategory getCategoryById(Long id);
    
    /**
     * 根据父ID查询子分类
     */
    List<FactorCategory> getCategoriesByParentId(Long parentId);
    
    /**
     * 新增分类
     */
    boolean addCategory(FactorCategory category);
    
    /**
     * 更新分类
     */
    boolean updateCategory(FactorCategory category);
    
    /**
     * 删除分类
     */
    boolean deleteCategory(Long id);
    
    /**
     * 获取分类树结构
     */
    List<FactorCategory> getCategoryTree();
} 