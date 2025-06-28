package com.patronage.strategy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.patronage.strategy.entity.StrategyAlert;
import com.patronage.strategy.entity.AlertHistory;
import com.patronage.strategy.service.MonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 监控管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    /**
     * 创建预警规则
     */
    @PostMapping("/alert/create")
    public Map<String, Object> createAlert(@RequestBody StrategyAlert alert) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = monitorService.createAlert(alert);
            result.put("success", success);
            result.put("message", success ? "预警规则创建成功" : "创建失败");
        } catch (Exception e) {
            log.error("创建预警规则失败", e);
            result.put("success", false);
            result.put("message", "创建失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 更新预警规则
     */
    @PutMapping("/alert/update")
    public Map<String, Object> updateAlert(@RequestBody StrategyAlert alert) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = monitorService.updateAlert(alert);
            result.put("success", success);
            result.put("message", success ? "预警规则更新成功" : "更新失败");
        } catch (Exception e) {
            log.error("更新预警规则失败", e);
            result.put("success", false);
            result.put("message", "更新失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 删除预警规则
     */
    @DeleteMapping("/alert/delete/{alertId}")
    public Map<String, Object> deleteAlert(@PathVariable Long alertId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = monitorService.deleteAlert(alertId);
            result.put("success", success);
            result.put("message", success ? "预警规则删除成功" : "删除失败");
        } catch (Exception e) {
            log.error("删除预警规则失败", e);
            result.put("success", false);
            result.put("message", "删除失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 启用/禁用预警规则
     */
    @PostMapping("/alert/toggle/{alertId}")
    public Map<String, Object> toggleAlert(@PathVariable Long alertId, @RequestParam Boolean enabled) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = monitorService.toggleAlert(alertId, enabled);
            result.put("success", success);
            result.put("message", success ? "预警规则状态更新成功" : "更新失败");
        } catch (Exception e) {
            log.error("更新预警规则状态失败", e);
            result.put("success", false);
            result.put("message", "更新失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询预警规则
     */
    @GetMapping("/alert/list")
    public Map<String, Object> getAlertList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long strategyId,
            @RequestParam(required = false) Integer alertType) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            IPage<StrategyAlert> page = monitorService.getAlertPage(pageNum, pageSize, strategyId, alertType);
            result.put("success", true);
            result.put("data", page);
        } catch (Exception e) {
            log.error("查询预警规则失败", e);
            result.put("success", false);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询预警历史
     */
    @GetMapping("/alert/history")
    public Map<String, Object> getAlertHistoryList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long strategyId,
            @RequestParam(required = false) Integer alertType,
            @RequestParam(required = false) Integer processStatus) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            IPage<AlertHistory> page = monitorService.getAlertHistoryPage(pageNum, pageSize, strategyId, alertType, processStatus);
            result.put("success", true);
            result.put("data", page);
        } catch (Exception e) {
            log.error("查询预警历史失败", e);
            result.put("success", false);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 处理预警
     */
    @PostMapping("/alert/process/{alertHistoryId}")
    public Map<String, Object> processAlert(
            @PathVariable Long alertHistoryId,
            @RequestParam String processBy,
            @RequestParam(required = false) String processRemark) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = monitorService.processAlert(alertHistoryId, processBy, processRemark);
            result.put("success", success);
            result.put("message", success ? "预警处理成功" : "处理失败");
        } catch (Exception e) {
            log.error("处理预警失败", e);
            result.put("success", false);
            result.put("message", "处理失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取策略监控数据
     */
    @GetMapping("/strategy/{strategyId}")
    public Map<String, Object> getStrategyMonitorData(@PathVariable Long strategyId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> monitorData = monitorService.getStrategyMonitorData(strategyId);
            result.put("success", true);
            result.put("data", monitorData);
        } catch (Exception e) {
            log.error("获取策略监控数据失败", e);
            result.put("success", false);
            result.put("message", "获取监控数据失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取监控看板数据
     */
    @GetMapping("/dashboard")
    public Map<String, Object> getMonitorDashboardData() {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> dashboardData = monitorService.getMonitorDashboardData();
            result.put("success", true);
            result.put("data", dashboardData);
        } catch (Exception e) {
            log.error("获取监控看板数据失败", e);
            result.put("success", false);
            result.put("message", "获取看板数据失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 检查策略预警
     */
    @PostMapping("/check/{strategyId}")
    public Map<String, Object> checkStrategyAlerts(@PathVariable Long strategyId) {
        Map<String, Object> result = new HashMap<>();
        try {
            monitorService.checkStrategyAlerts(strategyId);
            result.put("success", true);
            result.put("message", "预警检查完成");
        } catch (Exception e) {
            log.error("检查策略预警失败", e);
            result.put("success", false);
            result.put("message", "检查失败: " + e.getMessage());
        }
        return result;
    }
} 