package com.patronage.strategy.service;

import src.java.com.patronage.strategy.entity.Strategy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StrategyServiceTest {

    @Autowired
    private StrategyService strategyService;

    @Test
    void testCreateStrategy() {
        Strategy strategy = new Strategy();
        strategy.setStrategyName("测试策略");
        strategy.setDescription("这是一个测试策略");
        strategy.setStrategyType(1);
        strategy.setInitialCapital(new BigDecimal("100000"));
        strategy.setCreateBy("test");

        boolean result = strategyService.createStrategy(strategy);
        assertTrue(result);
        assertNotNull(strategy.getId());
    }

    @Test
    void testGetStrategyDetail() {
        // 先创建一个策略
        Strategy strategy = new Strategy();
        strategy.setStrategyName("测试策略");
        strategy.setDescription("这是一个测试策略");
        strategy.setStrategyType(1);
        strategy.setInitialCapital(new BigDecimal("100000"));
        strategy.setCreateBy("test");
        strategyService.createStrategy(strategy);

        // 查询策略详情
        Strategy found = strategyService.getStrategyDetail(strategy.getId());
        assertNotNull(found);
        assertEquals("测试策略", found.getStrategyName());
    }

    @Test
    void testUpdateStrategy() {
        // 先创建一个策略
        Strategy strategy = new Strategy();
        strategy.setStrategyName("测试策略");
        strategy.setDescription("这是一个测试策略");
        strategy.setStrategyType(1);
        strategy.setInitialCapital(new BigDecimal("100000"));
        strategy.setCreateBy("test");
        strategyService.createStrategy(strategy);

        // 更新策略
        strategy.setDescription("更新后的描述");
        boolean result = strategyService.updateStrategy(strategy);
        assertTrue(result);

        // 验证更新结果
        Strategy updated = strategyService.getStrategyDetail(strategy.getId());
        assertEquals("更新后的描述", updated.getDescription());
    }

    @Test
    void testDeleteStrategy() {
        // 先创建一个策略
        Strategy strategy = new Strategy();
        strategy.setStrategyName("测试策略");
        strategy.setDescription("这是一个测试策略");
        strategy.setStrategyType(1);
        strategy.setInitialCapital(new BigDecimal("100000"));
        strategy.setCreateBy("test");
        strategyService.createStrategy(strategy);

        // 删除策略
        boolean result = strategyService.deleteStrategy(strategy.getId());
        assertTrue(result);

        // 验证删除结果
        Strategy deleted = strategyService.getStrategyDetail(strategy.getId());
        assertNull(deleted);
    }

    @Test
    void testStartStrategy() {
        // 先创建一个策略
        Strategy strategy = new Strategy();
        strategy.setStrategyName("测试策略");
        strategy.setDescription("这是一个测试策略");
        strategy.setStrategyType(1);
        strategy.setInitialCapital(new BigDecimal("100000"));
        strategy.setCreateBy("test");
        strategyService.createStrategy(strategy);

        // 启动策略
        boolean result = strategyService.startStrategy(strategy.getId());
        assertTrue(result);

        // 验证启动结果
        Strategy started = strategyService.getStrategyDetail(strategy.getId());
        assertEquals(1, started.getStatus());
    }

    @Test
    void testStopStrategy() {
        // 先创建一个策略并启动
        Strategy strategy = new Strategy();
        strategy.setStrategyName("测试策略");
        strategy.setDescription("这是一个测试策略");
        strategy.setStrategyType(1);
        strategy.setInitialCapital(new BigDecimal("100000"));
        strategy.setCreateBy("test");
        strategyService.createStrategy(strategy);
        strategyService.startStrategy(strategy.getId());

        // 停止策略
        boolean result = strategyService.stopStrategy(strategy.getId());
        assertTrue(result);

        // 验证停止结果
        Strategy stopped = strategyService.getStrategyDetail(strategy.getId());
        assertEquals(3, stopped.getStatus());
    }
} 