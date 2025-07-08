package com.PatronageSystem.controller;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.hamcrest.Matchers.*;

import com.PatronageSystem.common.Result;
import com.PatronageSystem.controller.FundCompanyController;
import com.PatronageSystem.entity.FundCompany;
import com.PatronageSystem.service.FundCompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

class FundCompanyTests {

    private MockMvc mockMvc;

    @Mock
    private FundCompanyService fundCompanyService;

    @InjectMocks
    private FundCompanyController fundCompanyController;

    private final FundCompany mockCompany = new FundCompany();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(fundCompanyController).build();

        // 初始化模拟数据
        mockCompany.setId(1L);
        mockCompany.setCompanyCode("F001");
        mockCompany.setCompanyName("测试基金公司");
        mockCompany.setRegisteredCapital(BigDecimal.valueOf(100000000));
    }

    // 测试获取所有基金公司
    @Test
    void testFindAll() throws Exception {
        when(fundCompanyService.findAll()).thenReturn(Arrays.asList(mockCompany));

        mockMvc.perform(get("/api/fund-company/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].companyCode", is("F001")));
    }

    // 测试根据ID查询存在的公司
    @Test
    void testFindByIdExists() throws Exception {
        when(fundCompanyService.findById(1L)).thenReturn(mockCompany);

        mockMvc.perform(get("/api/fund-company/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.data.companyName", is("测试基金公司")));
    }

    // 测试根据ID查询不存在的公司
    @Test
    void testFindByIdNotExists() throws Exception {
        when(fundCompanyService.findById(2L)).thenReturn(null);

        mockMvc.perform(get("/api/fund-company/2"))
                .andExpect(status().isOk()) // 注意：控制器返回200+错误消息
                .andExpect(jsonPath("$.code", is(500)))
                .andExpect(jsonPath("$.message", is("基金公司不存在")));
    }

    // 测试根据公司代码查询
    @Test
    void testFindByCode() throws Exception {
        when(fundCompanyService.findByCode("F001")).thenReturn(mockCompany);

        mockMvc.perform(get("/api/fund-company/code/F001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.registeredCapital", is(100000000)));
    }

    // 测试带关键字的搜索
    @Test
    void testSearchWithKeyword() throws Exception {
        when(fundCompanyService.search("测试")).thenReturn(Arrays.asList(mockCompany));

        mockMvc.perform(get("/api/fund-company/search")
                        .param("keyword", "测试"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id", is(1)));
    }

    // 测试空搜索（返回所有数据）
    @Test
    void testSearchWithoutKeyword() throws Exception {
        when(fundCompanyService.search(null)).thenReturn(Arrays.asList(mockCompany));

        mockMvc.perform(get("/api/fund-company/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)));
    }
}