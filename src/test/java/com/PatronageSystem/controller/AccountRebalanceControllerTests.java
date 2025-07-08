package com.PatronageSystem.controller;

import com.PatronageSystem.Patronage.controller.AccountRebalanceController;
import com.PatronageSystem.Patronage.entity.Position;
import com.PatronageSystem.Patronage.entity.RebalancePlan;
import com.PatronageSystem.Patronage.service.PositionService;
import com.PatronageSystem.Patronage.service.RebalancePlanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AccountRebalanceControllerTests {

    @Mock
    private PositionService positionService;

    @Mock
    private RebalancePlanService rebalancePlanService;

    @InjectMocks
    private AccountRebalanceController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetAccountPositions_Success() throws Exception {
        // 初始化MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        // 模拟数据
        Long accountId = 1L;
        List<Position> positions = Arrays.asList(
                createPosition(1L, 10L),
                createPosition(2L, 20L)
        );

        // 模拟服务行为
        when(positionService.getPositionsByAccountId(accountId)).thenReturn(positions);

        // 执行请求并验证
        mockMvc.perform(get("/api/trade/account/{accountId}/positions", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(positionService, times(1)).getPositionsByAccountId(accountId);
    }

    @Test
    void testGetAccountPositions_Empty() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        Long accountId = 1L;

        when(positionService.getPositionsByAccountId(accountId)).thenReturn(List.of());

        mockMvc.perform(get("/api/trade/account/{accountId}/positions", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testCreateAccountRebalance_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        RebalancePlan plan = new RebalancePlan();
        plan.setPlanName("Test Plan");

        // 模拟服务返回插入行数
        when(rebalancePlanService.createRebalancePlan(any(RebalancePlan.class))).thenReturn(1);

        mockMvc.perform(post("/api/trade/account/rebalance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(plan)))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        // 验证状态被设置为PENDING
        verify(rebalancePlanService).createRebalancePlan(argThat(p ->
                "PENDING".equals(p.getStatus())
        ));
    }

    @Test
    void testCreateAccountRebalance_Failure() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        RebalancePlan plan = new RebalancePlan();
        when(rebalancePlanService.createRebalancePlan(any())).thenReturn(0);

        mockMvc.perform(post("/api/trade/account/rebalance")
                        .content(objectMapper.writeValueAsString(plan))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
    }

    // 辅助方法：创建Position对象
    private Position createPosition(Long id, Long fundId) {
        Position position = new Position();
        position.setId(id);
        position.setAccountId(1L);
        position.setFundId(fundId);
        position.setTotalQuantity(BigDecimal.valueOf(100));
        position.setAvailableQuantity(BigDecimal.valueOf(100));
        position.setFrozenQuantity(BigDecimal.ZERO);
        position.setUpdateTime(LocalDateTime.now());
        return position;
    }
}