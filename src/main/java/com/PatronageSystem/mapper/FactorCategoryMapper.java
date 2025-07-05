package com.PatronageSystem.mapper;

import com.PatronageSystem.entity.FactorCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 因子分类Mapper接口
 */
@Mapper
public interface FactorCategoryMapper {
    
    /**
     * 查询所有分类
     */
    List<FactorCategory> selectAll();
    
    /**
     * 根据ID查询分类
     */
    FactorCategory selectById(@Param("id") Long id);
    
    /**
     * 根据父ID查询子分类
     */
    List<FactorCategory> selectByParentId(@Param("parentId") Long parentId);
    
    /**
     * 新增分类
     */
    int insert(FactorCategory category);
    
    /**
     * 更新分类
     */
    int update(FactorCategory category);
    
    /**
     * 删除分类
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据状态查询分类
     */
    List<FactorCategory> selectByStatus(@Param("status") Integer status);
} 