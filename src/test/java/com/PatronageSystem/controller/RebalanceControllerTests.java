package com.PatronageSystem.controller;

import com.PatronageSystem.Patronage.controller.RebalanceController;
import com.PatronageSystem.Patronage.entity.RebalancePlan;
import com.PatronageSystem.Patronage.service.RebalancePlanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RebalanceController.class)
public class RebalanceControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RebalancePlanService rebalancePlanService;

    @Autowired
    private ObjectMapper objectMapper;

    private RebalancePlan createTestPlan() {
        RebalancePlan plan = new RebalancePlan();
        plan.setId(1L);
        plan.setPlanName("Test Plan");
        plan.setPortfolioName("Portfolio A");
        plan.setPortfolioType("Type X");
        plan.setCurrentHoldings("{}");
        plan.setRebalanceInstructions("[]");
        plan.setStatus("PENDING");
        plan.setCreateTime(LocalDateTime.now());
        plan.setUpdateTime(LocalDateTime.now());
        return plan;
    }

    @Test
    void createRebalancePlan_Success() throws Exception {
        RebalancePlan plan = createTestPlan();
        Mockito.when(rebalancePlanService.createRebalancePlan(plan)).thenReturn(1);

        mockMvc.perform(post("/api/trade/rebalance/plan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(plan)))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
    }

    @Test
    void getRebalancePlanById_Found() throws Exception {
        RebalancePlan plan = createTestPlan();
        Mockito.when(rebalancePlanService.getRebalancePlanById(1L)).thenReturn(plan);

        mockMvc.perform(get("/api/trade/rebalance/plan/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getRebalancePlanById_NotFound() throws Exception {
        Mockito.when(rebalancePlanService.getRebalancePlanById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/trade/rebalance/plan/99"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void getAllRebalancePlans() throws Exception {
        RebalancePlan plan = createTestPlan();
        Mockito.when(rebalancePlanService.getAllRebalancePlans()).thenReturn(Collections.singletonList(plan));

        mockMvc.perform(get("/api/trade/rebalance/plan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void updateRebalancePlan_Success() throws Exception {
        RebalancePlan plan = createTestPlan();
        Mockito.when(rebalancePlanService.updateRebalancePlan(plan)).thenReturn(1);

        mockMvc.perform(put("/api/trade/rebalance/plan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(plan)))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
    }

    @Test
    void deleteRebalancePlan_Success() throws Exception {
        Mockito.when(rebalancePlanService.deleteRebalancePlan(1L)).thenReturn(1);

        mockMvc.perform(delete("/api/trade/rebalance/plan/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
    }

    @Test
    void createPlanAndGenerateInstructions() throws Exception {
        RebalancePlan inputPlan = createTestPlan();
        inputPlan.setRebalanceInstructions(null); // 确保初始无指令
        inputPlan.setStatus(null);

        // 模拟Service调用并验证指令生成逻辑
        Mockito.when(rebalancePlanService.createRebalancePlan(Mockito.any()))
                .thenAnswer(invocation -> {
                    RebalancePlan savedPlan = invocation.getArgument(0);
                    return 1; // 模拟保存成功
                });

        mockMvc.perform(post("/api/trade/rebalance/plan/with-instructions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputPlan)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rebalanceInstructions").exists())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void dispatchInstructions_Success() throws Exception {
        RebalancePlan existingPlan = createTestPlan();
        existingPlan.setStatus("PENDING");

        Mockito.when(rebalancePlanService.getRebalancePlanById(1L)).thenReturn(existingPlan);
        Mockito.when(rebalancePlanService.updateRebalancePlan(Mockito.any())).thenReturn(1);

        mockMvc.perform(post("/api/trade/rebalance/plan/1/dispatch"))
                .andExpect(status().isOk())
                .andExpect(content().string("dispatched"));
    }

    @Test
    void dispatchInstructions_NotFound() throws Exception {
        Mockito.when(rebalancePlanService.getRebalancePlanById(99L)).thenReturn(null);

        mockMvc.perform(post("/api/trade/rebalance/plan/99/dispatch"))
                .andExpect(status().isOk())
                .andExpect(content().string("plan not found"));
    }
}