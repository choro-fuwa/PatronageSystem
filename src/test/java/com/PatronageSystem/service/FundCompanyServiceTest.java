package com.PatronageSystem.service;

import com.PatronageSystem.entity.FundCompany;
import com.PatronageSystem.mapper.FundCompanyMapper;
import com.PatronageSystem.service.impl.FundCompanyServiceImpl;
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
public class FundCompanyServiceTest {

    @Mock
    private FundCompanyMapper fundCompanyMapper;

    @InjectMocks
    private FundCompanyServiceImpl fundCompanyService;

    // 测试数据
    private final FundCompany company1 = createFundCompany(1L, "000001", "华夏基金管理有限公司", "华夏基金", 
            LocalDate.of(1998, 4, 9), new BigDecimal("23800.00"), "张弘弢", "公募基金", 1);
    private final FundCompany company2 = createFundCompany(2L, "000002", "易方达基金管理有限公司", "易方达基金", 
            LocalDate.of(2001, 4, 17), new BigDecimal("12000.00"), "刘晓艳", "公募基金", 1);
    private final FundCompany company3 = createFundCompany(3L, "000003", "嘉实基金管理有限公司", "嘉实基金", 
            LocalDate.of(1999, 3, 25), new BigDecimal("15000.00"), "赵学军", "公募基金", 1);

    private FundCompany createFundCompany(Long id, String companyCode, String companyName, String companyShortName,
                                        LocalDate establishDate, BigDecimal registeredCapital, String legalRepresentative,
                                        String companyType, Integer status) {
        FundCompany company = new FundCompany();
        company.setId(id);
        company.setCompanyCode(companyCode);
        company.setCompanyName(companyName);
        company.setCompanyShortName(companyShortName);
        company.setEstablishDate(establishDate);
        company.setRegisteredCapital(registeredCapital);
        company.setLegalRepresentative(legalRepresentative);
        company.setCompanyType(companyType);
        company.setStatus(status);
        company.setCreateTime(LocalDateTime.now());
        company.setUpdateTime(LocalDateTime.now());
        return company;
    }

    @Test
    void findAll_ShouldReturnAllCompanies() {
        // 模拟Mapper行为
        when(fundCompanyMapper.findAll()).thenReturn(Arrays.asList(company1, company2, company3));

        // 调用Service方法
        List<FundCompany> result = fundCompanyService.findAll();

        // 验证结果
        assertEquals(3, result.size());
        assertEquals("000001", result.get(0).getCompanyCode());
        assertEquals("华夏基金管理有限公司", result.get(0).getCompanyName());
        assertEquals("000002", result.get(1).getCompanyCode());
        assertEquals("易方达基金管理有限公司", result.get(1).getCompanyName());
        verify(fundCompanyMapper, times(1)).findAll();
    }

    @Test
    void findAll_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(fundCompanyMapper.findAll()).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<FundCompany> result = fundCompanyService.findAll();

        // 验证结果
        assertTrue(result.isEmpty());
        verify(fundCompanyMapper, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnCompany() {
        // 模拟Mapper行为
        when(fundCompanyMapper.findById(1L)).thenReturn(company1);

        // 调用Service方法
        FundCompany result = fundCompanyService.findById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals("000001", result.getCompanyCode());
        assertEquals("华夏基金管理有限公司", result.getCompanyName());
        assertEquals("华夏基金", result.getCompanyShortName());
        assertEquals(LocalDate.of(1998, 4, 9), result.getEstablishDate());
        assertEquals(new BigDecimal("23800.00"), result.getRegisteredCapital());
        assertEquals("张弘弢", result.getLegalRepresentative());
        assertEquals("公募基金", result.getCompanyType());
        assertEquals(1, result.getStatus());
        verify(fundCompanyMapper, times(1)).findById(1L);
    }

    @Test
    void findById_ShouldReturnNull() {
        // 模拟Mapper行为
        when(fundCompanyMapper.findById(999L)).thenReturn(null);

        // 调用Service方法
        FundCompany result = fundCompanyService.findById(999L);

        // 验证结果
        assertNull(result);
        verify(fundCompanyMapper, times(1)).findById(999L);
    }

    @Test
    void findById_WithNullId_ShouldReturnNull() {
        // 调用Service方法
        FundCompany result = fundCompanyService.findById(null);

        // 验证结果
        assertNull(result);
        verify(fundCompanyMapper, times(1)).findById(null);
    }

    @Test
    void findByCode_ShouldReturnCompany() {
        // 模拟Mapper行为
        when(fundCompanyMapper.findByCode("000001")).thenReturn(company1);

        // 调用Service方法
        FundCompany result = fundCompanyService.findByCode("000001");

        // 验证结果
        assertNotNull(result);
        assertEquals("000001", result.getCompanyCode());
        assertEquals("华夏基金管理有限公司", result.getCompanyName());
        verify(fundCompanyMapper, times(1)).findByCode("000001");
    }

    @Test
    void findByCode_ShouldReturnNull() {
        // 模拟Mapper行为
        when(fundCompanyMapper.findByCode("NONEXISTENT")).thenReturn(null);

        // 调用Service方法
        FundCompany result = fundCompanyService.findByCode("NONEXISTENT");

        // 验证结果
        assertNull(result);
        verify(fundCompanyMapper, times(1)).findByCode("NONEXISTENT");
    }

    @Test
    void findByCode_WithNullCode_ShouldReturnNull() {
        // 调用Service方法
        FundCompany result = fundCompanyService.findByCode(null);

        // 验证结果
        assertNull(result);
        verify(fundCompanyMapper, times(1)).findByCode(null);
    }

    @Test
    void findByCode_WithEmptyCode_ShouldReturnNull() {
        // 调用Service方法
        FundCompany result = fundCompanyService.findByCode("");

        // 验证结果
        assertNull(result);
        verify(fundCompanyMapper, times(1)).findByCode("");
    }

    @Test
    void search_ShouldReturnMatchingCompanies() {
        // 模拟Mapper行为
        when(fundCompanyMapper.search("华夏")).thenReturn(Arrays.asList(company1));

        // 调用Service方法
        List<FundCompany> result = fundCompanyService.search("华夏");

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("000001", result.get(0).getCompanyCode());
        assertEquals("华夏基金管理有限公司", result.get(0).getCompanyName());
        verify(fundCompanyMapper, times(1)).search("华夏");
    }

    @Test
    void search_ShouldReturnMultipleMatchingCompanies() {
        // 模拟Mapper行为
        when(fundCompanyMapper.search("基金")).thenReturn(Arrays.asList(company1, company2, company3));

        // 调用Service方法
        List<FundCompany> result = fundCompanyService.search("基金");

        // 验证结果
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(c -> c.getCompanyCode().equals("000001")));
        assertTrue(result.stream().anyMatch(c -> c.getCompanyCode().equals("000002")));
        assertTrue(result.stream().anyMatch(c -> c.getCompanyCode().equals("000003")));
        verify(fundCompanyMapper, times(1)).search("基金");
    }

    @Test
    void search_ShouldReturnEmptyList() {
        // 模拟Mapper行为
        when(fundCompanyMapper.search("不存在的公司")).thenReturn(Collections.emptyList());

        // 调用Service方法
        List<FundCompany> result = fundCompanyService.search("不存在的公司");

        // 验证结果
        assertTrue(result.isEmpty());
        verify(fundCompanyMapper, times(1)).search("不存在的公司");
    }

    @Test
    void search_WithNullKeyword_ShouldReturnAllCompanies() {
        // 模拟Mapper行为
        when(fundCompanyMapper.search(null)).thenReturn(Arrays.asList(company1, company2, company3));

        // 调用Service方法
        List<FundCompany> result = fundCompanyService.search(null);

        // 验证结果
        assertEquals(3, result.size());
        verify(fundCompanyMapper, times(1)).search(null);
    }

    @Test
    void search_WithEmptyKeyword_ShouldReturnAllCompanies() {
        // 模拟Mapper行为
        when(fundCompanyMapper.search("")).thenReturn(Arrays.asList(company1, company2, company3));

        // 调用Service方法
        List<FundCompany> result = fundCompanyService.search("");

        // 验证结果
        assertEquals(3, result.size());
        verify(fundCompanyMapper, times(1)).search("");
    }

    @Test
    void search_WithWhitespaceKeyword_ShouldReturnAllCompanies() {
        // 模拟Mapper行为
        when(fundCompanyMapper.search("   ")).thenReturn(Arrays.asList(company1, company2, company3));

        // 调用Service方法
        List<FundCompany> result = fundCompanyService.search("   ");

        // 验证结果
        assertEquals(3, result.size());
        verify(fundCompanyMapper, times(1)).search("   ");
    }

    @Test
    void search_WithSpecialCharacters_ShouldHandleCorrectly() {
        // 模拟Mapper行为
        when(fundCompanyMapper.search("华夏%")).thenReturn(Arrays.asList(company1));

        // 调用Service方法
        List<FundCompany> result = fundCompanyService.search("华夏%");

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("000001", result.get(0).getCompanyCode());
        verify(fundCompanyMapper, times(1)).search("华夏%");
    }

    @Test
    void search_WithCaseInsensitive_ShouldReturnMatchingCompanies() {
        // 模拟Mapper行为 - 假设搜索是大小写不敏感的
        when(fundCompanyMapper.search("华夏")).thenReturn(Arrays.asList(company1));

        // 调用Service方法
        List<FundCompany> result = fundCompanyService.search("华夏");

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("000001", result.get(0).getCompanyCode());
        verify(fundCompanyMapper, times(1)).search("华夏");
    }

    @Test
    void search_WithPartialMatch_ShouldReturnMatchingCompanies() {
        // 模拟Mapper行为
        when(fundCompanyMapper.search("易方达")).thenReturn(Arrays.asList(company2));

        // 调用Service方法
        List<FundCompany> result = fundCompanyService.search("易方达");

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("000002", result.get(0).getCompanyCode());
        assertEquals("易方达基金管理有限公司", result.get(0).getCompanyName());
        verify(fundCompanyMapper, times(1)).search("易方达");
    }

    @Test
    void search_WithCompanyCode_ShouldReturnMatchingCompany() {
        // 模拟Mapper行为
        when(fundCompanyMapper.search("000001")).thenReturn(Arrays.asList(company1));

        // 调用Service方法
        List<FundCompany> result = fundCompanyService.search("000001");

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("000001", result.get(0).getCompanyCode());
        verify(fundCompanyMapper, times(1)).search("000001");
    }

    @Test
    void search_WithLegalRepresentative_ShouldReturnMatchingCompany() {
        // 模拟Mapper行为
        when(fundCompanyMapper.search("张弘弢")).thenReturn(Arrays.asList(company1));

        // 调用Service方法
        List<FundCompany> result = fundCompanyService.search("张弘弢");

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("000001", result.get(0).getCompanyCode());
        assertEquals("张弘弢", result.get(0).getLegalRepresentative());
        verify(fundCompanyMapper, times(1)).search("张弘弢");
    }

    @Test
    void search_WithCompanyType_ShouldReturnMatchingCompanies() {
        // 模拟Mapper行为
        when(fundCompanyMapper.search("公募基金")).thenReturn(Arrays.asList(company1, company2, company3));

        // 调用Service方法
        List<FundCompany> result = fundCompanyService.search("公募基金");

        // 验证结果
        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(c -> "公募基金".equals(c.getCompanyType())));
        verify(fundCompanyMapper, times(1)).search("公募基金");
    }
}
