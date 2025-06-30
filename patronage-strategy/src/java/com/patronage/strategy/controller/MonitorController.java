package src.java.com.patronage.strategy.controller;

import src.java.com.patronage.strategy.entity.AlertHistory;
import src.java.com.patronage.strategy.entity.StrategyAlert;
import src.java.com.patronage.strategy.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监控控制器
 */
@RestController
@RequestMapping("/api/monitor")
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    /**
     * 创建预警规则
     */
    @PostMapping("/alert")
    public Map<String, Object> createAlert(@RequestBody StrategyAlert alert) {
        boolean success = monitorService.createAlert(alert);
        
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "创建成功");
        } else {
            response.put("code", 500);
            response.put("message", "创建失败");
        }
        return response;
    }

    /**
     * 更新预警规则
     */
    @PutMapping("/alert/{id}")
    public Map<String, Object> updateAlert(@PathVariable Long id, @RequestBody StrategyAlert alert) {
        alert.setId(id);
        boolean success = monitorService.updateAlert(alert);
        
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "更新成功");
        } else {
            response.put("code", 500);
            response.put("message", "更新失败");
        }
        return response;
    }

    /**
     * 删除预警规则
     */
    @DeleteMapping("/alert/{id}")
    public Map<String, Object> deleteAlert(@PathVariable Long id) {
        boolean success = monitorService.deleteAlert(id);
        
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "删除成功");
        } else {
            response.put("code", 500);
            response.put("message", "删除失败");
        }
        return response;
    }

    /**
     * 获取策略的预警规则列表
     */
    @GetMapping("/alert/{strategyId}")
    public Map<String, Object> getStrategyAlerts(@PathVariable Long strategyId) {
        List<StrategyAlert> alerts = monitorService.getStrategyAlerts(strategyId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", alerts);
        return response;
    }

    /**
     * 启用/禁用预警规则
     */
    @PostMapping("/alert/{id}/toggle")
    public Map<String, Object> toggleAlert(@PathVariable Long id, @RequestParam boolean enabled) {
        boolean success = monitorService.toggleAlert(id, enabled);
        
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", enabled ? "启用成功" : "禁用成功");
        } else {
            response.put("code", 500);
            response.put("message", "操作失败");
        }
        return response;
    }

    /**
     * 获取预警历史
     */
    @GetMapping("/history/{strategyId}")
    public Map<String, Object> getAlertHistory(
            @PathVariable Long strategyId,
            @RequestParam(required = false) Integer alertType,
            @RequestParam(required = false) Integer processStatus) {
        
        List<AlertHistory> history = monitorService.getAlertHistory(strategyId, alertType, processStatus);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", history);
        return response;
    }

    /**
     * 处理预警
     */
    @PostMapping("/history/{id}/process")
    public Map<String, Object> processAlert(@PathVariable Long id, @RequestParam String processRemark) {
        boolean success = monitorService.processAlert(id, processRemark);
        
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("code", 200);
            response.put("message", "处理成功");
        } else {
            response.put("code", 500);
            response.put("message", "处理失败");
        }
        return response;
    }

    /**
     * 获取策略监控数据
     */
    @GetMapping("/data/{strategyId}")
    public Map<String, Object> getStrategyMonitorData(@PathVariable Long strategyId) {
        Map<String, Object> monitorData = monitorService.getStrategyMonitorData(strategyId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", monitorData);
        return response;
    }

    /**
     * 手动检查预警条件
     */
    @PostMapping("/check/{strategyId}")
    public Map<String, Object> checkAlertConditions(@PathVariable Long strategyId) {
        try {
            monitorService.checkAlertConditions(strategyId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "检查完成");
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "检查失败: " + e.getMessage());
            return response;
        }
    }

    /**
     * 获取监控概览
     */
    @GetMapping("/overview")
    public Map<String, Object> getMonitorOverview() {
        Map<String, Object> overview = monitorService.getMonitorOverview();
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", overview);
        return response;
    }

    /**
     * 获取预警列表
     */
    @GetMapping("/alerts")
    public Map<String, Object> getAlertList(@RequestParam(required = false) Integer pageNum,
                                           @RequestParam(required = false) Integer pageSize,
                                           @RequestParam(required = false) Integer status) {
        Map<String, Object> alerts = monitorService.getAlertList(pageNum, pageSize, status);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", alerts);
        return response;
    }

    /**
     * 获取预警历史列表
     */
    @GetMapping("/history")
    public Map<String, Object> getAlertHistoryList(@RequestParam(required = false) Integer pageNum,
                                                  @RequestParam(required = false) Integer pageSize,
                                                  @RequestParam(required = false) Integer processStatus) {
        Map<String, Object> history = monitorService.getAlertHistoryList(pageNum, pageSize, processStatus);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", history);
        return response;
    }

    /**
     * 获取性能分析
     */
    @GetMapping("/performance")
    public Map<String, Object> getPerformanceAnalysis(@RequestParam(required = false) String startDate,
                                                     @RequestParam(required = false) String endDate) {
        Map<String, Object> analysis = monitorService.getPerformanceAnalysis(startDate, endDate);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", analysis);
        return response;
    }

    /**
     * 获取收益归因
     */
    @GetMapping("/attribution/{strategyId}")
    public Map<String, Object> getReturnAttribution(@PathVariable Long strategyId,
                                                   @RequestParam(required = false) String startDate,
                                                   @RequestParam(required = false) String endDate) {
        Map<String, Object> attribution = monitorService.getReturnAttribution(strategyId, startDate, endDate);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", attribution);
        return response;
    }

    /**
     * 获取风险归因
     */
    @GetMapping("/risk-attribution/{strategyId}")
    public Map<String, Object> getRiskAttribution(@PathVariable Long strategyId) {
        Map<String, Object> attribution = monitorService.getRiskAttribution(strategyId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", attribution);
        return response;
    }

    /**
     * 获取监控仪表板数据
     */
    @GetMapping("/dashboard")
    public Map<String, Object> getMonitorDashboardData() {
        Map<String, Object> dashboardData = monitorService.getMonitorDashboardData();
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "查询成功");
        response.put("data", dashboardData);
        return response;
    }
} 