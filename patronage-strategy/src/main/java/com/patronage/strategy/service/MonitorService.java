package com.patronage.strategy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.patronage.strategy.entity.StrategyAlert;
import com.patronage.strategy.entity.AlertHistory;

import java.util.Map;

/**
 * 监控服务接口
 */
public interface MonitorService extends IService<StrategyAlert> {

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
     * 启用/禁用预警规则
     */
    boolean toggleAlert(Long alertId, boolean enabled);

    /**
     * 分页查询预警规则
     */
    IPage<StrategyAlert> getAlertPage(Integer pageNum, Integer pageSize, Long strategyId, Integer alertType);

    /**
     * 分页查询预警历史
     */
    IPage<AlertHistory> getAlertHistoryPage(Integer pageNum, Integer pageSize, Long strategyId, Integer alertType, Integer processStatus);

    /**
     * 处理预警
     */
    boolean processAlert(Long alertHistoryId, String processBy, String processRemark);

    /**
     * 获取策略监控数据
     */
    Map<String, Object> getStrategyMonitorData(Long strategyId);

    /**
     * 获取监控看板数据
     */
    Map<String, Object> getMonitorDashboardData();

    /**
     * 检查策略预警
     */
    void checkStrategyAlerts(Long strategyId);
} 