package com.PatronageSystem.service;

import com.PatronageSystem.entity.Strategy;
import com.PatronageSystem.mapper.StrategyMapper;
import com.PatronageSystem.service.impl.StrategyServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StrategeServiceTest {

    @Mock
    private StrategyMapper strategyMapper;

    @InjectMocks
    private StrategyServiceImpl strategyService;

    // 测试数据
    private final Strategy strategy1 = createStrategy(1L, "STR001", "多因子价值策略", "多因子", 
            "基于价值因子的多因子选股策略", 1L, "中", new BigDecimal("0.1200"), new BigDecimal("0.1500"), 
            "月", "沪深300指数", 1L, 1, 1);

    private final Strategy strategy2 = createStrategy(2L, "STR002", "技术分析策略", "技术分析", 
            "基于技术指标的交易策略", 2L, "高", new BigDecimal("0.1800"), new BigDecimal("0.2000"), 
            "周", "中证500指数", 1L, 1, 1);

    private final Strategy strategy3 = createStrategy(3L, "STR003", "基本面策略", "基本面", 
            "基于基本面分析的选股策略", 3L, "低", new BigDecimal("0.0800"), new BigDecimal("0.1000"), 
            "季", "中债总财富指数", 2L, 0, 1);

    private final Strategy strategy4 = createStrategy(4L, "STR004", "量化对冲策略", "量化对冲", 
            "基于量化模型的对冲策略", 4L, "中", new BigDecimal("0.1000"), new BigDecimal("0.1200"), 
            "日", "中证800指数", 1L, 1, 0);

    private Strategy createStrategy(Long id, String strategyCode, String strategyName, String strategyType,
                                   String description, Long portfolioId, String riskLevel, BigDecimal targetReturn,
                                   BigDecimal maxDrawdown, String rebalanceFrequency, String benchmark,
                                   Long creatorId, Integer isPublic, Integer status) {
        Strategy strategy = new Strategy();
        strategy.setId(id);
        strategy.setStrategyCode(strategyCode);
        strategy.setStrategyName(strategyName);
        strategy.setStrategyType(strategyType);
        strategy.setDescription(description);
        strategy.setPortfolioId(portfolioId);
        strategy.setRiskLevel(riskLevel);
        strategy.setTargetReturn(targetReturn);
        strategy.setMaxDrawdown(maxDrawdown);
        strategy.setRebalanceFrequency(rebalanceFrequency);
        strategy.setBenchmark(benchmark);
        strategy.setCreatorId(creatorId);
        strategy.setIsPublic(isPublic);
        strategy.setStatus(status);
        strategy.setCreateTime(LocalDateTime.now());
        strategy.setUpdateTime(LocalDateTime.now());
        strategy.setPortfolioName("测试组合" + portfolioId);
        strategy.setCreatorName("测试用户" + creatorId);
        return strategy;
    }

    // ==================== 查询功能测试 ====================

    @Test
    void getAllStrategies_ShouldReturnAllStrategies() {
        // 模拟Mapper行为
        when(strategyMapper.selectAll()).thenReturn(Arrays.asList(strategy1, strategy2, strategy3, strategy4));

        // 调用Service方法
        List<Strategy> result = strategyService.getAllStrategies();

        // 验证结果
        assertEquals(4, result.size());
        assertEquals("STR001", result.get(0).getStrategyCode());
        assertEquals("多因子价值策略", result.get(0).getStrategyName());
        assertEquals("多因子", result.get(0).getStrategyType());
        assertEquals("STR002", result.get(1).getStrategyCode());
        assertEquals("技术分析策略", result.get(1).getStrategyName());
        assertEquals("技术分析", result.get(1).getStrategyType());
        verify(strategyMapper, times(1)).selectAll();
    }

    @Test
    void getAllStrategies_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(strategyMapper.selectAll()).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<Strategy> result = strategyService.getAllStrategies();

        // 验证结果
        assertTrue(result.isEmpty());
        verify(strategyMapper, times(1)).selectAll();
    }

    @Test
    void getStrategyById_ShouldReturnStrategy() {
        // 模拟Mapper行为
        when(strategyMapper.selectById(1L)).thenReturn(strategy1);

        // 调用Service方法
        Strategy result = strategyService.getStrategyById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("STR001", result.getStrategyCode());
        assertEquals("多因子价值策略", result.getStrategyName());
        assertEquals("多因子", result.getStrategyType());
        assertEquals("基于价值因子的多因子选股策略", result.getDescription());
        assertEquals(1L, result.getPortfolioId());
        assertEquals("中", result.getRiskLevel());
        assertEquals(new BigDecimal("0.1200"), result.getTargetReturn());
        assertEquals(new BigDecimal("0.1500"), result.getMaxDrawdown());
        assertEquals("月", result.getRebalanceFrequency());
        assertEquals("沪深300指数", result.getBenchmark());
        assertEquals(1L, result.getCreatorId());
        assertEquals(1, result.getIsPublic());
        assertEquals(1, result.getStatus());
        assertEquals("测试组合1", result.getPortfolioName());
        assertEquals("测试用户1", result.getCreatorName());
        verify(strategyMapper, times(1)).selectById(1L);
    }

    @Test
    void getStrategyById_ShouldReturnNull() {
        // 模拟Mapper行为
        when(strategyMapper.selectById(999L)).thenReturn(null);

        // 调用Service方法
        Strategy result = strategyService.getStrategyById(999L);

        // 验证结果
        assertNull(result);
        verify(strategyMapper, times(1)).selectById(999L);
    }

    @Test
    void getStrategyByCode_ShouldReturnStrategy() {
        // 模拟Mapper行为
        when(strategyMapper.selectByStrategyCode("STR001")).thenReturn(strategy1);

        // 调用Service方法
        Strategy result = strategyService.getStrategyByCode("STR001");

        // 验证结果
        assertNotNull(result);
        assertEquals("STR001", result.getStrategyCode());
        assertEquals("多因子价值策略", result.getStrategyName());
        verify(strategyMapper, times(1)).selectByStrategyCode("STR001");
    }

    @Test
    void getStrategyByCode_ShouldReturnNull() {
        // 模拟Mapper行为
        when(strategyMapper.selectByStrategyCode("NONEXISTENT")).thenReturn(null);

        // 调用Service方法
        Strategy result = strategyService.getStrategyByCode("NONEXISTENT");

        // 验证结果
        assertNull(result);
        verify(strategyMapper, times(1)).selectByStrategyCode("NONEXISTENT");
    }

    @Test
    void getStrategiesByCreatorId_ShouldReturnStrategies() {
        // 模拟Mapper行为
        when(strategyMapper.selectByCreatorId(1L)).thenReturn(Arrays.asList(strategy1, strategy2, strategy4));

        // 调用Service方法
        List<Strategy> result = strategyService.getStrategiesByCreatorId(1L);

        // 验证结果
        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(s -> s.getCreatorId().equals(1L)));
        verify(strategyMapper, times(1)).selectByCreatorId(1L);
    }

    @Test
    void getStrategiesByCreatorId_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(strategyMapper.selectByCreatorId(999L)).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<Strategy> result = strategyService.getStrategiesByCreatorId(999L);

        // 验证结果
        assertTrue(result.isEmpty());
        verify(strategyMapper, times(1)).selectByCreatorId(999L);
    }

    @Test
    void getStrategiesByCondition_ShouldReturnStrategies() {
        // 模拟Mapper行为
        when(strategyMapper.selectByCondition("多因子", "中", 1)).thenReturn(Arrays.asList(strategy1));

        // 调用Service方法
        List<Strategy> result = strategyService.getStrategiesByCondition("多因子", "中", 1);

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("多因子", result.get(0).getStrategyType());
        assertEquals("中", result.get(0).getRiskLevel());
        assertEquals(1, result.get(0).getIsPublic());
        verify(strategyMapper, times(1)).selectByCondition("多因子", "中", 1);
    }

    @Test
    void getStrategiesByCondition_WithNullParameters_ShouldReturnStrategies() {
        // 模拟Mapper行为
        when(strategyMapper.selectByCondition(null, null, null)).thenReturn(Arrays.asList(strategy1, strategy2, strategy3, strategy4));

        // 调用Service方法
        List<Strategy> result = strategyService.getStrategiesByCondition(null, null, null);

        // 验证结果
        assertEquals(4, result.size());
        verify(strategyMapper, times(1)).selectByCondition(null, null, null);
    }

    @Test
    void searchStrategies_ShouldReturnMatchingStrategies() {
        // 模拟Mapper行为
        when(strategyMapper.selectByKeyword("多因子")).thenReturn(Arrays.asList(strategy1));

        // 调用Service方法
        List<Strategy> result = strategyService.searchStrategies("多因子");

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("多因子价值策略", result.get(0).getStrategyName());
        assertTrue(result.get(0).getStrategyName().contains("多因子") || result.get(0).getStrategyType().contains("多因子"));
        verify(strategyMapper, times(1)).selectByKeyword("多因子");
    }

    @Test
    void searchStrategies_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(strategyMapper.selectByKeyword("不存在的策略")).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<Strategy> result = strategyService.searchStrategies("不存在的策略");

        // 验证结果
        assertTrue(result.isEmpty());
        verify(strategyMapper, times(1)).selectByKeyword("不存在的策略");
    }

    // ==================== 创建功能测试 ====================

    @Test
    void addStrategy_ShouldReturnTrue() {
        // 准备测试数据
        Strategy newStrategy = createStrategy(null, "STR005", "新策略", "多因子", 
                "新策略描述", 5L, "中", new BigDecimal("0.1000"), new BigDecimal("0.1200"), 
                "月", "沪深300指数", 1L, 1, null);

        // 模拟Mapper行为
        when(strategyMapper.checkStrategyCodeExists("STR005")).thenReturn(0);
        when(strategyMapper.insert(any(Strategy.class))).thenReturn(1);

        // 调用Service方法
        boolean result = strategyService.addStrategy(newStrategy);

        // 验证结果
        assertTrue(result);
        assertEquals(1, newStrategy.getStatus()); // 验证默认状态
        assertEquals(1, newStrategy.getIsPublic()); // 验证默认公开状态
        verify(strategyMapper, times(1)).checkStrategyCodeExists("STR005");
        verify(strategyMapper, times(1)).insert(newStrategy);
    }

    @Test
    void addStrategy_WithExistingCode_ShouldThrowException() {
        // 准备测试数据
        Strategy newStrategy = createStrategy(null, "STR001", "重复代码策略", "多因子", 
                "描述", 5L, "中", new BigDecimal("0.1000"), new BigDecimal("0.1200"), 
                "月", "沪深300指数", 1L, 1, 1);

        // 模拟Mapper行为
        when(strategyMapper.checkStrategyCodeExists("STR001")).thenReturn(1);

        // 调用Service方法并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            strategyService.addStrategy(newStrategy);
        });

        assertEquals("策略代码已存在", exception.getMessage());
        verify(strategyMapper, times(1)).checkStrategyCodeExists("STR001");
        verify(strategyMapper, never()).insert(any(Strategy.class));
    }

    @Test
    void addStrategy_WithNullValues_ShouldReturnTrue() {
        // 准备测试数据
        Strategy newStrategy = new Strategy();
        newStrategy.setStrategyCode("STR006");
        newStrategy.setStrategyName("空值测试策略");
        newStrategy.setStrategyType("多因子");
        newStrategy.setDescription(null);
        newStrategy.setPortfolioId(null);
        newStrategy.setRiskLevel(null);
        newStrategy.setTargetReturn(null);
        newStrategy.setMaxDrawdown(null);
        newStrategy.setRebalanceFrequency(null);
        newStrategy.setBenchmark(null);
        newStrategy.setCreatorId(1L);
        newStrategy.setIsPublic(null);
        newStrategy.setStatus(null);

        // 模拟Mapper行为
        when(strategyMapper.checkStrategyCodeExists("STR006")).thenReturn(0);
        when(strategyMapper.insert(any(Strategy.class))).thenReturn(1);

        // 调用Service方法
        boolean result = strategyService.addStrategy(newStrategy);

        // 验证结果
        assertTrue(result);
        assertEquals(1, newStrategy.getStatus()); // 验证默认状态
        assertEquals(1, newStrategy.getIsPublic()); // 验证默认公开状态
        verify(strategyMapper, times(1)).checkStrategyCodeExists("STR006");
        verify(strategyMapper, times(1)).insert(newStrategy);
    }

    @Test
    void addStrategy_ShouldReturnFalse() {
        // 准备测试数据
        Strategy newStrategy = createStrategy(null, "STR007", "失败测试策略", "多因子", 
                "描述", 5L, "中", new BigDecimal("0.1000"), new BigDecimal("0.1200"), 
                "月", "沪深300指数", 1L, 1, 1);

        // 模拟Mapper行为
        when(strategyMapper.checkStrategyCodeExists("STR007")).thenReturn(0);
        when(strategyMapper.insert(any(Strategy.class))).thenReturn(0);

        // 调用Service方法
        boolean result = strategyService.addStrategy(newStrategy);

        // 验证结果
        assertFalse(result);
        verify(strategyMapper, times(1)).checkStrategyCodeExists("STR007");
        verify(strategyMapper, times(1)).insert(newStrategy);
    }

    // ==================== 更新功能测试 ====================

    @Test
    void updateStrategy_ShouldReturnTrue() {
        // 准备测试数据
        Strategy updatedStrategy = createStrategy(1L, "STR001", "更新后的策略", "多因子", 
                "更新后的描述", 1L, "高", new BigDecimal("0.1500"), new BigDecimal("0.1800"), 
                "周", "中证500指数", 1L, 0, 1);

        // 模拟Mapper行为
        when(strategyMapper.selectByStrategyCode("STR001")).thenReturn(strategy1);
        when(strategyMapper.update(any(Strategy.class))).thenReturn(1);

        // 调用Service方法
        boolean result = strategyService.updateStrategy(updatedStrategy);

        // 验证结果
        assertTrue(result);
        verify(strategyMapper, times(1)).selectByStrategyCode("STR001");
        verify(strategyMapper, times(1)).update(updatedStrategy);
    }

    @Test
    void updateStrategy_WithCodeConflict_ShouldThrowException() {
        // 准备测试数据
        Strategy updatedStrategy = createStrategy(1L, "STR002", "冲突代码策略", "多因子", 
                "描述", 1L, "中", new BigDecimal("0.1000"), new BigDecimal("0.1200"), 
                "月", "沪深300指数", 1L, 1, 1);

        // 模拟Mapper行为
        when(strategyMapper.selectByStrategyCode("STR002")).thenReturn(strategy2);

        // 调用Service方法并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            strategyService.updateStrategy(updatedStrategy);
        });

        assertEquals("策略代码已被其他策略使用", exception.getMessage());
        verify(strategyMapper, times(1)).selectByStrategyCode("STR002");
        verify(strategyMapper, never()).update(any(Strategy.class));
    }

    @Test
    void updateStrategy_WithSameCode_ShouldReturnTrue() {
        // 准备测试数据 - 更新自己的代码（应该允许）
        Strategy updatedStrategy = createStrategy(1L, "STR001", "更新后的策略", "多因子", 
                "更新后的描述", 1L, "高", new BigDecimal("0.1500"), new BigDecimal("0.1800"), 
                "周", "中证500指数", 1L, 0, 1);

        // 模拟Mapper行为 - 返回相同的策略（自己）
        when(strategyMapper.selectByStrategyCode("STR001")).thenReturn(strategy1);
        when(strategyMapper.update(any(Strategy.class))).thenReturn(1);

        // 调用Service方法
        boolean result = strategyService.updateStrategy(updatedStrategy);

        // 验证结果
        assertTrue(result);
        verify(strategyMapper, times(1)).selectByStrategyCode("STR001");
        verify(strategyMapper, times(1)).update(updatedStrategy);
    }

    @Test
    void updateStrategy_ShouldReturnFalse() {
        // 准备测试数据
        Strategy updatedStrategy = createStrategy(999L, "STR999", "不存在的策略", "多因子", 
                "描述", 1L, "中", new BigDecimal("0.1000"), new BigDecimal("0.1200"), 
                "月", "沪深300指数", 1L, 1, 1);

        // 模拟Mapper行为
        when(strategyMapper.selectByStrategyCode("STR999")).thenReturn(null);
        when(strategyMapper.update(any(Strategy.class))).thenReturn(0);

        // 调用Service方法
        boolean result = strategyService.updateStrategy(updatedStrategy);

        // 验证结果
        assertFalse(result);
        verify(strategyMapper, times(1)).selectByStrategyCode("STR999");
        verify(strategyMapper, times(1)).update(updatedStrategy);
    }

    // ==================== 删除功能测试 ====================

    @Test
    void deleteStrategy_ShouldReturnTrue() {
        // 模拟Mapper行为
        when(strategyMapper.deleteById(1L)).thenReturn(1);

        // 调用Service方法
        boolean result = strategyService.deleteStrategy(1L);

        // 验证结果
        assertTrue(result);
        verify(strategyMapper, times(1)).deleteById(1L);
    }

    @Test
    void deleteStrategy_ShouldReturnFalse() {
        // 模拟Mapper行为
        when(strategyMapper.deleteById(999L)).thenReturn(0);

        // 调用Service方法
        boolean result = strategyService.deleteStrategy(999L);

        // 验证结果
        assertFalse(result);
        verify(strategyMapper, times(1)).deleteById(999L);
    }

    // ==================== 验证功能测试 ====================

    @Test
    void isStrategyCodeExists_ShouldReturnTrue() {
        // 模拟Mapper行为
        when(strategyMapper.checkStrategyCodeExists("STR001")).thenReturn(1);

        // 调用Service方法
        boolean result = strategyService.isStrategyCodeExists("STR001");

        // 验证结果
        assertTrue(result);
        verify(strategyMapper, times(1)).checkStrategyCodeExists("STR001");
    }

    @Test
    void isStrategyCodeExists_ShouldReturnFalse() {
        // 模拟Mapper行为
        when(strategyMapper.checkStrategyCodeExists("NONEXISTENT")).thenReturn(0);

        // 调用Service方法
        boolean result = strategyService.isStrategyCodeExists("NONEXISTENT");

        // 验证结果
        assertFalse(result);
        verify(strategyMapper, times(1)).checkStrategyCodeExists("NONEXISTENT");
    }

    // ==================== 统计功能测试 ====================

    @Test
    void getStrategyStats_ShouldReturnStats() {
        // 准备测试数据
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", 4);
        stats.put("active", 3);
        stats.put("inactive", 1);
        stats.put("public", 3);
        stats.put("private", 1);

        // 模拟Mapper行为
        when(strategyMapper.selectStrategyStats()).thenReturn(stats);

        // 调用Service方法
        Map<String, Object> result = strategyService.getStrategyStats();

        // 验证结果
        assertNotNull(result);
        assertEquals(4, result.get("total"));
        assertEquals(3, result.get("active"));
        assertEquals(1, result.get("inactive"));
        assertEquals(3, result.get("public"));
        assertEquals(1, result.get("private"));
        verify(strategyMapper, times(1)).selectStrategyStats();
    }

    @Test
    void getStrategyCountByType_ShouldReturnCounts() {
        // 准备测试数据
        Map<String, Object> type1 = new HashMap<>();
        type1.put("strategyType", "多因子");
        type1.put("count", 2);

        Map<String, Object> type2 = new HashMap<>();
        type2.put("strategyType", "技术分析");
        type2.put("count", 1);

        List<Map<String, Object>> typeCounts = Arrays.asList(type1, type2);

        // 模拟Mapper行为
        when(strategyMapper.selectStrategyCountByType()).thenReturn(typeCounts);

        // 调用Service方法
        List<Map<String, Object>> result = strategyService.getStrategyCountByType();

        // 验证结果
        assertEquals(2, result.size());
        assertEquals("多因子", result.get(0).get("strategyType"));
        assertEquals(2, result.get(0).get("count"));
        assertEquals("技术分析", result.get(1).get("strategyType"));
        assertEquals(1, result.get(1).get("count"));
        verify(strategyMapper, times(1)).selectStrategyCountByType();
    }

    @Test
    void getStrategyCountByRiskLevel_ShouldReturnCounts() {
        // 准备测试数据
        Map<String, Object> risk1 = new HashMap<>();
        risk1.put("riskLevel", "中");
        risk1.put("count", 2);

        Map<String, Object> risk2 = new HashMap<>();
        risk2.put("riskLevel", "高");
        risk2.put("count", 1);

        Map<String, Object> risk3 = new HashMap<>();
        risk3.put("riskLevel", "低");
        risk3.put("count", 1);

        List<Map<String, Object>> riskCounts = Arrays.asList(risk1, risk2, risk3);

        // 模拟Mapper行为
        when(strategyMapper.selectStrategyCountByRiskLevel()).thenReturn(riskCounts);

        // 调用Service方法
        List<Map<String, Object>> result = strategyService.getStrategyCountByRiskLevel();

        // 验证结果
        assertEquals(3, result.size());
        assertEquals("中", result.get(0).get("riskLevel"));
        assertEquals(2, result.get(0).get("count"));
        assertEquals("高", result.get(1).get("riskLevel"));
        assertEquals(1, result.get(1).get("count"));
        assertEquals("低", result.get(2).get("riskLevel"));
        assertEquals(1, result.get(2).get("count"));
        verify(strategyMapper, times(1)).selectStrategyCountByRiskLevel();
    }

    // ==================== 边界情况测试 ====================

    @Test
    void getStrategyById_WithNullId_ShouldReturnNull() {
        // 调用Service方法
        Strategy result = strategyService.getStrategyById(null);

        // 验证结果
        assertNull(result);
        verify(strategyMapper, times(1)).selectById(null);
    }

    @Test
    void getStrategyByCode_WithNullCode_ShouldReturnNull() {
        // 调用Service方法
        Strategy result = strategyService.getStrategyByCode(null);

        // 验证结果
        assertNull(result);
        verify(strategyMapper, times(1)).selectByStrategyCode(null);
    }

    @Test
    void getStrategyByCode_WithEmptyCode_ShouldReturnNull() {
        // 调用Service方法
        Strategy result = strategyService.getStrategyByCode("");

        // 验证结果
        assertNull(result);
        verify(strategyMapper, times(1)).selectByStrategyCode("");
    }

    @Test
    void getStrategiesByCreatorId_WithNullCreatorId_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(strategyMapper.selectByCreatorId(null)).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<Strategy> result = strategyService.getStrategiesByCreatorId(null);

        // 验证结果
        assertTrue(result.isEmpty());
        verify(strategyMapper, times(1)).selectByCreatorId(null);
    }

    @Test
    void searchStrategies_WithNullKeyword_ShouldReturnAllStrategies() {
        // 模拟Mapper行为
        when(strategyMapper.selectByKeyword(null)).thenReturn(Arrays.asList(strategy1, strategy2, strategy3, strategy4));

        // 调用Service方法
        List<Strategy> result = strategyService.searchStrategies(null);

        // 验证结果
        assertEquals(4, result.size());
        verify(strategyMapper, times(1)).selectByKeyword(null);
    }

    @Test
    void searchStrategies_WithEmptyKeyword_ShouldReturnAllStrategies() {
        // 模拟Mapper行为
        when(strategyMapper.selectByKeyword("")).thenReturn(Arrays.asList(strategy1, strategy2, strategy3, strategy4));

        // 调用Service方法
        List<Strategy> result = strategyService.searchStrategies("");

        // 验证结果
        assertEquals(4, result.size());
        verify(strategyMapper, times(1)).selectByKeyword("");
    }

    @Test
    void addStrategy_WithNullStrategy_ShouldThrowException() {
        // 调用Service方法并验证异常
        assertThrows(NullPointerException.class, () -> {
            strategyService.addStrategy(null);
        });
    }

    @Test
    void updateStrategy_WithNullStrategy_ShouldThrowException() {
        // 调用Service方法并验证异常
        assertThrows(NullPointerException.class, () -> {
            strategyService.updateStrategy(null);
        });
    }

    @Test
    void deleteStrategy_WithNullId_ShouldReturnFalse() {
        // 模拟Mapper行为
        when(strategyMapper.deleteById(null)).thenReturn(0);

        // 调用Service方法
        boolean result = strategyService.deleteStrategy(null);

        // 验证结果
        assertFalse(result);
        verify(strategyMapper, times(1)).deleteById(null);
    }

    @Test
    void isStrategyCodeExists_WithNullCode_ShouldReturnFalse() {
        // 模拟Mapper行为
        when(strategyMapper.checkStrategyCodeExists(null)).thenReturn(0);

        // 调用Service方法
        boolean result = strategyService.isStrategyCodeExists(null);

        // 验证结果
        assertFalse(result);
        verify(strategyMapper, times(1)).checkStrategyCodeExists(null);
    }

    @Test
    void isStrategyCodeExists_WithEmptyCode_ShouldReturnFalse() {
        // 模拟Mapper行为
        when(strategyMapper.checkStrategyCodeExists("")).thenReturn(0);

        // 调用Service方法
        boolean result = strategyService.isStrategyCodeExists("");

        // 验证结果
        assertFalse(result);
        verify(strategyMapper, times(1)).checkStrategyCodeExists("");
    }

    @Test
    void getStrategyStats_ShouldReturnEmptyStats() {
        // 模拟Mapper行为
        when(strategyMapper.selectStrategyStats()).thenReturn(new HashMap<>());

        // 调用Service方法
        Map<String, Object> result = strategyService.getStrategyStats();

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(strategyMapper, times(1)).selectStrategyStats();
    }

    @Test
    void getStrategyCountByType_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(strategyMapper.selectStrategyCountByType()).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<Map<String, Object>> result = strategyService.getStrategyCountByType();

        // 验证结果
        assertTrue(result.isEmpty());
        verify(strategyMapper, times(1)).selectStrategyCountByType();
    }

    @Test
    void getStrategyCountByRiskLevel_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(strategyMapper.selectStrategyCountByRiskLevel()).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<Map<String, Object>> result = strategyService.getStrategyCountByRiskLevel();

        // 验证结果
        assertTrue(result.isEmpty());
        verify(strategyMapper, times(1)).selectStrategyCountByRiskLevel();
    }
}
