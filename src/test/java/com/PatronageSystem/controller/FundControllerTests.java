package com.PatronageSystem.controller;

import com.PatronageSystem.common.Result;
import com.PatronageSystem.controller.FundController;
import com.PatronageSystem.entity.Fund;
import com.PatronageSystem.service.FundService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FundControllerTests {

    private MockMvc mockMvc;

    @Mock
    private FundService fundService;

    @InjectMocks
    private FundController fundController;

    private Fund testFund;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(fundController).build();

        testFund = new Fund();
        testFund.setId(1L);
        testFund.setFundCode("F001");
        testFund.setFundName("测试基金");
        testFund.setFundShortName("TST");
        testFund.setCompanyId(101L);
        testFund.setManagerId(201L);
        testFund.setFundType("股票型");
        testFund.setRiskLevel("高风险");
        testFund.setEstablishDate(LocalDate.now());
        testFund.setFundSize(BigDecimal.valueOf(100000000));
        testFund.setNav(BigDecimal.valueOf(1.25));
        testFund.setNavDate(LocalDate.now());
        testFund.setTotalReturn(BigDecimal.valueOf(15.5));
        testFund.setAnnualReturn(BigDecimal.valueOf(12.3));
        testFund.setMaxDrawdown(BigDecimal.valueOf(-8.7));
        testFund.setSharpeRatio(BigDecimal.valueOf(1.8));
        testFund.setStatus(1);
        testFund.setCompanyName("测试公司");
        testFund.setManagerName("张经理");
        testFund.setTags(Arrays.asList("科技", "成长"));
    }

    @Test
    void findAll_ShouldReturnAllFunds() throws Exception {
        List<Fund> funds = Collections.singletonList(testFund);
        when(fundService.findAll()).thenReturn(funds);

        mockMvc.perform(get("/api/fund/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].fundCode", is("F001")));
    }

    @Test
    void findById_WhenFundExists_ShouldReturnFund() throws Exception {
        when(fundService.findById(1L)).thenReturn(testFund);

        mockMvc.perform(get("/api/fund/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.data.id", is(1)));
    }

    @Test
    void findById_WhenFundNotExists_ShouldReturnError() throws Exception {
        when(fundService.findById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/fund/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(500)))
                .andExpect(jsonPath("$.message", is("基金不存在"))); // 修复为 message
    }

    @Test
    void findByCode_WhenFundExists_ShouldReturnFund() throws Exception {
        when(fundService.findByCode("F001")).thenReturn(testFund);

        mockMvc.perform(get("/api/fund/code/F001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.data.fundCode", is("F001")));
    }

    @Test
    void findByCompanyId_ShouldReturnCompanyFunds() throws Exception {
        List<Fund> funds = Collections.singletonList(testFund);
        when(fundService.findByCompanyId(101L)).thenReturn(funds);

        mockMvc.perform(get("/api/fund/company/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].companyId", is(101)));
    }

    @Test
    void findByManagerId_ShouldReturnManagerFunds() throws Exception {
        List<Fund> funds = Collections.singletonList(testFund);
        when(fundService.findByManagerId(201L)).thenReturn(funds);

        mockMvc.perform(get("/api/fund/manager/201"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.data", hasSize(1)));
    }

    @Test
    void search_WithParameters_ShouldReturnFilteredFunds() throws Exception {
        List<Fund> funds = Collections.singletonList(testFund);
        when(fundService.search(eq("科技"), eq("股票型"), eq("高风险"), anyList()))
                .thenReturn(funds);

        mockMvc.perform(get("/api/fund/search")
                        .param("keyword", "科技")
                        .param("fundType", "股票型")
                        .param("riskLevel", "高风险")
                        .param("tagIds", "1", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.data", hasSize(1)));
    }

    @Test
    void getFundTags_ShouldReturnFundTags() throws Exception {
        List<String> tags = Arrays.asList("科技", "成长");
        when(fundService.getFundTags(1L)).thenReturn(tags);

        mockMvc.perform(get("/api/fund/1/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    void search_WithPartialParameters_ShouldHandleNulls() throws Exception {
        List<Fund> funds = Collections.singletonList(testFund);
        when(fundService.search(eq(null), eq(null), eq(null), eq(null)))
                .thenReturn(funds);

        mockMvc.perform(get("/api/fund/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.data", hasSize(1)));
    }
}