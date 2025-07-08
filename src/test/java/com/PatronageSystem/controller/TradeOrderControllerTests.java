package com.PatronageSystem.controller;

import com.PatronageSystem.Patronage.controller.TradeOrderController;
import com.PatronageSystem.Patronage.entity.TradeOrder;
import com.PatronageSystem.Patronage.service.TradeOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TradeOrderControllerTests {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private TradeOrderService tradeOrderService;

    @InjectMocks
    private TradeOrderController tradeOrderController;

    private TradeOrder testOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 配置ObjectMapper支持Java 8日期类型
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders.standaloneSetup(tradeOrderController).build();

        // 创建完整的测试订单对象
        testOrder = new TradeOrder();
        testOrder.setId(1L);
        testOrder.setOrderCode("ORDER123");
        testOrder.setAccountId(1001L);
        testOrder.setStrategyId(2001L);
        testOrder.setFundId(3001L);
        testOrder.setOrderType("LIMIT");
        testOrder.setOrderStatus("PENDING");
        testOrder.setOrderBizType("BUY");
        testOrder.setOrderPrice(BigDecimal.valueOf(100.50));
        testOrder.setOrderQuantity(BigDecimal.valueOf(10));
        testOrder.setFilledQuantity(BigDecimal.ZERO);
        testOrder.setFilledAmount(BigDecimal.ZERO);
        testOrder.setAvgFillPrice(BigDecimal.ZERO);
        testOrder.setCommission(BigDecimal.ZERO);
        testOrder.setOrderTime(LocalDateTime.of(2023, 1, 1, 10, 0));
        testOrder.setExpireTime(LocalDateTime.of(2023, 1, 2, 10, 0));
        testOrder.setCreateTime(LocalDateTime.now());
        testOrder.setUpdateTime(LocalDateTime.now());
    }

    @Test
    void getAllOrders_ShouldReturnOrders() throws Exception {
        when(tradeOrderService.getAllOrders()).thenReturn(Collections.singletonList(testOrder));

        mockMvc.perform(get("/api/trade/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getOrderById_ShouldReturnOrder() throws Exception {
        when(tradeOrderService.getOrderById(1L)).thenReturn(testOrder);

        mockMvc.perform(get("/api/trade/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderCode").value("ORDER123"));
    }

    @Test
    void getOrderById_NotFound_ShouldReturn404() throws Exception {
        when(tradeOrderService.getOrderById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/api/trade/orders/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createOrder_ShouldReturnCreatedOrder() throws Exception {
        when(tradeOrderService.createOrder(any(TradeOrder.class))).thenReturn(testOrder);

        mockMvc.perform(post("/api/trade/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void updateOrder_ShouldReturnUpdatedOrder() throws Exception {
        when(tradeOrderService.updateOrder(any(TradeOrder.class))).thenReturn(testOrder);

        mockMvc.perform(put("/api/trade/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderCode").value("ORDER123"));
    }

    @Test
    void deleteOrder_ShouldReturnOk() throws Exception {
        when(tradeOrderService.deleteOrder(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/trade/orders/1"))
                .andExpect(status().isOk());
    }

    @Test
    void executeOrder_Success_ShouldReturnSuccessResponse() throws Exception {
        when(tradeOrderService.executeOrder(1L)).thenReturn(true);

        mockMvc.perform(post("/api/trade/orders/1/execute"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("订单执行成功"));
    }

    @Test
    void cancelOrder_ShouldReturnSuccess() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("cancelReason", "Test cancellation");

        when(tradeOrderService.cancelOrder(eq(1L), anyString())).thenReturn(true);

        mockMvc.perform(post("/api/trade/orders/1/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void searchOrders_ShouldReturnFilteredOrders() throws Exception {
        // 创建专门用于搜索测试的订单
        TradeOrder searchOrder = new TradeOrder();
        searchOrder.setId(2L);
        searchOrder.setOrderStatus("PENDING");
        searchOrder.setOrderType("LIMIT");
        searchOrder.setOrderBizType("BUY");
        searchOrder.setAccountId(1001L);

        // 确保返回的列表包含有orderStatus属性的订单
        when(tradeOrderService.searchOrders(eq("PENDING"), eq("LIMIT"), eq("BUY"), eq(1001L), any(), any()))
                .thenReturn(Collections.singletonList(searchOrder));

        mockMvc.perform(get("/api/trade/orders/search")
                        .param("orderStatus", "PENDING")
                        .param("orderType", "LIMIT")
                        .param("orderBizType", "BUY")
                        .param("accountId", "1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderStatus").value("PENDING"));
    }

    @Test
    void updateOrderStatus_ShouldReturnOk() throws Exception {
        when(tradeOrderService.updateOrderStatus(1L, "COMPLETED")).thenReturn(true);

        mockMvc.perform(patch("/api/trade/orders/1/status")
                        .param("status", "COMPLETED"))
                .andExpect(status().isOk());
    }

    @Test
    void getFailedOrders_ShouldReturnRejectedOrders() throws Exception {
        // 创建专门的失败订单对象
        TradeOrder failedOrder = new TradeOrder();
        failedOrder.setId(3L);
        failedOrder.setOrderStatus("REJECTED");
        failedOrder.setCancelReason("Test failure");

        when(tradeOrderService.getOrdersByStatus("REJECTED")).thenReturn(Collections.singletonList(failedOrder));

        mockMvc.perform(get("/api/trade/orders/failed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderStatus").value("REJECTED"));
    }

    @Test
    void getOrdersByTimeRange_ShouldReturnOrders() throws Exception {
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 2, 0, 0);

        when(tradeOrderService.getOrdersByTimeRange(start, end))
                .thenReturn(Collections.singletonList(testOrder));

        mockMvc.perform(get("/api/trade/orders/timerange")
                        .param("startTime", "2023-01-01T00:00:00")
                        .param("endTime", "2023-01-02T00:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }
}