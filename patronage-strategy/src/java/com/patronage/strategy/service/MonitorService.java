package src.java.com.patronage.strategy.service;

import src.java.com.patronage.strategy.entity.AlertHistory;
import src.java.com.patronage.strategy.entity.StrategyAlert;

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

    /**
     * 获取监控概览
     */
    Map<String, Object> getMonitorOverview();

    /**
     * 获取预警列表
     */
    Map<String, Object> getAlertList(Integer pageNum, Integer pageSize, Integer status);

    /**
     * 获取预警历史列表
     */
    Map<String, Object> getAlertHistoryList(Integer pageNum, Integer pageSize, Integer processStatus);

    /**
     * 获取性能分析
     */
    Map<String, Object> getPerformanceAnalysis(String startDate, String endDate);

    /**
     * 获取收益归因
     */
    Map<String, Object> getReturnAttribution(Long strategyId, String startDate, String endDate);

    /**
     * 获取风险归因
     */
    Map<String, Object> getRiskAttribution(Long strategyId);

    /**
     * 获取监控仪表板数据
     */
    Map<String, Object> getMonitorDashboardData();
} 