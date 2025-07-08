package com.PatronageSystem.service;

import com.PatronageSystem.Patronage.entity.RebalancePlan;
import com.PatronageSystem.Patronage.mapper.RebalancePlanMapper;
import com.PatronageSystem.Patronage.service.impl.RebalancePlanServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RebalancePlanServiceTest {

    @Mock
    private RebalancePlanMapper rebalancePlanMapper;

    @InjectMocks
    private RebalancePlanServiceImpl rebalancePlanService;

    // 测试数据
    private final RebalancePlan plan1 = createRebalancePlan(1L, "2024年二季度调仓", "成长进取组合", "组合",
            "{\"A股\":200000,\"港股\":80000}", "[{\"action\":\"买入\",\"asset\":\"港股\",\"amount\":20000}]", "DISPATCHED");

    private final RebalancePlan plan2 = createRebalancePlan(2L, "2024年三季度调仓", "价值投资组合", "定制",
            "{\"A股\":150000,\"美股\":60000}", "[{\"action\":\"卖出\",\"asset\":\"A股\",\"amount\":5000}]", "DISPATCHED");

    private final RebalancePlan plan3 = createRebalancePlan(3L, "2024年四季度调仓", "稳健配置组合", "FOF",
            "{\"债券\":300000,\"货币\":100000}", "[{\"action\":\"买入\",\"asset\":\"债券\",\"amount\":50000}]", "PENDING");

    private final RebalancePlan plan4 = createRebalancePlan(4L, "测试调仓计划", "测试组合", "组合",
            "[\n  {\n    \"id\": 1,\n    \"accountId\": 1,\n    \"fundId\": 1,\n    \"totalQuantity\": 10000,\n    \"availableQuantity\": 10000,\n    \"frozenQuantity\": 0,\n    \"avgCost\": 1.25,\n    \"marketPrice\": 1.275,\n    \"marketValue\": 12750,\n    \"unrealizedPnl\": 250,\n    \"realizedPnl\": 0,\n    \"totalPnl\": 250,\n    \"updateTime\": \"2025-07-02T14:11:30\"\n  }\n]", null, "PENDING");

    private RebalancePlan createRebalancePlan(Long id, String planName, String portfolioName, String portfolioType,
                                              String currentHoldings, String rebalanceInstructions, String status) {
        RebalancePlan plan = new RebalancePlan();
        plan.setId(id);
        plan.setPlanName(planName);
        plan.setPortfolioName(portfolioName);
        plan.setPortfolioType(portfolioType);
        plan.setCurrentHoldings(currentHoldings);
        plan.setRebalanceInstructions(rebalanceInstructions);
        plan.setStatus(status);
        plan.setCreateTime(LocalDateTime.now());
        plan.setUpdateTime(LocalDateTime.now());
        return plan;
    }

    // ==================== 查询功能测试 ====================

    @Test
    void getAllRebalancePlans_ShouldReturnAllPlans() {
        // 模拟Mapper行为
        when(rebalancePlanMapper.selectAll()).thenReturn(Arrays.asList(plan1, plan2, plan3, plan4));

        // 调用Service方法
        List<RebalancePlan> result = rebalancePlanService.getAllRebalancePlans();

        // 验证结果
        assertEquals(4, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("2024年二季度调仓", result.get(0).getPlanName());
        assertEquals("成长进取组合", result.get(0).getPortfolioName());
        assertEquals("组合", result.get(0).getPortfolioType());
        assertEquals("DISPATCHED", result.get(0).getStatus());
        
        assertEquals(2L, result.get(1).getId());
        assertEquals("2024年三季度调仓", result.get(1).getPlanName());
        assertEquals("价值投资组合", result.get(1).getPortfolioName());
        assertEquals("定制", result.get(1).getPortfolioType());
        assertEquals("DISPATCHED", result.get(1).getStatus());
        
        assertEquals(3L, result.get(2).getId());
        assertEquals("2024年四季度调仓", result.get(2).getPlanName());
        assertEquals("稳健配置组合", result.get(2).getPortfolioName());
        assertEquals("FOF", result.get(2).getPortfolioType());
        assertEquals("PENDING", result.get(2).getStatus());
        
        assertEquals(4L, result.get(3).getId());
        assertEquals("测试调仓计划", result.get(3).getPlanName());
        assertEquals("测试组合", result.get(3).getPortfolioName());
        assertEquals("组合", result.get(3).getPortfolioType());
        assertEquals("PENDING", result.get(3).getStatus());
        
        verify(rebalancePlanMapper, times(1)).selectAll();
    }

    @Test
    void getAllRebalancePlans_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(rebalancePlanMapper.selectAll()).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<RebalancePlan> result = rebalancePlanService.getAllRebalancePlans();

        // 验证结果
        assertTrue(result.isEmpty());
        verify(rebalancePlanMapper, times(1)).selectAll();
    }

    @Test
    void getRebalancePlanById_ShouldReturnPlan() {
        // 模拟Mapper行为
        when(rebalancePlanMapper.selectById(1L)).thenReturn(plan1);

        // 调用Service方法
        RebalancePlan result = rebalancePlanService.getRebalancePlanById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("2024年二季度调仓", result.getPlanName());
        assertEquals("成长进取组合", result.getPortfolioName());
        assertEquals("组合", result.getPortfolioType());
        assertEquals("{\"A股\":200000,\"港股\":80000}", result.getCurrentHoldings());
        assertEquals("[{\"action\":\"买入\",\"asset\":\"港股\",\"amount\":20000}]", result.getRebalanceInstructions());
        assertEquals("DISPATCHED", result.getStatus());
        assertNotNull(result.getCreateTime());
        assertNotNull(result.getUpdateTime());
        verify(rebalancePlanMapper, times(1)).selectById(1L);
    }

    @Test
    void getRebalancePlanById_ShouldReturnNull() {
        // 模拟Mapper行为
        when(rebalancePlanMapper.selectById(999L)).thenReturn(null);

        // 调用Service方法
        RebalancePlan result = rebalancePlanService.getRebalancePlanById(999L);

        // 验证结果
        assertNull(result);
        verify(rebalancePlanMapper, times(1)).selectById(999L);
    }

    // ==================== 创建功能测试 ====================

    @Test
    void createRebalancePlan_ShouldReturnSuccess() {
        // 准备测试数据
        RebalancePlan newPlan = createRebalancePlan(null, "新调仓计划", "新组合", "组合",
                "{\"A股\":100000,\"债券\":50000}", "[{\"action\":\"买入\",\"asset\":\"A股\",\"amount\":10000}]", "PENDING");

        // 模拟Mapper行为
        when(rebalancePlanMapper.insert(any(RebalancePlan.class))).thenReturn(1);

        // 调用Service方法
        int result = rebalancePlanService.createRebalancePlan(newPlan);

        // 验证结果
        assertEquals(1, result);
        verify(rebalancePlanMapper, times(1)).insert(newPlan);
    }

    @Test
    void createRebalancePlan_WithNullValues_ShouldReturnSuccess() {
        // 准备测试数据
        RebalancePlan newPlan = new RebalancePlan();
        newPlan.setPlanName("空值测试计划");
        newPlan.setPortfolioName("空值测试组合");
        newPlan.setPortfolioType("组合");
        newPlan.setCurrentHoldings(null);
        newPlan.setRebalanceInstructions(null);
        newPlan.setStatus("PENDING");

        // 模拟Mapper行为
        when(rebalancePlanMapper.insert(any(RebalancePlan.class))).thenReturn(1);

        // 调用Service方法
        int result = rebalancePlanService.createRebalancePlan(newPlan);

        // 验证结果
        assertEquals(1, result);
        verify(rebalancePlanMapper, times(1)).insert(newPlan);
    }

    @Test
    void createRebalancePlan_WithEmptyStrings_ShouldReturnSuccess() {
        // 准备测试数据
        RebalancePlan newPlan = createRebalancePlan(null, "", "", "",
                "", "", "");

        // 模拟Mapper行为
        when(rebalancePlanMapper.insert(any(RebalancePlan.class))).thenReturn(1);

        // 调用Service方法
        int result = rebalancePlanService.createRebalancePlan(newPlan);

        // 验证结果
        assertEquals(1, result);
        verify(rebalancePlanMapper, times(1)).insert(newPlan);
    }

    @Test
    void createRebalancePlan_WithComplexJsonData_ShouldReturnSuccess() {
        // 准备测试数据 - 包含复杂的JSON数据
        String complexHoldings = "[\n" +
                "  {\n" +
                "    \"id\": 1,\n" +
                "    \"accountId\": 1,\n" +
                "    \"fundId\": 1,\n" +
                "    \"totalQuantity\": 10000,\n" +
                "    \"availableQuantity\": 10000,\n" +
                "    \"frozenQuantity\": 0,\n" +
                "    \"avgCost\": 1.25,\n" +
                "    \"marketPrice\": 1.275,\n" +
                "    \"marketValue\": 12750,\n" +
                "    \"unrealizedPnl\": 250,\n" +
                "    \"realizedPnl\": 0,\n" +
                "    \"totalPnl\": 250,\n" +
                "    \"updateTime\": \"2025-07-02T14:11:30\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 2,\n" +
                "    \"accountId\": 1,\n" +
                "    \"fundId\": 2,\n" +
                "    \"totalQuantity\": 2000,\n" +
                "    \"availableQuantity\": 2000,\n" +
                "    \"frozenQuantity\": 0,\n" +
                "    \"avgCost\": 1.18,\n" +
                "    \"marketPrice\": 1.185,\n" +
                "    \"marketValue\": 2370,\n" +
                "    \"unrealizedPnl\": 10,\n" +
                "    \"realizedPnl\": 0,\n" +
                "    \"totalPnl\": 10,\n" +
                "    \"updateTime\": \"2025-07-02T14:11:30\"\n" +
                "  }\n" +
                "]";

        String complexInstructions = "[\n" +
                "  {\n" +
                "    \"action\": \"买入\",\n" +
                "    \"asset\": \"A股\",\n" +
                "    \"amount\": 10000,\n" +
                "    \"reason\": \"估值修复\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"action\": \"卖出\",\n" +
                "    \"asset\": \"债券\",\n" +
                "    \"amount\": 5000,\n" +
                "    \"reason\": \"利率上行\"\n" +
                "  }\n" +
                "]";

        RebalancePlan newPlan = createRebalancePlan(null, "复杂JSON测试", "复杂组合", "定制",
                complexHoldings, complexInstructions, "PENDING");

        // 模拟Mapper行为
        when(rebalancePlanMapper.insert(any(RebalancePlan.class))).thenReturn(1);

        // 调用Service方法
        int result = rebalancePlanService.createRebalancePlan(newPlan);

        // 验证结果
        assertEquals(1, result);
        verify(rebalancePlanMapper, times(1)).insert(newPlan);
    }

    @Test
    void createRebalancePlan_ShouldReturnFailure() {
        // 准备测试数据
        RebalancePlan newPlan = createRebalancePlan(null, "失败测试计划", "失败组合", "组合",
                "{}", "[]", "PENDING");

        // 模拟Mapper行为
        when(rebalancePlanMapper.insert(any(RebalancePlan.class))).thenReturn(0);

        // 调用Service方法
        int result = rebalancePlanService.createRebalancePlan(newPlan);

        // 验证结果
        assertEquals(0, result);
        verify(rebalancePlanMapper, times(1)).insert(newPlan);
    }

    // ==================== 更新功能测试 ====================

    @Test
    void updateRebalancePlan_ShouldReturnSuccess() {
        // 准备测试数据
        RebalancePlan updatedPlan = createRebalancePlan(1L, "更新后的调仓计划", "更新后的组合", "组合",
                "{\"A股\":250000,\"港股\":100000}", "[{\"action\":\"买入\",\"asset\":\"港股\",\"amount\":30000}]", "COMPLETED");

        // 模拟Mapper行为
        when(rebalancePlanMapper.update(any(RebalancePlan.class))).thenReturn(1);

        // 调用Service方法
        int result = rebalancePlanService.updateRebalancePlan(updatedPlan);

        // 验证结果
        assertEquals(1, result);
        verify(rebalancePlanMapper, times(1)).update(updatedPlan);
    }

    @Test
    void updateRebalancePlan_WithStatusChange_ShouldReturnSuccess() {
        // 准备测试数据 - 只更新状态
        RebalancePlan updatedPlan = new RebalancePlan();
        updatedPlan.setId(3L);
        updatedPlan.setPlanName("2024年四季度调仓");
        updatedPlan.setPortfolioName("稳健配置组合");
        updatedPlan.setPortfolioType("FOF");
        updatedPlan.setCurrentHoldings("{\"债券\":300000,\"货币\":100000}");
        updatedPlan.setRebalanceInstructions("[{\"action\":\"买入\",\"asset\":\"债券\",\"amount\":50000}]");
        updatedPlan.setStatus("COMPLETED");

        // 模拟Mapper行为
        when(rebalancePlanMapper.update(any(RebalancePlan.class))).thenReturn(1);

        // 调用Service方法
        int result = rebalancePlanService.updateRebalancePlan(updatedPlan);

        // 验证结果
        assertEquals(1, result);
        assertEquals("COMPLETED", updatedPlan.getStatus());
        verify(rebalancePlanMapper, times(1)).update(updatedPlan);
    }

    @Test
    void updateRebalancePlan_WithNullValues_ShouldReturnSuccess() {
        // 准备测试数据
        RebalancePlan updatedPlan = new RebalancePlan();
        updatedPlan.setId(4L);
        updatedPlan.setPlanName("空值更新测试");
        updatedPlan.setPortfolioName(null);
        updatedPlan.setPortfolioType(null);
        updatedPlan.setCurrentHoldings(null);
        updatedPlan.setRebalanceInstructions(null);
        updatedPlan.setStatus(null);

        // 模拟Mapper行为
        when(rebalancePlanMapper.update(any(RebalancePlan.class))).thenReturn(1);

        // 调用Service方法
        int result = rebalancePlanService.updateRebalancePlan(updatedPlan);

        // 验证结果
        assertEquals(1, result);
        verify(rebalancePlanMapper, times(1)).update(updatedPlan);
    }

    @Test
    void updateRebalancePlan_ShouldReturnFailure() {
        // 准备测试数据
        RebalancePlan updatedPlan = createRebalancePlan(999L, "不存在的计划", "不存在的组合", "组合",
                "{}", "[]", "PENDING");

        // 模拟Mapper行为
        when(rebalancePlanMapper.update(any(RebalancePlan.class))).thenReturn(0);

        // 调用Service方法
        int result = rebalancePlanService.updateRebalancePlan(updatedPlan);

        // 验证结果
        assertEquals(0, result);
        verify(rebalancePlanMapper, times(1)).update(updatedPlan);
    }

    // ==================== 删除功能测试 ====================

    @Test
    void deleteRebalancePlan_ShouldReturnSuccess() {
        // 模拟Mapper行为
        when(rebalancePlanMapper.deleteById(1L)).thenReturn(1);

        // 调用Service方法
        int result = rebalancePlanService.deleteRebalancePlan(1L);

        // 验证结果
        assertEquals(1, result);
        verify(rebalancePlanMapper, times(1)).deleteById(1L);
    }

    @Test
    void deleteRebalancePlan_ShouldReturnFailure() {
        // 模拟Mapper行为
        when(rebalancePlanMapper.deleteById(999L)).thenReturn(0);

        // 调用Service方法
        int result = rebalancePlanService.deleteRebalancePlan(999L);

        // 验证结果
        assertEquals(0, result);
        verify(rebalancePlanMapper, times(1)).deleteById(999L);
    }

    // ==================== 边界情况测试 ====================

    @Test
    void getRebalancePlanById_WithNullId_ShouldReturnNull() {
        // 调用Service方法
        RebalancePlan result = rebalancePlanService.getRebalancePlanById(null);

        // 验证结果
        assertNull(result);
        verify(rebalancePlanMapper, times(1)).selectById(null);
    }

    @Test
    void createRebalancePlan_WithNullPlan_ShouldReturnFailure() {
        // 模拟Mapper行为
        when(rebalancePlanMapper.insert(null)).thenReturn(0);

        // 调用Service方法
        int result = rebalancePlanService.createRebalancePlan(null);

        // 验证结果
        assertEquals(0, result);
        verify(rebalancePlanMapper, times(1)).insert(null);
    }

    @Test
    void updateRebalancePlan_WithNullPlan_ShouldReturnFailure() {
        // 模拟Mapper行为
        when(rebalancePlanMapper.update(null)).thenReturn(0);

        // 调用Service方法
        int result = rebalancePlanService.updateRebalancePlan(null);

        // 验证结果
        assertEquals(0, result);
        verify(rebalancePlanMapper, times(1)).update(null);
    }

    @Test
    void deleteRebalancePlan_WithNullId_ShouldReturnFailure() {
        // 模拟Mapper行为
        when(rebalancePlanMapper.deleteById(null)).thenReturn(0);

        // 调用Service方法
        int result = rebalancePlanService.deleteRebalancePlan(null);

        // 验证结果
        assertEquals(0, result);
        verify(rebalancePlanMapper, times(1)).deleteById(null);
    }

    @Test
    void createRebalancePlan_WithVeryLongStrings_ShouldReturnSuccess() {
        // 准备测试数据 - 包含很长的字符串
        String longPlanName = "这是一个非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常长的调仓计划名称";
        String longPortfolioName = "这是一个非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常长的组合名称";
        String longPortfolioType = "这是一个非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常长的组合类型";

        RebalancePlan newPlan = createRebalancePlan(null, longPlanName, longPortfolioName, longPortfolioType,
                "{}", "[]", "PENDING");

        // 模拟Mapper行为
        when(rebalancePlanMapper.insert(any(RebalancePlan.class))).thenReturn(1);

        // 调用Service方法
        int result = rebalancePlanService.createRebalancePlan(newPlan);

        // 验证结果
        assertEquals(1, result);
        verify(rebalancePlanMapper, times(1)).insert(newPlan);
    }

    @Test
    void createRebalancePlan_WithSpecialCharacters_ShouldReturnSuccess() {
        // 准备测试数据 - 包含特殊字符
        RebalancePlan newPlan = createRebalancePlan(null, "特殊字符测试计划!@#$%^&*()", "特殊组合名称\"'<>", "特殊类型&*()",
                "{\"特殊字段\":\"特殊值!@#\"}", "[{\"action\":\"特殊操作\",\"asset\":\"特殊资产!@#\"}]", "特殊状态");

        // 模拟Mapper行为
        when(rebalancePlanMapper.insert(any(RebalancePlan.class))).thenReturn(1);

        // 调用Service方法
        int result = rebalancePlanService.createRebalancePlan(newPlan);

        // 验证结果
        assertEquals(1, result);
        verify(rebalancePlanMapper, times(1)).insert(newPlan);
    }

    @Test
    void updateRebalancePlan_WithAllStatuses_ShouldReturnSuccess() {
        // 测试所有可能的状态
        String[] statuses = {"PENDING", "DISPATCHED", "COMPLETED", "FAILED", "CANCELLED"};
        
        for (String status : statuses) {
            RebalancePlan updatedPlan = createRebalancePlan(1L, "状态测试计划", "状态测试组合", "组合",
                    "{}", "[]", status);

            // 模拟Mapper行为
            when(rebalancePlanMapper.update(any(RebalancePlan.class))).thenReturn(1);

            // 调用Service方法
            int result = rebalancePlanService.updateRebalancePlan(updatedPlan);

            // 验证结果
            assertEquals(1, result);
            assertEquals(status, updatedPlan.getStatus());
            verify(rebalancePlanMapper, times(1)).update(updatedPlan);
            
            // 重置Mock
            reset(rebalancePlanMapper);
        }
    }

    @Test
    void getAllRebalancePlans_WithSinglePlan_ShouldReturnOnePlan() {
        // 模拟Mapper行为
        when(rebalancePlanMapper.selectAll()).thenReturn(Arrays.asList(plan1));

        // 调用Service方法
        List<RebalancePlan> result = rebalancePlanService.getAllRebalancePlans();

        // 验证结果
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("2024年二季度调仓", result.get(0).getPlanName());
        assertEquals("成长进取组合", result.get(0).getPortfolioName());
        assertEquals("组合", result.get(0).getPortfolioType());
        assertEquals("DISPATCHED", result.get(0).getStatus());
        verify(rebalancePlanMapper, times(1)).selectAll();
    }

    @Test
    void getRebalancePlanById_WithExistingPlan_ShouldReturnPlan() {
        // 模拟Mapper行为
        when(rebalancePlanMapper.selectById(2L)).thenReturn(plan2);

        // 调用Service方法
        RebalancePlan result = rebalancePlanService.getRebalancePlanById(2L);

        // 验证结果
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("2024年三季度调仓", result.getPlanName());
        assertEquals("价值投资组合", result.getPortfolioName());
        assertEquals("定制", result.getPortfolioType());
        assertEquals("{\"A股\":150000,\"美股\":60000}", result.getCurrentHoldings());
        assertEquals("[{\"action\":\"卖出\",\"asset\":\"A股\",\"amount\":5000}]", result.getRebalanceInstructions());
        assertEquals("DISPATCHED", result.getStatus());
        verify(rebalancePlanMapper, times(1)).selectById(2L);
    }

    @Test
    void createRebalancePlan_WithDifferentPortfolioTypes_ShouldReturnSuccess() {
        // 测试不同的组合类型
        String[] portfolioTypes = {"组合", "FOF", "定制", "混合", "其他"};
        
        for (String portfolioType : portfolioTypes) {
            RebalancePlan newPlan = createRebalancePlan(null, "类型测试计划", "类型测试组合", portfolioType,
                    "{}", "[]", "PENDING");

            // 模拟Mapper行为
            when(rebalancePlanMapper.insert(any(RebalancePlan.class))).thenReturn(1);

            // 调用Service方法
            int result = rebalancePlanService.createRebalancePlan(newPlan);

            // 验证结果
            assertEquals(1, result);
            assertEquals(portfolioType, newPlan.getPortfolioType());
            verify(rebalancePlanMapper, times(1)).insert(newPlan);
            
            // 重置Mock
            reset(rebalancePlanMapper);
        }
    }
}
