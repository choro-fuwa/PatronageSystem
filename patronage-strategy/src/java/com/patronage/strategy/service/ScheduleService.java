package src.java.com.patronage.strategy.service;

/**
 * 定时任务服务接口
 */
public interface ScheduleService {

    /**
     * 定时检查策略预警条件
     */
    void checkStrategyAlerts();

    /**
     * 定时更新策略数据
     */
    void updateStrategyData();

    /**
     * 定时清理过期数据
     */
    void cleanExpiredData();
} 