package com.PatronageSystem.mapper;

import com.PatronageSystem.entity.FactorAnalysis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 因子有效性分析Mapper接口
 */
@Mapper
public interface FactorAnalysisMapper {
    
    /**
     * 根据因子ID查询分析数据
     */
    List<FactorAnalysis> selectByFactorId(@Param("factorId") Long factorId);
    
    /**
     * 根据因子ID和日期范围查询分析数据
     */
    List<FactorAnalysis> selectByFactorIdAndDateRange(@Param("factorId") Long factorId,
                                                      @Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate);
    
    /**
     * 根据分析日期查询数据
     */
    List<FactorAnalysis> selectByAnalysisDate(@Param("analysisDate") LocalDate analysisDate);
    
    /**
     * 查询最新的分析数据
     */
    FactorAnalysis selectLatestByFactorId(@Param("factorId") Long factorId);
    
    /**
     * 插入分析数据
     */
    int insert(FactorAnalysis analysis);
    
    /**
     * 批量插入分析数据
     */
    int batchInsert(@Param("analysisList") List<FactorAnalysis> analysisList);
    
    /**
     * 更新分析数据
     */
    int update(FactorAnalysis analysis);
    
    /**
     * 删除分析数据
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据因子ID删除分析数据
     */
    int deleteByFactorId(@Param("factorId") Long factorId);
    
    /**
     * 获取因子分析统计信息
     */
    List<FactorAnalysis> selectFactorAnalysisStats(@Param("factorId") Long factorId);
} 