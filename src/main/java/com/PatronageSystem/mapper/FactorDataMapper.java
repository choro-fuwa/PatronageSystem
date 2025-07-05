package com.PatronageSystem.mapper;

import com.PatronageSystem.entity.FactorData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 因子数据Mapper接口
 */
@Mapper
public interface FactorDataMapper {
    
    /**
     * 根据因子ID查询数据
     */
    List<FactorData> selectByFactorId(@Param("factorId") Long factorId);
    
    /**
     * 根据因子ID和日期范围查询数据
     */
    List<FactorData> selectByFactorIdAndDateRange(@Param("factorId") Long factorId,
                                                  @Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);
    
    /**
     * 根据基金代码查询数据
     */
    List<FactorData> selectByFundCode(@Param("fundCode") String fundCode);
    
    /**
     * 根据交易日期查询数据
     */
    List<FactorData> selectByTradeDate(@Param("tradeDate") LocalDate tradeDate);
    
    /**
     * 根据因子ID和基金代码查询数据
     */
    List<FactorData> selectByFactorIdAndFundCode(@Param("factorId") Long factorId,
                                                 @Param("fundCode") String fundCode);
    
    /**
     * 查询最新的因子数据
     */
    List<FactorData> selectLatestByFactorId(@Param("factorId") Long factorId);
    
    /**
     * 批量插入因子数据
     */
    int batchInsert(@Param("dataList") List<FactorData> dataList);
    
    /**
     * 插入单条因子数据
     */
    int insert(FactorData factorData);
    
    /**
     * 更新因子数据
     */
    int update(FactorData factorData);
    
    /**
     * 删除因子数据
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据因子ID删除数据
     */
    int deleteByFactorId(@Param("factorId") Long factorId);
    
    /**
     * 获取因子数据统计信息
     */
    List<FactorData> selectFactorDataStats(@Param("factorId") Long factorId);
} 