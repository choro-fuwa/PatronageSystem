package com.PatronageSystem.mapper;

import com.PatronageSystem.entity.FactorPortfolioDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 因子组合明细Mapper接口
 */
@Mapper
public interface FactorPortfolioDetailMapper {
    
    /**
     * 根据组合ID查询明细
     */
    List<FactorPortfolioDetail> selectByPortfolioId(@Param("portfolioId") Long portfolioId);
    
    /**
     * 根据因子ID查询明细
     */
    List<FactorPortfolioDetail> selectByFactorId(@Param("factorId") Long factorId);
    
    /**
     * 根据组合ID和因子ID查询明细
     */
    FactorPortfolioDetail selectByPortfolioIdAndFactorId(@Param("portfolioId") Long portfolioId,
                                                         @Param("factorId") Long factorId);
    
    /**
     * 插入明细
     */
    int insert(FactorPortfolioDetail detail);
    
    /**
     * 批量插入明细
     */
    int batchInsert(@Param("detailList") List<FactorPortfolioDetail> detailList);
    
    /**
     * 更新明细
     */
    int update(FactorPortfolioDetail detail);
    
    /**
     * 删除明细
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据组合ID删除明细
     */
    int deleteByPortfolioId(@Param("portfolioId") Long portfolioId);
    
    /**
     * 根据组合ID和因子ID删除明细
     */
    int deleteByPortfolioIdAndFactorId(@Param("portfolioId") Long portfolioId,
                                       @Param("factorId") Long factorId);
} 