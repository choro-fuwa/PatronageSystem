package src.java.com.patronage.strategy.service.impl;

import src.java.com.patronage.strategy.service.MonitorService;
import src.java.com.patronage.strategy.service.ScheduleService;
import src.java.com.patronage.strategy.service.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import src.java.com.patronage.strategy.entity.Strategy;

import java.util.List;

/**
 * 定时任务服务实现类
 */
@Slf4j
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private MonitorService monitorService;

    @Autowired
    private StrategyService strategyService;

    /**
     * 每5分钟检查一次策略预警条件
     */
    @Scheduled(fixedRate = 300000)
    @Override
    public void checkStrategyAlerts() {
        log.info("开始执行定时预警检查任务");
        
        try {
            // 获取所有运行中的策略
            List<Strategy> runningStrategies = strategyService.getRunningStrategies();
            
            for (Strategy strategy : runningStrategies) {
                try {
                    monitorService.checkAlertConditions(strategy.getId());
                } catch (Exception e) {
                    log.error("检查策略预警条件失败，策略ID: {}", strategy.getId(), e);
                }
            }
            
            log.info("定时预警检查任务执行完成，检查策略数量: {}", runningStrategies.size());
        } catch (Exception e) {
            log.error("定时预警检查任务执行失败", e);
        }
    }

    /**
     * 每小时更新一次策略数据
     */
    @Scheduled(fixedRate = 3600000)
    @Override
    public void updateStrategyData() {
        log.info("开始执行定时策略数据更新任务");
        
        try {
            // 获取所有运行中的策略
            List<Strategy> runningStrategies = strategyService.getRunningStrategies();
            
            for (Strategy strategy : runningStrategies) {
                try {
                    // 这里可以添加更新策略数据的逻辑
                    // 比如更新当前资金、收益率等
                    log.info("更新策略数据，策略ID: {}", strategy.getId());
                } catch (Exception e) {
                    log.error("更新策略数据失败，策略ID: {}", strategy.getId(), e);
                }
            }
            
            log.info("定时策略数据更新任务执行完成，更新策略数量: {}", runningStrategies.size());
        } catch (Exception e) {
            log.error("定时策略数据更新任务执行失败", e);
        }
    }

    /**
     * 每天凌晨2点清理过期数据
     */
    @Scheduled(cron = "0 0 2 * * ?")
    @Override
    public void cleanExpiredData() {
        log.info("开始执行定时数据清理任务");
        
        try {
            // 清理30天前的预警历史
            // 这里可以添加清理逻辑
            
            // 清理90天前的回测结果
            // 这里可以添加清理逻辑
            
            log.info("定时数据清理任务执行完成");
        } catch (Exception e) {
            log.error("定时数据清理任务执行失败", e);
        }
    }
} 