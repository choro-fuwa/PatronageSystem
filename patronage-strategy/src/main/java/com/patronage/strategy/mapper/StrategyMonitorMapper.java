package com.patronage.strategy.mapper;

import com.patronage.strategy.entity.StrategyMonitor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 策略监控Mapper接口
 */
@Mapper
public interface StrategyMonitorMapper {

    /**
     * 根据策略ID查询监控记录
     */
    List<StrategyMonitor> selectByStrategyId(@Param("strategyId") Long strategyId);

    /**
     * 根据策略ID和日期范围查询监控记录
     */
    List<StrategyMonitor> selectByStrategyIdAndDateRange(@Param("strategyId") Long strategyId,
                                                         @Param("startDate") LocalDate startDate,
                                                         @Param("endDate") LocalDate endDate);

    /**
     * 查询最新的监控记录
     */
    StrategyMonitor selectLatestByStrategyId(@Param("strategyId") Long strategyId);

    /**
     * 根据监控日期查询
     */
    List<StrategyMonitor> selectByMonitorDate(@Param("monitorDate") LocalDate monitorDate);

    /**
     * 根据状态查询监控记录
     */
    List<StrategyMonitor> selectByStatus(@Param("status") String status);

    /**
     * 查询有风险预警的监控记录
     */
    List<StrategyMonitor> selectRiskAlerts();

    /**
     * 新增监控记录
     */
    int insert(StrategyMonitor monitor);

    /**
     * 更新监控记录
     */
    int update(StrategyMonitor monitor);

    /**
     * 删除监控记录
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据策略ID删除监控记录
     */
    int deleteByStrategyId(@Param("strategyId") Long strategyId);

    /**
     * 获取监控统计信息
     */
    Map<String, Object> selectMonitorStats(@Param("strategyId") Long strategyId);

    /**
     * 按策略统计监控记录
     */
    List<Map<String, Object>> selectMonitorStatsByStrategy();
}
