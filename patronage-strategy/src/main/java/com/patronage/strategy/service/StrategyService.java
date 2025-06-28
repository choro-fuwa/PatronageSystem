package com.patronage.strategy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.patronage.strategy.entity.Strategy;

import java.util.Map;

/**
 * 策略服务接口
 */
public interface StrategyService extends IService<Strategy> {

    /**
     * 分页查询策略列表
     */
    IPage<Strategy> getStrategyPage(Integer pageNum, Integer pageSize, String strategyName, Integer strategyType, Integer status);

    /**
     * 创建策略
     */
    boolean createStrategy(Strategy strategy);

    /**
     * 更新策略
     */
    boolean updateStrategy(Strategy strategy);

    /**
     * 删除策略
     */
    boolean deleteStrategy(Long id);

    /**
     * 启动策略
     */
    boolean startStrategy(Long id);

    /**
     * 停止策略
     */
    boolean stopStrategy(Long id);

    /**
     * 暂停策略
     */
    boolean pauseStrategy(Long id);

    /**
     * 获取策略详情
     */
    Strategy getStrategyDetail(Long id);

    /**
     * 获取策略统计信息
     */
    Map<String, Object> getStrategyStatistics();
} 