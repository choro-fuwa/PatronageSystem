package com.patronage.strategy.service;

import com.patronage.strategy.entity.AlertHistory;
import com.patronage.strategy.entity.StrategyAlert;

import java.util.List;
import java.util.Map;

/**
 * 监控服务接口
 */
public interface MonitorService {

    /**
     * 创建预警规则
     */
    boolean createAlert(StrategyAlert alert);

    /**
     * 更新预警规则
     */
    boolean updateAlert(StrategyAlert alert);

    /**
     * 删除预警规则
     */
    boolean deleteAlert(Long alertId);

    /**
     * 获取策略的预警规则列表
     */
    List<StrategyAlert> getStrategyAlerts(Long strategyId);

    /**
     * 启用/禁用预警规则
     */
    boolean toggleAlert(Long alertId, boolean enabled);

    /**
     * 获取预警历史
     */
    List<AlertHistory> getAlertHistory(Long strategyId, Integer alertType, Integer processStatus);

    /**
     * 处理预警
     */
    boolean processAlert(Long alertId, String processRemark);

    /**
     * 获取策略监控数据
     */
    Map<String, Object> getStrategyMonitorData(Long strategyId);

    /**
     * 检查预警条件
     */
    void checkAlertConditions(Long strategyId);
} 