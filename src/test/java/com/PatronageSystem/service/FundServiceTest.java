package com.PatronageSystem.service;

import com.PatronageSystem.entity.Fund;
import com.PatronageSystem.mapper.FundMapper;
import com.PatronageSystem.service.impl.FundServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FundServiceTest {

    @Mock
    private FundMapper fundMapper;

    @InjectMocks
    private FundServiceImpl fundService;

    // 测试数据
    private final Fund fund1 = createFund(1L, "000001", "华夏成长混合", "华夏成长", 1L, 1L, 
            "混合型", "中风险", LocalDate.of(2001, 12, 18), new BigDecimal("5000000000.00"), 
            new BigDecimal("1.2345"), LocalDate.now(), new BigDecimal("0.1567"), 
            new BigDecimal("0.0890"), new BigDecimal("0.1234"), new BigDecimal("1.2345"), 1,
            "华夏基金管理有限公司", "张弘弢", Arrays.asList("成长", "混合", "明星基金"));
    
    private final Fund fund2 = createFund(2L, "000002", "易方达消费行业股票", "易方达消费", 2L, 2L, 
            "股票型", "高风险", LocalDate.of(2010, 8, 20), new BigDecimal("3000000000.00"), 
            new BigDecimal("2.5678"), LocalDate.now(), new BigDecimal("0.2345"), 
            new BigDecimal("0.1234"), new BigDecimal("0.1567"), new BigDecimal("1.5678"), 1,
            "易方达基金管理有限公司", "刘晓艳", Arrays.asList("消费", "股票", "行业基金"));
    
    private final Fund fund3 = createFund(3L, "000003", "嘉实增长混合", "嘉实增长", 3L, 3L, 
            "混合型", "中风险", LocalDate.of(2003, 7, 9), new BigDecimal("2000000000.00"), 
            new BigDecimal("3.4567"), LocalDate.now(), new BigDecimal("0.3456"), 
            new BigDecimal("0.1567"), new BigDecimal("0.1890"), new BigDecimal("1.7890"), 1,
            "嘉实基金管理有限公司", "赵学军", Arrays.asList("增长", "混合", "价值投资"));

    private Fund createFund(Long id, String fundCode, String fundName, String fundShortName, 
                           Long companyId, Long managerId, String fundType, String riskLevel,
                           LocalDate establishDate, BigDecimal fundSize, BigDecimal nav, 
                           LocalDate navDate, BigDecimal totalReturn, BigDecimal annualReturn,
                           BigDecimal maxDrawdown, BigDecimal sharpeRatio, Integer status,
                           String companyName, String managerName, List<String> tags) {
        Fund fund = new Fund();
        fund.setId(id);
        fund.setFundCode(fundCode);
        fund.setFundName(fundName);
        fund.setFundShortName(fundShortName);
        fund.setCompanyId(companyId);
        fund.setManagerId(managerId);
        fund.setFundType(fundType);
        fund.setRiskLevel(riskLevel);
        fund.setEstablishDate(establishDate);
        fund.setFundSize(fundSize);
        fund.setNav(nav);
        fund.setNavDate(navDate);
        fund.setTotalReturn(totalReturn);
        fund.setAnnualReturn(annualReturn);
        fund.setMaxDrawdown(maxDrawdown);
        fund.setSharpeRatio(sharpeRatio);
        fund.setStatus(status);
        fund.setCompanyName(companyName);
        fund.setManagerName(managerName);
        fund.setTags(tags);
        fund.setCreateTime(LocalDateTime.now());
        fund.setUpdateTime(LocalDateTime.now());
        return fund;
    }

    @Test
    void findAll_ShouldReturnAllFundsWithTags() {
        // 模拟Mapper行为
        when(fundMapper.findAll()).thenReturn(Arrays.asList(fund1, fund2, fund3));
        when(fundMapper.getFundTags(1L)).thenReturn(Arrays.asList("成长", "混合", "明星基金"));
        when(fundMapper.getFundTags(2L)).thenReturn(Arrays.asList("消费", "股票", "行业基金"));
        when(fundMapper.getFundTags(3L)).thenReturn(Arrays.asList("增长", "混合", "价值投资"));

        // 调用Service方法
        List<Fund> result = fundService.findAll();

        // 验证结果
        assertEquals(3, result.size());
        assertEquals("000001", result.get(0).getFundCode());
        assertEquals("华夏成长混合", result.get(0).getFundName());
        assertEquals(3, result.get(0).getTags().size());
        assertTrue(result.get(0).getTags().contains("成长"));
        assertEquals("000002", result.get(1).getFundCode());
        assertEquals("易方达消费行业股票", result.get(1).getFundName());
        assertEquals(3, result.get(1).getTags().size());
        assertTrue(result.get(1).getTags().contains("消费"));
        verify(fundMapper, times(1)).findAll();
        verify(fundMapper, times(1)).getFundTags(1L);
        verify(fundMapper, times(1)).getFundTags(2L);
        verify(fundMapper, times(1)).getFundTags(3L);
    }

    @Test
    void findAll_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(fundMapper.findAll()).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<Fund> result = fundService.findAll();

        // 验证结果
        assertTrue(result.isEmpty());
        verify(fundMapper, times(1)).findAll();
        verify(fundMapper, never()).getFundTags(anyLong());
    }

    @Test
    void findById_ShouldReturnFundWithTags() {
        // 模拟Mapper行为
        when(fundMapper.findById(1L)).thenReturn(fund1);
        when(fundMapper.getFundTags(1L)).thenReturn(Arrays.asList("成长", "混合", "明星基金"));

        // 调用Service方法
        Fund result = fundService.findById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals("000001", result.getFundCode());
        assertEquals("华夏成长混合", result.getFundName());
        assertEquals("华夏基金管理有限公司", result.getCompanyName());
        assertEquals("张弘弢", result.getManagerName());
        assertEquals(3, result.getTags().size());
        assertTrue(result.getTags().contains("成长"));
        assertTrue(result.getTags().contains("混合"));
        assertTrue(result.getTags().contains("明星基金"));
        verify(fundMapper, times(1)).findById(1L);
        verify(fundMapper, times(1)).getFundTags(1L);
    }

    @Test
    void findById_ShouldReturnNull() {
        // 模拟Mapper行为
        when(fundMapper.findById(999L)).thenReturn(null);

        // 调用Service方法
        Fund result = fundService.findById(999L);

        // 验证结果
        assertNull(result);
        verify(fundMapper, times(1)).findById(999L);
        verify(fundMapper, never()).getFundTags(anyLong());
    }

    @Test
    void findById_WithNullId_ShouldReturnNull() {
        // 调用Service方法
        Fund result = fundService.findById(null);

        // 验证结果
        assertNull(result);
        verify(fundMapper, times(1)).findById(null);
        verify(fundMapper, never()).getFundTags(anyLong());
    }

    @Test
    void findByCode_ShouldReturnFundWithTags() {
        // 模拟Mapper行为
        when(fundMapper.findByCode("000001")).thenReturn(fund1);
        when(fundMapper.getFundTags(1L)).thenReturn(Arrays.asList("成长", "混合", "明星基金"));

        // 调用Service方法
        Fund result = fundService.findByCode("000001");

        // 验证结果
        assertNotNull(result);
        assertEquals("000001", result.getFundCode());
        assertEquals("华夏成长混合", result.getFundName());
        assertEquals(3, result.getTags().size());
        verify(fundMapper, times(1)).findByCode("000001");
        verify(fundMapper, times(1)).getFundTags(1L);
    }

    @Test
    void findByCode_ShouldReturnNull() {
        // 模拟Mapper行为
        when(fundMapper.findByCode("NONEXISTENT")).thenReturn(null);

        // 调用Service方法
        Fund result = fundService.findByCode("NONEXISTENT");

        // 验证结果
        assertNull(result);
        verify(fundMapper, times(1)).findByCode("NONEXISTENT");
        verify(fundMapper, never()).getFundTags(anyLong());
    }

    @Test
    void findByCode_WithNullCode_ShouldReturnNull() {
        // 调用Service方法
        Fund result = fundService.findByCode(null);

        // 验证结果
        assertNull(result);
        verify(fundMapper, times(1)).findByCode(null);
        verify(fundMapper, never()).getFundTags(anyLong());
    }

    @Test
    void findByCode_WithEmptyCode_ShouldReturnNull() {
        // 调用Service方法
        Fund result = fundService.findByCode("");

        // 验证结果
        assertNull(result);
        verify(fundMapper, times(1)).findByCode("");
        verify(fundMapper, never()).getFundTags(anyLong());
    }

    @Test
    void findByCompanyId_ShouldReturnFundsWithTags() {
        // 模拟Mapper行为
        when(fundMapper.findByCompanyId(1L)).thenReturn(Arrays.asList(fund1));
        when(fundMapper.getFundTags(1L)).thenReturn(Arrays.asList("成长", "混合", "明星基金"));

        // 调用Service方法
        List<Fund> result = fundService.findByCompanyId(1L);

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("000001", result.get(0).getFundCode());
        assertEquals("华夏成长混合", result.get(0).getFundName());
        assertEquals(1L, result.get(0).getCompanyId());
        assertEquals("华夏基金管理有限公司", result.get(0).getCompanyName());
        assertEquals(3, result.get(0).getTags().size());
        verify(fundMapper, times(1)).findByCompanyId(1L);
        verify(fundMapper, times(1)).getFundTags(1L);
    }

    @Test
    void findByCompanyId_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(fundMapper.findByCompanyId(999L)).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<Fund> result = fundService.findByCompanyId(999L);

        // 验证结果
        assertTrue(result.isEmpty());
        verify(fundMapper, times(1)).findByCompanyId(999L);
        verify(fundMapper, never()).getFundTags(anyLong());
    }

    @Test
    void findByCompanyId_WithNullCompanyId_ShouldReturnEmptyList() {
        // 调用Service方法
        List<Fund> result = fundService.findByCompanyId(null);

        // 验证结果
        assertTrue(result.isEmpty());
        verify(fundMapper, times(1)).findByCompanyId(null);
        verify(fundMapper, never()).getFundTags(anyLong());
    }

    @Test
    void findByManagerId_ShouldReturnFundsWithTags() {
        // 模拟Mapper行为
        when(fundMapper.findByManagerId(1L)).thenReturn(Arrays.asList(fund1));
        when(fundMapper.getFundTags(1L)).thenReturn(Arrays.asList("成长", "混合", "明星基金"));

        // 调用Service方法
        List<Fund> result = fundService.findByManagerId(1L);

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("000001", result.get(0).getFundCode());
        assertEquals("华夏成长混合", result.get(0).getFundName());
        assertEquals(1L, result.get(0).getManagerId());
        assertEquals("张弘弢", result.get(0).getManagerName());
        assertEquals(3, result.get(0).getTags().size());
        verify(fundMapper, times(1)).findByManagerId(1L);
        verify(fundMapper, times(1)).getFundTags(1L);
    }

    @Test
    void findByManagerId_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(fundMapper.findByManagerId(999L)).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<Fund> result = fundService.findByManagerId(999L);

        // 验证结果
        assertTrue(result.isEmpty());
        verify(fundMapper, times(1)).findByManagerId(999L);
        verify(fundMapper, never()).getFundTags(anyLong());
    }

    @Test
    void findByManagerId_WithNullManagerId_ShouldReturnEmptyList() {
        // 调用Service方法
        List<Fund> result = fundService.findByManagerId(null);

        // 验证结果
        assertTrue(result.isEmpty());
        verify(fundMapper, times(1)).findByManagerId(null);
        verify(fundMapper, never()).getFundTags(anyLong());
    }

    @Test
    void search_ShouldReturnFundsWithTags() {
        // 模拟Mapper行为
        when(fundMapper.search("华夏", null, null, null)).thenReturn(Arrays.asList(fund1));
        when(fundMapper.getFundTags(1L)).thenReturn(Arrays.asList("成长", "混合", "明星基金"));

        // 调用Service方法
        List<Fund> result = fundService.search("华夏", null, null, null);

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("000001", result.get(0).getFundCode());
        assertEquals("华夏成长混合", result.get(0).getFundName());
        assertEquals(3, result.get(0).getTags().size());
        verify(fundMapper, times(1)).search("华夏", null, null, null);
        verify(fundMapper, times(1)).getFundTags(1L);
    }

    @Test
    void search_WithAllParameters_ShouldReturnFundsWithTags() {
        // 模拟Mapper行为
        when(fundMapper.search("消费", "股票型", "高风险", Arrays.asList(1L, 2L)))
                .thenReturn(Arrays.asList(fund2));
        when(fundMapper.getFundTags(2L)).thenReturn(Arrays.asList("消费", "股票", "行业基金"));

        // 调用Service方法
        List<Fund> result = fundService.search("消费", "股票型", "高风险", Arrays.asList(1L, 2L));

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("000002", result.get(0).getFundCode());
        assertEquals("易方达消费行业股票", result.get(0).getFundName());
        assertEquals("股票型", result.get(0).getFundType());
        assertEquals("高风险", result.get(0).getRiskLevel());
        assertEquals(3, result.get(0).getTags().size());
        verify(fundMapper, times(1)).search("消费", "股票型", "高风险", Arrays.asList(1L, 2L));
        verify(fundMapper, times(1)).getFundTags(2L);
    }

    @Test
    void search_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(fundMapper.search("不存在的基金", null, null, null)).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<Fund> result = fundService.search("不存在的基金", null, null, null);

        // 验证结果
        assertTrue(result.isEmpty());
        verify(fundMapper, times(1)).search("不存在的基金", null, null, null);
        verify(fundMapper, never()).getFundTags(anyLong());
    }

    @Test
    void search_WithNullParameters_ShouldReturnAllFundsWithTags() {
        // 模拟Mapper行为
        when(fundMapper.search(null, null, null, null)).thenReturn(Arrays.asList(fund1, fund2, fund3));
        when(fundMapper.getFundTags(1L)).thenReturn(Arrays.asList("成长", "混合", "明星基金"));
        when(fundMapper.getFundTags(2L)).thenReturn(Arrays.asList("消费", "股票", "行业基金"));
        when(fundMapper.getFundTags(3L)).thenReturn(Arrays.asList("增长", "混合", "价值投资"));

        // 调用Service方法
        List<Fund> result = fundService.search(null, null, null, null);

        // 验证结果
        assertEquals(3, result.size());
        verify(fundMapper, times(1)).search(null, null, null, null);
        verify(fundMapper, times(1)).getFundTags(1L);
        verify(fundMapper, times(1)).getFundTags(2L);
        verify(fundMapper, times(1)).getFundTags(3L);
    }

    @Test
    void search_WithFundTypeFilter_ShouldReturnMatchingFunds() {
        // 模拟Mapper行为
        when(fundMapper.search(null, "混合型", null, null)).thenReturn(Arrays.asList(fund1, fund3));
        when(fundMapper.getFundTags(1L)).thenReturn(Arrays.asList("成长", "混合", "明星基金"));
        when(fundMapper.getFundTags(3L)).thenReturn(Arrays.asList("增长", "混合", "价值投资"));

        // 调用Service方法
        List<Fund> result = fundService.search(null, "混合型", null, null);

        // 验证结果
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(f -> "混合型".equals(f.getFundType())));
        verify(fundMapper, times(1)).search(null, "混合型", null, null);
        verify(fundMapper, times(1)).getFundTags(1L);
        verify(fundMapper, times(1)).getFundTags(3L);
    }

    @Test
    void search_WithRiskLevelFilter_ShouldReturnMatchingFunds() {
        // 模拟Mapper行为
        when(fundMapper.search(null, null, "高风险", null)).thenReturn(Arrays.asList(fund2));
        when(fundMapper.getFundTags(2L)).thenReturn(Arrays.asList("消费", "股票", "行业基金"));

        // 调用Service方法
        List<Fund> result = fundService.search(null, null, "高风险", null);

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("高风险", result.get(0).getRiskLevel());
        verify(fundMapper, times(1)).search(null, null, "高风险", null);
        verify(fundMapper, times(1)).getFundTags(2L);
    }

    @Test
    void search_WithTagIdsFilter_ShouldReturnMatchingFunds() {
        // 模拟Mapper行为
        when(fundMapper.search(null, null, null, Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(fund1, fund2));
        when(fundMapper.getFundTags(1L)).thenReturn(Arrays.asList("成长", "混合", "明星基金"));
        when(fundMapper.getFundTags(2L)).thenReturn(Arrays.asList("消费", "股票", "行业基金"));

        // 调用Service方法
        List<Fund> result = fundService.search(null, null, null, Arrays.asList(1L, 2L));

        // 验证结果
        assertEquals(2, result.size());
        verify(fundMapper, times(1)).search(null, null, null, Arrays.asList(1L, 2L));
        verify(fundMapper, times(1)).getFundTags(1L);
        verify(fundMapper, times(1)).getFundTags(2L);
    }

    @Test
    void getFundTags_ShouldReturnTags() {
        // 模拟Mapper行为
        when(fundMapper.getFundTags(1L)).thenReturn(Arrays.asList("成长", "混合", "明星基金"));

        // 调用Service方法
        List<String> result = fundService.getFundTags(1L);

        // 验证结果
        assertEquals(3, result.size());
        assertTrue(result.contains("成长"));
        assertTrue(result.contains("混合"));
        assertTrue(result.contains("明星基金"));
        verify(fundMapper, times(1)).getFundTags(1L);
    }

    @Test
    void getFundTags_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(fundMapper.getFundTags(999L)).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<String> result = fundService.getFundTags(999L);

        // 验证结果
        assertTrue(result.isEmpty());
        verify(fundMapper, times(1)).getFundTags(999L);
    }

    @Test
    void getFundTags_WithNullFundId_ShouldReturnEmptyList() {
        // 调用Service方法
        List<String> result = fundService.getFundTags(null);

        // 验证结果
        assertTrue(result.isEmpty());
        verify(fundMapper, times(1)).getFundTags(null);
    }

    @Test
    void findByCompanyId_WithMultipleFunds_ShouldReturnAllFundsWithTags() {
        // 模拟Mapper行为
        when(fundMapper.findByCompanyId(1L)).thenReturn(Arrays.asList(fund1));
        when(fundMapper.getFundTags(1L)).thenReturn(Arrays.asList("成长", "混合", "明星基金"));

        // 调用Service方法
        List<Fund> result = fundService.findByCompanyId(1L);

        // 验证结果
        assertEquals(1, result.size());
        assertTrue(result.stream().allMatch(f -> f.getCompanyId().equals(1L)));
        assertEquals("华夏基金管理有限公司", result.get(0).getCompanyName());
        assertEquals(3, result.get(0).getTags().size());
        verify(fundMapper, times(1)).findByCompanyId(1L);
        verify(fundMapper, times(1)).getFundTags(1L);
    }

    @Test
    void findByManagerId_WithMultipleFunds_ShouldReturnAllFundsWithTags() {
        // 模拟Mapper行为
        when(fundMapper.findByManagerId(1L)).thenReturn(Arrays.asList(fund1));
        when(fundMapper.getFundTags(1L)).thenReturn(Arrays.asList("成长", "混合", "明星基金"));

        // 调用Service方法
        List<Fund> result = fundService.findByManagerId(1L);

        // 验证结果
        assertEquals(1, result.size());
        assertTrue(result.stream().allMatch(f -> f.getManagerId().equals(1L)));
        assertEquals("张弘弢", result.get(0).getManagerName());
        assertEquals(3, result.get(0).getTags().size());
        verify(fundMapper, times(1)).findByManagerId(1L);
        verify(fundMapper, times(1)).getFundTags(1L);
    }

    @Test
    void search_WithComplexCriteria_ShouldReturnMatchingFunds() {
        // 模拟Mapper行为
        when(fundMapper.search("华夏", "混合型", "中风险", Arrays.asList(1L)))
                .thenReturn(Arrays.asList(fund1));
        when(fundMapper.getFundTags(1L)).thenReturn(Arrays.asList("成长", "混合", "明星基金"));

        // 调用Service方法
        List<Fund> result = fundService.search("华夏", "混合型", "中风险", Arrays.asList(1L));

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("000001", result.get(0).getFundCode());
        assertEquals("混合型", result.get(0).getFundType());
        assertEquals("中风险", result.get(0).getRiskLevel());
        assertEquals(3, result.get(0).getTags().size());
        verify(fundMapper, times(1)).search("华夏", "混合型", "中风险", Arrays.asList(1L));
        verify(fundMapper, times(1)).getFundTags(1L);
    }
}
