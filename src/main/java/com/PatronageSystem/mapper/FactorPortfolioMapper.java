package com.PatronageSystem.mapper;

import com.PatronageSystem.entity.FactorPortfolio;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 因子组合Mapper接口
 */
@Mapper
public interface FactorPortfolioMapper {
    
    /**
     * 查询所有组合
     */
    List<FactorPortfolio> selectAll();
    
    /**
     * 根据ID查询组合
     */
    FactorPortfolio selectById(@Param("id") Long id);
    
    /**
     * 根据组合代码查询
     */
    FactorPortfolio selectByPortfolioCode(@Param("portfolioCode") String portfolioCode);
    
    /**
     * 根据创建者查询组合
     */
    List<FactorPortfolio> selectByCreatorId(@Param("creatorId") Long creatorId);
    
    /**
     * 条件查询组合
     */
    List<FactorPortfolio> selectByCondition(@Param("strategyType") String strategyType,
                                            @Param("isPublic") Integer isPublic);
    
    /**
     * 新增组合
     */
    int insert(FactorPortfolio portfolio);
    
    /**
     * 更新组合
     */
    int update(FactorPortfolio portfolio);
    
    /**
     * 删除组合
     */
    int deleteById(@Param("id") Long id);
} 