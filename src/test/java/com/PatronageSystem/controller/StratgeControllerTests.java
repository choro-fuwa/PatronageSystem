package com.PatronageSystem.controller;

import com.PatronageSystem.controller.StrategyController;
import com.PatronageSystem.entity.Strategy;
import com.PatronageSystem.entity.StrategyBacktest;
import com.PatronageSystem.entity.StrategyMonitor;
import com.PatronageSystem.service.StrategyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StratgeControllerTests {

    @Mock
    private StrategyService strategyService;

    @Mock
    private com.PatronageSystem.mapper.StrategyBacktestMapper strategyBacktestMapper;

    @Mock
    private com.PatronageSystem.mapper.StrategyMonitorMapper strategyMonitorMapper;

    @InjectMocks
    private StrategyController strategyController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetStrategies_Success() {
        List<Strategy> strategies = new ArrayList<>();
        Strategy strategy = new Strategy();
        strategy.setId(1L);
        strategies.add(strategy);

        when(strategyService.getAllStrategies()).thenReturn(strategies);

        Map<String, Object> response = strategyController.getStrategies();

        assertTrue((Boolean) response.get("success"));
        assertEquals("获取策略列表成功", response.get("message"));
        assertEquals(strategies, response.get("data"));
    }

    @Test
    public void testGetStrategyDetail_Found() {

        Strategy strategy = new Strategy();
        strategy.setId(1L);

        when(strategyService.getStrategyById(1L)).thenReturn(strategy);

        Map<String, Object> response = strategyController.getStrategyDetail(1L);

        assertTrue((Boolean) response.get("success"));
        assertEquals(strategy, response.get("data"));
    }

    @Test
    public void testGetStrategyDetail_NotFound() {
        when(strategyService.getStrategyById(1L)).thenReturn(null);

        Map<String, Object> response = strategyController.getStrategyDetail(1L);

        assertFalse((Boolean) response.get("success"));
        assertEquals("策略不存在", response.get("message"));
    }

    @Test
    public void testSearchStrategiesByCondition() {
        List<Strategy> strategies = new ArrayList<>();
        Strategy strategy = new Strategy();
        strategy.setStrategyType("多因子");
        strategies.add(strategy);

        when(strategyService.getStrategiesByCondition("多因子", "中风险", 1))
                .thenReturn(strategies);

        Map<String, Object> response = strategyController.searchStrategies("多因子", "中风险", 1);

        assertTrue((Boolean) response.get("success"));
        assertEquals(strategies, response.get("data"));
    }

    @Test
    public void testAddStrategy_Success() {
        Strategy strategy = new Strategy();
        strategy.setStrategyCode("TEST001");

        when(strategyService.addStrategy(strategy)).thenReturn(true);

        Map<String, Object> response = strategyController.addStrategy(strategy);

        assertTrue((Boolean) response.get("success"));
        assertEquals("新增策略成功", response.get("message"));
    }

    @Test
    public void testCheckStrategyCode_Exists() {
        when(strategyService.isStrategyCodeExists("EXISTING_CODE")).thenReturn(true);

        Map<String, Object> response = strategyController.checkStrategyCode("EXISTING_CODE");

        assertTrue((Boolean) response.get("success"));
        assertTrue((Boolean) response.get("exists"));
    }

    @Test
    public void testGetStrategyBacktests() {
        List<StrategyBacktest> backtests = new ArrayList<>();
        StrategyBacktest backtest = new StrategyBacktest();
        backtest.setStrategyId(1L);
        backtests.add(backtest);

        when(strategyBacktestMapper.selectByStrategyId(1L)).thenReturn(backtests);

        List<StrategyBacktest> result = strategyController.getStrategyBacktests(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getStrategyId());
    }

    @Test
    public void testGetStrategyMonitors() {
        List<StrategyMonitor> monitors = new ArrayList<>();
        StrategyMonitor monitor = new StrategyMonitor();
        monitor.setStrategyId(1L);
        monitors.add(monitor);

        when(strategyMonitorMapper.selectByStrategyId(1L)).thenReturn(monitors);

        List<StrategyMonitor> result = strategyController.getStrategyMonitors(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getStrategyId());
    }

    @Test
    public void testDeleteStrategy_Success() {

        when(strategyService.deleteStrategy(1L)).thenReturn(true);

        Map<String, Object> response = strategyController.deleteStrategy(1L);

        assertTrue((Boolean) response.get("success"));
        assertEquals("删除策略成功", response.get("message"));
    }

    @Test
    public void testGetStrategyStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", 10);

        when(strategyService.getStrategyStats()).thenReturn(stats);

        Map<String, Object> response = strategyController.getStrategyStats();

        assertTrue((Boolean) response.get("success"));
        assertEquals(stats, response.get("data"));
    }
}