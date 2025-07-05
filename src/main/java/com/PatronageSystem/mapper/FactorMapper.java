package com.PatronageSystem.mapper;

import com.PatronageSystem.entity.Factor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 因子Mapper接口
 */
@Mapper
public interface FactorMapper {
    
    /**
     * 查询所有因子
     */
    List<Factor> selectAll();
    
    /**
     * 根据ID查询因子
     */
    Factor selectById(@Param("id") Long id);
    
    /**
     * 根据分类ID查询因子
     */
    List<Factor> selectByCategoryId(@Param("categoryId") Long categoryId);
    
    /**
     * 根据因子代码查询
     */
    Factor selectByFactorCode(@Param("factorCode") String factorCode);
    
    /**
     * 条件查询因子
     */
    List<Factor> selectByCondition(@Param("factorType") String factorType, 
                                   @Param("riskLevel") String riskLevel,
                                   @Param("isPublic") Integer isPublic);
    
    /**
     * 新增因子
     */
    int insert(Factor factor);
    
    /**
     * 更新因子
     */
    int update(Factor factor);
    
    /**
     * 删除因子
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据创建者查询因子
     */
    List<Factor> selectByCreatorId(@Param("creatorId") Long creatorId);
} 