package com.PatronageSystem.mapper;

import com.PatronageSystem.entity.StrategyPosition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 策略持仓Mapper接口
 */
@Mapper
public interface StrategyPositionMapper {
    
    /**
     * 根据策略ID查询持仓
     */
    List<StrategyPosition> selectByStrategyId(@Param("strategyId") Long strategyId);
    
    /**
     * 根据策略ID和日期查询持仓
     */
    List<StrategyPosition> selectByStrategyIdAndDate(@Param("strategyId") Long strategyId,
                                                     @Param("positionDate") LocalDate positionDate);
    
    /**
     * 根据基金代码查询持仓
     */
    List<StrategyPosition> selectByFundCode(@Param("fundCode") String fundCode);
    
    /**
     * 查询最新的持仓
     */
    List<StrategyPosition> selectLatestByStrategyId(@Param("strategyId") Long strategyId);
    
    /**
     * 新增持仓
     */
    int insert(StrategyPosition position);
    
    /**
     * 批量新增持仓
     */
    int batchInsert(@Param("positions") List<StrategyPosition> positions);
    
    /**
     * 更新持仓
     */
    int update(StrategyPosition position);
    
    /**
     * 删除持仓
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据策略ID删除持仓
     */
    int deleteByStrategyId(@Param("strategyId") Long strategyId);
    
    /**
     * 获取持仓统计信息
     */
    Map<String, Object> selectPositionStats(@Param("strategyId") Long strategyId);
    
    /**
     * 按策略统计持仓
     */
    List<Map<String, Object>> selectPositionStatsByStrategy();
} 