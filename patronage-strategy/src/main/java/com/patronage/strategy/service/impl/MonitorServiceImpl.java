package com.patronage.strategy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.patronage.strategy.entity.AlertHistory;
import com.patronage.strategy.entity.StrategyAlert;
import com.patronage.strategy.mapper.AlertHistoryMapper;
import com.patronage.strategy.mapper.StrategyAlertMapper;
import com.patronage.strategy.service.MonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监控服务实现类
 */
@Slf4j
@Service
public class MonitorServiceImpl implements MonitorService {

    @Autowired
    private StrategyAlertMapper strategyAlertMapper;

    @Autowired
    private AlertHistoryMapper alertHistoryMapper;

    @Override
    public boolean createAlert(StrategyAlert alert) {
        alert.setCreateTime(LocalDateTime.now());
        return strategyAlertMapper.insert(alert) > 0;
    }

    @Override
    public boolean updateAlert(StrategyAlert alert) {
        alert.setUpdateTime(LocalDateTime.now());
        return strategyAlertMapper.updateById(alert) > 0;
    }

    @Override
    public boolean deleteAlert(Long alertId) {
        return strategyAlertMapper.deleteById(alertId) > 0;
    }

    @Override
    public List<StrategyAlert> getStrategyAlerts(Long strategyId) {
        return strategyAlertMapper.selectList(
            new LambdaQueryWrapper<StrategyAlert>()
                .eq(StrategyAlert::getStrategyId, strategyId)
                .orderByDesc(StrategyAlert::getCreateTime)
        );
    }

    @Override
    public boolean toggleAlert(Long alertId, boolean enabled) {
        StrategyAlert alert = strategyAlertMapper.selectById(alertId);
        if (alert != null) {
            alert.setIsEnabled(enabled ? 1 : 0);
            alert.setUpdateTime(LocalDateTime.now());
            return strategyAlertMapper.updateById(alert) > 0;
        }
        return false;
    }

    @Override
    public List<AlertHistory> getAlertHistory(Long strategyId, Integer alertType, Integer processStatus) {
        LambdaQueryWrapper<AlertHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AlertHistory::getStrategyId, strategyId);
        
        if (alertType != null) {
            wrapper.eq(AlertHistory::getAlertType, alertType);
        }
        if (processStatus != null) {
            wrapper.eq(AlertHistory::getProcessStatus, processStatus);
        }
        
        wrapper.orderByDesc(AlertHistory::getCreateTime);
        return alertHistoryMapper.selectList(wrapper);
    }

    @Override
    public boolean processAlert(Long alertId, String processRemark) {
        AlertHistory alert = alertHistoryMapper.selectById(alertId);
        if (alert != null) {
            alert.setProcessStatus(1);
            alert.setProcessTime(LocalDateTime.now());
            alert.setProcessRemark(processRemark);
            return alertHistoryMapper.updateById(alert) > 0;
        }
        return false;
    }

    @Override
    public Map<String, Object> getStrategyMonitorData(Long strategyId) {
        Map<String, Object> monitorData = new HashMap<>();
        
        // 获取策略基本信息
        // 这里需要注入StrategyService来获取策略信息
        // Strategy strategy = strategyService.getById(strategyId);
        
        // 获取预警规则数量
        long alertCount = strategyAlertMapper.selectCount(
            new LambdaQueryWrapper<StrategyAlert>()
                .eq(StrategyAlert::getStrategyId, strategyId)
                .eq(StrategyAlert::getIsEnabled, 1)
        );
        
        // 获取未处理预警数量
        long unprocessedAlertCount = alertHistoryMapper.selectCount(
            new LambdaQueryWrapper<AlertHistory>()
                .eq(AlertHistory::getStrategyId, strategyId)
                .eq(AlertHistory::getProcessStatus, 0)
        );
        
        monitorData.put("alertCount", alertCount);
        monitorData.put("unprocessedAlertCount", unprocessedAlertCount);
        
        return monitorData;
    }

    @Override
    public void checkAlertConditions(Long strategyId) {
        log.info("检查策略预警条件，策略ID: {}", strategyId);
        
        // 获取策略的所有启用预警规则
        List<StrategyAlert> alerts = strategyAlertMapper.selectList(
            new LambdaQueryWrapper<StrategyAlert>()
                .eq(StrategyAlert::getStrategyId, strategyId)
                .eq(StrategyAlert::getIsEnabled, 1)
        );
        
        for (StrategyAlert alert : alerts) {
            try {
                // 检查预警条件
                boolean triggered = checkSingleAlertCondition(alert);
                
                if (triggered) {
                    // 创建预警历史记录
                    createAlertHistory(alert);
                    
                    // 发送通知
                    sendNotification(alert);
                }
            } catch (Exception e) {
                log.error("检查预警条件失败，预警ID: {}", alert.getId(), e);
            }
        }
    }

    /**
     * 检查单个预警条件
     */
    private boolean checkSingleAlertCondition(StrategyAlert alert) {
        // 这里需要根据预警类型获取相应的策略数据
        // 暂时返回false，实际实现需要根据具体业务逻辑
        return false;
    }

    /**
     * 创建预警历史记录
     */
    private void createAlertHistory(StrategyAlert alert) {
        AlertHistory history = new AlertHistory();
        history.setStrategyId(alert.getStrategyId());
        history.setAlertId(alert.getId());
        history.setAlertType(alert.getAlertType());
        history.setAlertMessage("策略触发预警条件: " + alert.getAlertName());
        history.setThreshold(alert.getThreshold());
        history.setNotificationStatus(0);
        history.setProcessStatus(0);
        history.setCreateTime(LocalDateTime.now());
        
        alertHistoryMapper.insert(history);
    }

    /**
     * 发送通知
     */
    private void sendNotification(StrategyAlert alert) {
        // 根据通知方式发送通知
        // 这里可以实现邮件、短信、系统消息等通知方式
        log.info("发送预警通知，预警ID: {}, 通知方式: {}", alert.getId(), alert.getNotificationType());
    }
} 