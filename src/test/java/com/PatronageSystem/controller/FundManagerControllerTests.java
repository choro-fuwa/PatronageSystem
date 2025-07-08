package com.PatronageSystem.controller;

import com.PatronageSystem.common.Result;
import com.PatronageSystem.controller.FundManagerController;
import com.PatronageSystem.entity.FundManager;
import com.PatronageSystem.service.FundManagerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FundManagerController.class)
class FundManagerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FundManagerService fundManagerService;

    private final FundManager mockManager = createMockManager();

    private FundManager createMockManager() {
        FundManager manager = new FundManager();
        manager.setId(1L);
        manager.setManagerCode("M001");
        manager.setManagerName("张经理");
        manager.setCompanyId(1001L);
        manager.setEducation("金融学硕士");
        manager.setExperienceYears(8);
        manager.setIntroduction("资深基金经理");
        manager.setStatus(1);
        manager.setCreateTime(LocalDateTime.now());
        manager.setUpdateTime(LocalDateTime.now());
        manager.setCompanyName("华夏基金");
        return manager;
    }

    // 测试获取所有基金经理
    @Test
    void findAll_shouldReturnManagerList() throws Exception {
        List<FundManager> managers = Collections.singletonList(mockManager);
        when(fundManagerService.findAll()).thenReturn(managers);

        mockMvc.perform(get("/api/fund-manager/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].managerCode").value("M001"))
                .andExpect(jsonPath("$.data[0].managerName").value("张经理"));
    }

    // 测试通过ID查询存在的基金经理
    @Test
    void findById_whenExists_shouldReturnManager() throws Exception {
        when(fundManagerService.findById(1L)).thenReturn(mockManager);

        mockMvc.perform(get("/api/fund-manager/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.managerCode").value("M001"))
                .andExpect(jsonPath("$.data.companyName").value("华夏基金"));
    }

    // 修正：匹配 message 字段而不是 msg
    @Test
    void findById_whenNotExists_shouldReturnError() throws Exception {
        when(fundManagerService.findById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/fund-manager/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("基金经理不存在")) // 修正这里
                .andExpect(jsonPath("$.code").value(500)); // 可以同时验证错误码
    }

    // 测试通过经理代码查询
    @Test
    void findByCode_shouldReturnManager() throws Exception {
        when(fundManagerService.findByCode("M001")).thenReturn(mockManager);

        mockMvc.perform(get("/api/fund-manager/code/M001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.experienceYears").value(8));
    }

    // 测试通过公司ID查询
    @Test
    void findByCompanyId_shouldReturnManagers() throws Exception {
        List<FundManager> managers = Arrays.asList(mockManager, mockManager);
        when(fundManagerService.findByCompanyId(1001L)).thenReturn(managers);

        mockMvc.perform(get("/api/fund-manager/company/1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].education").value("金融学硕士"));
    }

    // 测试关键字搜索
    @Test
    void search_withKeyword_shouldReturnResults() throws Exception {
        List<FundManager> managers = Collections.singletonList(mockManager);
        when(fundManagerService.search("张")).thenReturn(managers);

        mockMvc.perform(get("/api/fund-manager/search")
                        .param("keyword", "张"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].managerName").value("张经理"));
    }

    // 测试空关键字搜索
    @Test
    void search_withoutKeyword_shouldReturnAll() throws Exception {
        List<FundManager> managers = Arrays.asList(mockManager, mockManager);
        when(fundManagerService.search(null)).thenReturn(managers);

        mockMvc.perform(get("/api/fund-manager/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2));
    }
}