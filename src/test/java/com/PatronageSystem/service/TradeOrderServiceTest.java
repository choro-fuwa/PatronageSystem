package com.PatronageSystem.service;

import com.PatronageSystem.Patronage.entity.TradeOrder;
import com.PatronageSystem.Patronage.mapper.TradeOrderMapper;
import com.PatronageSystem.Patronage.service.TradeOrderService;
import com.PatronageSystem.Patronage.service.impl.TradeOrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TradeOrderServiceTest {

    @Mock
    private TradeOrderMapper tradeOrderMapper;

    @InjectMocks
    private TradeOrderServiceImpl tradeOrderService;

    private TradeOrder testOrder1;
    private TradeOrder testOrder2;
    private TradeOrder testOrder3;

    @BeforeEach
    void setUp() {
        // 创建测试数据
        testOrder1 = new TradeOrder();
        testOrder1.setId(1L);
        testOrder1.setOrderCode("ORDER001");
        testOrder1.setAccountId(1001L);
        testOrder1.setStrategyId(2001L);
        testOrder1.setFundId(3001L);
        testOrder1.setOrderType("BUY");
        testOrder1.setOrderStatus("PENDING");
        testOrder1.setOrderBizType("NORMAL");
        testOrder1.setOrderPrice(new BigDecimal("100.50"));
        testOrder1.setOrderQuantity(new BigDecimal("100"));
        testOrder1.setFilledQuantity(new BigDecimal("0"));
        testOrder1.setFilledAmount(new BigDecimal("0"));
        testOrder1.setAvgFillPrice(new BigDecimal("0"));
        testOrder1.setCommission(new BigDecimal("0"));
        testOrder1.setOrderTime(LocalDateTime.now());
        testOrder1.setExpireTime(LocalDateTime.now().plusDays(1));
        testOrder1.setCancelTime(null);
        testOrder1.setCancelReason(null);
        testOrder1.setRemark("测试买入订单");
        testOrder1.setCreateTime(LocalDateTime.now());
        testOrder1.setUpdateTime(LocalDateTime.now());

        testOrder2 = new TradeOrder();
        testOrder2.setId(2L);
        testOrder2.setOrderCode("ORDER002");
        testOrder2.setAccountId(1001L);
        testOrder2.setStrategyId(2002L);
        testOrder2.setFundId(3002L);
        testOrder2.setOrderType("SELL");
        testOrder2.setOrderStatus("FILLED");
        testOrder2.setOrderBizType("NORMAL");
        testOrder2.setOrderPrice(new BigDecimal("95.20"));
        testOrder2.setOrderQuantity(new BigDecimal("50"));
        testOrder2.setFilledQuantity(new BigDecimal("50"));
        testOrder2.setFilledAmount(new BigDecimal("4760.00"));
        testOrder2.setAvgFillPrice(new BigDecimal("95.20"));
        testOrder2.setCommission(new BigDecimal("5.00"));
        testOrder2.setOrderTime(LocalDateTime.now().minusHours(2));
        testOrder2.setExpireTime(LocalDateTime.now().plusHours(22));
        testOrder2.setCancelTime(null);
        testOrder2.setCancelReason(null);
        testOrder2.setRemark("测试卖出订单");
        testOrder2.setCreateTime(LocalDateTime.now().minusHours(2));
        testOrder2.setUpdateTime(LocalDateTime.now().minusHours(1));

        testOrder3 = new TradeOrder();
        testOrder3.setId(3L);
        testOrder3.setOrderCode("ORDER003");
        testOrder3.setAccountId(1002L);
        testOrder3.setStrategyId(2003L);
        testOrder3.setFundId(3003L);
        testOrder3.setOrderType("BUY");
        testOrder3.setOrderStatus("CANCELLED");
        testOrder3.setOrderBizType("REBALANCE");
        testOrder3.setOrderPrice(new BigDecimal("120.00"));
        testOrder3.setOrderQuantity(new BigDecimal("200"));
        testOrder3.setFilledQuantity(new BigDecimal("0"));
        testOrder3.setFilledAmount(new BigDecimal("0"));
        testOrder3.setAvgFillPrice(new BigDecimal("0"));
        testOrder3.setCommission(new BigDecimal("0"));
        testOrder3.setOrderTime(LocalDateTime.now().minusDays(1));
        testOrder3.setExpireTime(LocalDateTime.now().minusHours(12));
        testOrder3.setCancelTime(LocalDateTime.now().minusHours(6));
        testOrder3.setCancelReason("用户主动取消");
        testOrder3.setRemark("测试取消订单");
        testOrder3.setCreateTime(LocalDateTime.now().minusDays(1));
        testOrder3.setUpdateTime(LocalDateTime.now().minusHours(6));
    }

    @Test
    void testGetAllOrders_Success() {
        // Given
        List<TradeOrder> expectedOrders = Arrays.asList(testOrder1, testOrder2, testOrder3);
        when(tradeOrderMapper.selectAll()).thenReturn(expectedOrders);

        // When
        List<TradeOrder> actualOrders = tradeOrderService.getAllOrders();

        // Then
        assertNotNull(actualOrders);
        assertEquals(3, actualOrders.size());
        assertEquals(expectedOrders, actualOrders);
        verify(tradeOrderMapper, times(1)).selectAll();
    }

    @Test
    void testGetAllOrders_EmptyList() {
        // Given
        when(tradeOrderMapper.selectAll()).thenReturn(Collections.emptyList());

        // When
        List<TradeOrder> actualOrders = tradeOrderService.getAllOrders();

        // Then
        assertNotNull(actualOrders);
        assertTrue(actualOrders.isEmpty());
        verify(tradeOrderMapper, times(1)).selectAll();
    }

    @Test
    void testGetOrderById_Success() {
        // Given
        Long orderId = 1L;
        when(tradeOrderMapper.selectById(orderId)).thenReturn(testOrder1);

        // When
        TradeOrder actualOrder = tradeOrderService.getOrderById(orderId);

        // Then
        assertNotNull(actualOrder);
        assertEquals(testOrder1, actualOrder);
        assertEquals(orderId, actualOrder.getId());
        verify(tradeOrderMapper, times(1)).selectById(orderId);
    }

    @Test
    void testGetOrderById_NotFound() {
        // Given
        Long orderId = 999L;
        when(tradeOrderMapper.selectById(orderId)).thenReturn(null);

        // When
        TradeOrder actualOrder = tradeOrderService.getOrderById(orderId);

        // Then
        assertNull(actualOrder);
        verify(tradeOrderMapper, times(1)).selectById(orderId);
    }

    @Test
    void testGetOrderById_NullId() {
        // Given
        when(tradeOrderMapper.selectById(null)).thenReturn(null);

        // When
        TradeOrder actualOrder = tradeOrderService.getOrderById(null);

        // Then
        assertNull(actualOrder);
        verify(tradeOrderMapper, times(1)).selectById(null);
    }

    @Test
    void testGetOrderByCode_Success() {
        // Given
        String orderCode = "ORDER001";
        when(tradeOrderMapper.selectByOrderCode(orderCode)).thenReturn(testOrder1);

        // When
        TradeOrder actualOrder = tradeOrderService.getOrderByCode(orderCode);

        // Then
        assertNotNull(actualOrder);
        assertEquals(testOrder1, actualOrder);
        assertEquals(orderCode, actualOrder.getOrderCode());
        verify(tradeOrderMapper, times(1)).selectByOrderCode(orderCode);
    }

    @Test
    void testGetOrderByCode_NotFound() {
        // Given
        String orderCode = "INVALID_CODE";
        when(tradeOrderMapper.selectByOrderCode(orderCode)).thenReturn(null);

        // When
        TradeOrder actualOrder = tradeOrderService.getOrderByCode(orderCode);

        // Then
        assertNull(actualOrder);
        verify(tradeOrderMapper, times(1)).selectByOrderCode(orderCode);
    }

    @Test
    void testGetOrderByCode_NullCode() {
        // Given
        when(tradeOrderMapper.selectByOrderCode(null)).thenReturn(null);

        // When
        TradeOrder actualOrder = tradeOrderService.getOrderByCode(null);

        // Then
        assertNull(actualOrder);
        verify(tradeOrderMapper, times(1)).selectByOrderCode(null);
    }

    @Test
    void testGetOrdersByAccountId_Success() {
        // Given
        Long accountId = 1001L;
        List<TradeOrder> expectedOrders = Arrays.asList(testOrder1, testOrder2);
        when(tradeOrderMapper.selectByAccountId(accountId)).thenReturn(expectedOrders);

        // When
        List<TradeOrder> actualOrders = tradeOrderService.getOrdersByAccountId(accountId);

        // Then
        assertNotNull(actualOrders);
        assertEquals(2, actualOrders.size());
        assertEquals(expectedOrders, actualOrders);
        actualOrders.forEach(order -> assertEquals(accountId, order.getAccountId()));
        verify(tradeOrderMapper, times(1)).selectByAccountId(accountId);
    }

    @Test
    void testGetOrdersByAccountId_EmptyList() {
        // Given
        Long accountId = 9999L;
        when(tradeOrderMapper.selectByAccountId(accountId)).thenReturn(Collections.emptyList());

        // When
        List<TradeOrder> actualOrders = tradeOrderService.getOrdersByAccountId(accountId);

        // Then
        assertNotNull(actualOrders);
        assertTrue(actualOrders.isEmpty());
        verify(tradeOrderMapper, times(1)).selectByAccountId(accountId);
    }

    @Test
    void testGetOrdersByStrategyId_Success() {
        // Given
        Long strategyId = 2001L;
        List<TradeOrder> expectedOrders = Arrays.asList(testOrder1);
        when(tradeOrderMapper.selectByStrategyId(strategyId)).thenReturn(expectedOrders);

        // When
        List<TradeOrder> actualOrders = tradeOrderService.getOrdersByStrategyId(strategyId);

        // Then
        assertNotNull(actualOrders);
        assertEquals(1, actualOrders.size());
        assertEquals(expectedOrders, actualOrders);
        assertEquals(strategyId, actualOrders.get(0).getStrategyId());
        verify(tradeOrderMapper, times(1)).selectByStrategyId(strategyId);
    }

    @Test
    void testGetOrdersByStrategyId_EmptyList() {
        // Given
        Long strategyId = 9999L;
        when(tradeOrderMapper.selectByStrategyId(strategyId)).thenReturn(Collections.emptyList());

        // When
        List<TradeOrder> actualOrders = tradeOrderService.getOrdersByStrategyId(strategyId);

        // Then
        assertNotNull(actualOrders);
        assertTrue(actualOrders.isEmpty());
        verify(tradeOrderMapper, times(1)).selectByStrategyId(strategyId);
    }

    @Test
    void testGetOrdersByStatus_Success() {
        // Given
        String status = "PENDING";
        List<TradeOrder> expectedOrders = Arrays.asList(testOrder1);
        when(tradeOrderMapper.selectByStatus(status)).thenReturn(expectedOrders);

        // When
        List<TradeOrder> actualOrders = tradeOrderService.getOrdersByStatus(status);

        // Then
        assertNotNull(actualOrders);
        assertEquals(1, actualOrders.size());
        assertEquals(expectedOrders, actualOrders);
        actualOrders.forEach(order -> assertEquals(status, order.getOrderStatus()));
        verify(tradeOrderMapper, times(1)).selectByStatus(status);
    }

    @Test
    void testGetOrdersByStatus_Filled() {
        // Given
        String status = "FILLED";
        List<TradeOrder> expectedOrders = Arrays.asList(testOrder2);
        when(tradeOrderMapper.selectByStatus(status)).thenReturn(expectedOrders);

        // When
        List<TradeOrder> actualOrders = tradeOrderService.getOrdersByStatus(status);

        // Then
        assertNotNull(actualOrders);
        assertEquals(1, actualOrders.size());
        assertEquals(expectedOrders, actualOrders);
        assertEquals(status, actualOrders.get(0).getOrderStatus());
        verify(tradeOrderMapper, times(1)).selectByStatus(status);
    }

    @Test
    void testGetOrdersByStatus_EmptyList() {
        // Given
        String status = "REJECTED";
        when(tradeOrderMapper.selectByStatus(status)).thenReturn(Collections.emptyList());

        // When
        List<TradeOrder> actualOrders = tradeOrderService.getOrdersByStatus(status);

        // Then
        assertNotNull(actualOrders);
        assertTrue(actualOrders.isEmpty());
        verify(tradeOrderMapper, times(1)).selectByStatus(status);
    }

    @Test
    void testGetOrdersByBizType_Success() {
        // Given
        String bizType = "NORMAL";
        List<TradeOrder> expectedOrders = Arrays.asList(testOrder1, testOrder2);
        when(tradeOrderMapper.selectByBizType(bizType)).thenReturn(expectedOrders);

        // When
        List<TradeOrder> actualOrders = tradeOrderService.getOrdersByBizType(bizType);

        // Then
        assertNotNull(actualOrders);
        assertEquals(2, actualOrders.size());
        assertEquals(expectedOrders, actualOrders);
        actualOrders.forEach(order -> assertEquals(bizType, order.getOrderBizType()));
        verify(tradeOrderMapper, times(1)).selectByBizType(bizType);
    }

    @Test
    void testGetOrdersByBizType_Rebalance() {
        // Given
        String bizType = "REBALANCE";
        List<TradeOrder> expectedOrders = Arrays.asList(testOrder3);
        when(tradeOrderMapper.selectByBizType(bizType)).thenReturn(expectedOrders);

        // When
        List<TradeOrder> actualOrders = tradeOrderService.getOrdersByBizType(bizType);

        // Then
        assertNotNull(actualOrders);
        assertEquals(1, actualOrders.size());
        assertEquals(expectedOrders, actualOrders);
        assertEquals(bizType, actualOrders.get(0).getOrderBizType());
        verify(tradeOrderMapper, times(1)).selectByBizType(bizType);
    }

    @Test
    void testGetOrdersByTimeRange_Success() {
        // Given
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now();
        List<TradeOrder> expectedOrders = Arrays.asList(testOrder1, testOrder2, testOrder3);
        when(tradeOrderMapper.selectByTimeRange(startTime, endTime)).thenReturn(expectedOrders);

        // When
        List<TradeOrder> actualOrders = tradeOrderService.getOrdersByTimeRange(startTime, endTime);

        // Then
        assertNotNull(actualOrders);
        assertEquals(3, actualOrders.size());
        assertEquals(expectedOrders, actualOrders);
        verify(tradeOrderMapper, times(1)).selectByTimeRange(startTime, endTime);
    }

    @Test
    void testGetOrdersByTimeRange_EmptyList() {
        // Given
        LocalDateTime startTime = LocalDateTime.now().minusDays(30);
        LocalDateTime endTime = LocalDateTime.now().minusDays(25);
        when(tradeOrderMapper.selectByTimeRange(startTime, endTime)).thenReturn(Collections.emptyList());

        // When
        List<TradeOrder> actualOrders = tradeOrderService.getOrdersByTimeRange(startTime, endTime);

        // Then
        assertNotNull(actualOrders);
        assertTrue(actualOrders.isEmpty());
        verify(tradeOrderMapper, times(1)).selectByTimeRange(startTime, endTime);
    }

    @Test
    void testCreateOrder_Success() {
        // Given
        TradeOrder newOrder = new TradeOrder();
        newOrder.setOrderCode("ORDER004");
        newOrder.setAccountId(1003L);
        newOrder.setStrategyId(2004L);
        newOrder.setFundId(3004L);
        newOrder.setOrderType("BUY");
        newOrder.setOrderStatus("PENDING");
        newOrder.setOrderPrice(new BigDecimal("150.00"));
        newOrder.setOrderQuantity(new BigDecimal("75"));
        newOrder.setOrderTime(LocalDateTime.now());
        newOrder.setExpireTime(LocalDateTime.now().plusDays(1));
        newOrder.setRemark("新创建的订单");

        when(tradeOrderMapper.insert(newOrder)).thenReturn(1);

        // When
        TradeOrder createdOrder = tradeOrderService.createOrder(newOrder);

        // Then
        assertNotNull(createdOrder);
        assertEquals(newOrder, createdOrder);
        assertEquals("NORMAL", createdOrder.getOrderBizType()); // 默认业务类型
        verify(tradeOrderMapper, times(1)).insert(newOrder);
    }

    @Test
    void testCreateOrder_WithExistingBizType() {
        // Given
        TradeOrder newOrder = new TradeOrder();
        newOrder.setOrderCode("ORDER005");
        newOrder.setAccountId(1004L);
        newOrder.setStrategyId(2005L);
        newOrder.setFundId(3005L);
        newOrder.setOrderType("SELL");
        newOrder.setOrderStatus("PENDING");
        newOrder.setOrderBizType("REBALANCE");
        newOrder.setOrderPrice(new BigDecimal("80.00"));
        newOrder.setOrderQuantity(new BigDecimal("200"));
        newOrder.setOrderTime(LocalDateTime.now());
        newOrder.setExpireTime(LocalDateTime.now().plusDays(1));

        when(tradeOrderMapper.insert(newOrder)).thenReturn(1);

        // When
        TradeOrder createdOrder = tradeOrderService.createOrder(newOrder);

        // Then
        assertNotNull(createdOrder);
        assertEquals(newOrder, createdOrder);
        assertEquals("REBALANCE", createdOrder.getOrderBizType());
        verify(tradeOrderMapper, times(1)).insert(newOrder);
    }

    @Test
    void testUpdateOrder_Success() {
        // Given
        TradeOrder updateOrder = new TradeOrder();
        updateOrder.setId(1L);
        updateOrder.setOrderCode("ORDER001");
        updateOrder.setAccountId(1001L);
        updateOrder.setStrategyId(2001L);
        updateOrder.setFundId(3001L);
        updateOrder.setOrderType("BUY");
        updateOrder.setOrderStatus("PARTIAL");
        updateOrder.setOrderBizType("NORMAL");
        updateOrder.setOrderPrice(new BigDecimal("100.50"));
        updateOrder.setOrderQuantity(new BigDecimal("100"));
        updateOrder.setFilledQuantity(new BigDecimal("50"));
        updateOrder.setFilledAmount(new BigDecimal("5025.00"));
        updateOrder.setAvgFillPrice(new BigDecimal("100.50"));
        updateOrder.setCommission(new BigDecimal("2.50"));
        updateOrder.setOrderTime(LocalDateTime.now());
        updateOrder.setExpireTime(LocalDateTime.now().plusDays(1));
        updateOrder.setRemark("更新后的订单");

        when(tradeOrderMapper.update(updateOrder)).thenReturn(1);

        // When
        TradeOrder updatedOrder = tradeOrderService.updateOrder(updateOrder);

        // Then
        assertNotNull(updatedOrder);
        assertEquals(updateOrder, updatedOrder);
        assertEquals("PARTIAL", updatedOrder.getOrderStatus());
        assertEquals(new BigDecimal("50"), updatedOrder.getFilledQuantity());
        verify(tradeOrderMapper, times(1)).update(updateOrder);
    }

    @Test
    void testDeleteOrder_Success() {
        // Given
        Long orderId = 1L;
        when(tradeOrderMapper.deleteById(orderId)).thenReturn(1);

        // When
        boolean result = tradeOrderService.deleteOrder(orderId);

        // Then
        assertTrue(result);
        verify(tradeOrderMapper, times(1)).deleteById(orderId);
    }

    @Test
    void testDeleteOrder_NotFound() {
        // Given
        Long orderId = 999L;
        when(tradeOrderMapper.deleteById(orderId)).thenReturn(0);

        // When
        boolean result = tradeOrderService.deleteOrder(orderId);

        // Then
        assertFalse(result);
        verify(tradeOrderMapper, times(1)).deleteById(orderId);
    }

    @Test
    void testUpdateOrderStatus_Success() {
        // Given
        Long orderId = 1L;
        String newStatus = "EXECUTING";
        when(tradeOrderMapper.updateOrderStatus(orderId, newStatus)).thenReturn(1);

        // When
        boolean result = tradeOrderService.updateOrderStatus(orderId, newStatus);

        // Then
        assertTrue(result);
        verify(tradeOrderMapper, times(1)).updateOrderStatus(orderId, newStatus);
    }

    @Test
    void testUpdateOrderStatus_NotFound() {
        // Given
        Long orderId = 999L;
        String newStatus = "FILLED";
        when(tradeOrderMapper.updateOrderStatus(orderId, newStatus)).thenReturn(0);

        // When
        boolean result = tradeOrderService.updateOrderStatus(orderId, newStatus);

        // Then
        assertFalse(result);
        verify(tradeOrderMapper, times(1)).updateOrderStatus(orderId, newStatus);
    }

    @Test
    void testExecuteOrder_Success() {
        // Given
        Long orderId = 1L;
        when(tradeOrderMapper.executeOrder(orderId, "EXECUTING")).thenReturn(1);

        // When
        boolean result = tradeOrderService.executeOrder(orderId);

        // Then
        assertTrue(result);
        verify(tradeOrderMapper, times(1)).executeOrder(orderId, "EXECUTING");
    }

    @Test
    void testExecuteOrder_NotFound() {
        // Given
        Long orderId = 999L;
        when(tradeOrderMapper.executeOrder(orderId, "EXECUTING")).thenReturn(0);

        // When
        boolean result = tradeOrderService.executeOrder(orderId);

        // Then
        assertFalse(result);
        verify(tradeOrderMapper, times(1)).executeOrder(orderId, "EXECUTING");
    }

    @Test
    void testCancelOrder_Success() {
        // Given
        Long orderId = 1L;
        String cancelReason = "市场波动较大";
        when(tradeOrderMapper.cancelOrder(orderId, "CANCELLED", cancelReason)).thenReturn(1);

        // When
        boolean result = tradeOrderService.cancelOrder(orderId, cancelReason);

        // Then
        assertTrue(result);
        verify(tradeOrderMapper, times(1)).cancelOrder(orderId, "CANCELLED", cancelReason);
    }

    @Test
    void testCancelOrder_NotFound() {
        // Given
        Long orderId = 999L;
        String cancelReason = "用户取消";
        when(tradeOrderMapper.cancelOrder(orderId, "CANCELLED", cancelReason)).thenReturn(0);

        // When
        boolean result = tradeOrderService.cancelOrder(orderId, cancelReason);

        // Then
        assertFalse(result);
        verify(tradeOrderMapper, times(1)).cancelOrder(orderId, "CANCELLED", cancelReason);
    }

    @Test
    void testCancelOrder_NullReason() {
        // Given
        Long orderId = 1L;
        String cancelReason = null;
        when(tradeOrderMapper.cancelOrder(orderId, "CANCELLED", cancelReason)).thenReturn(1);

        // When
        boolean result = tradeOrderService.cancelOrder(orderId, cancelReason);

        // Then
        assertTrue(result);
        verify(tradeOrderMapper, times(1)).cancelOrder(orderId, "CANCELLED", cancelReason);
    }

    @Test
    void testSearchOrders_ByStatusAndType() {
        // Given
        String orderStatus = "PENDING";
        String orderType = "BUY";
        String orderBizType = null;
        Long accountId = null;
        String startTime = null;
        String endTime = null;
        List<TradeOrder> expectedOrders = Arrays.asList(testOrder1);
        when(tradeOrderMapper.searchOrders(orderStatus, orderType, orderBizType, accountId, startTime, endTime))
                .thenReturn(expectedOrders);

        // When
        List<TradeOrder> actualOrders = tradeOrderService.searchOrders(orderStatus, orderType, orderBizType, accountId, startTime, endTime);

        // Then
        assertNotNull(actualOrders);
        assertEquals(1, actualOrders.size());
        assertEquals(expectedOrders, actualOrders);
        assertEquals(orderStatus, actualOrders.get(0).getOrderStatus());
        assertEquals(orderType, actualOrders.get(0).getOrderType());
        verify(tradeOrderMapper, times(1)).searchOrders(orderStatus, orderType, orderBizType, accountId, startTime, endTime);
    }

    @Test
    void testSearchOrders_ByAccountId() {
        // Given
        String orderStatus = null;
        String orderType = null;
        String orderBizType = null;
        Long accountId = 1001L;
        String startTime = null;
        String endTime = null;
        List<TradeOrder> expectedOrders = Arrays.asList(testOrder1, testOrder2);
        when(tradeOrderMapper.searchOrders(orderStatus, orderType, orderBizType, accountId, startTime, endTime))
                .thenReturn(expectedOrders);

        // When
        List<TradeOrder> actualOrders = tradeOrderService.searchOrders(orderStatus, orderType, orderBizType, accountId, startTime, endTime);

        // Then
        assertNotNull(actualOrders);
        assertEquals(2, actualOrders.size());
        assertEquals(expectedOrders, actualOrders);
        actualOrders.forEach(order -> assertEquals(accountId, order.getAccountId()));
        verify(tradeOrderMapper, times(1)).searchOrders(orderStatus, orderType, orderBizType, accountId, startTime, endTime);
    }

    @Test
    void testSearchOrders_ByTimeRange() {
        // Given
        String orderStatus = null;
        String orderType = null;
        String orderBizType = null;
        Long accountId = null;
        String startTime = "2023-01-01 00:00:00";
        String endTime = "2023-12-31 23:59:59";
        List<TradeOrder> expectedOrders = Arrays.asList(testOrder1, testOrder2, testOrder3);
        when(tradeOrderMapper.searchOrders(orderStatus, orderType, orderBizType, accountId, startTime, endTime))
                .thenReturn(expectedOrders);

        // When
        List<TradeOrder> actualOrders = tradeOrderService.searchOrders(orderStatus, orderType, orderBizType, accountId, startTime, endTime);

        // Then
        assertNotNull(actualOrders);
        assertEquals(3, actualOrders.size());
        assertEquals(expectedOrders, actualOrders);
        verify(tradeOrderMapper, times(1)).searchOrders(orderStatus, orderType, orderBizType, accountId, startTime, endTime);
    }

    @Test
    void testSearchOrders_NoCriteria() {
        // Given
        String orderStatus = null;
        String orderType = null;
        String orderBizType = null;
        Long accountId = null;
        String startTime = null;
        String endTime = null;
        List<TradeOrder> expectedOrders = Arrays.asList(testOrder1, testOrder2, testOrder3);
        when(tradeOrderMapper.searchOrders(orderStatus, orderType, orderBizType, accountId, startTime, endTime))
                .thenReturn(expectedOrders);

        // When
        List<TradeOrder> actualOrders = tradeOrderService.searchOrders(orderStatus, orderType, orderBizType, accountId, startTime, endTime);

        // Then
        assertNotNull(actualOrders);
        assertEquals(3, actualOrders.size());
        assertEquals(expectedOrders, actualOrders);
        verify(tradeOrderMapper, times(1)).searchOrders(orderStatus, orderType, orderBizType, accountId, startTime, endTime);
    }

    @Test
    void testSearchOrders_EmptyResult() {
        // Given
        String orderStatus = "REJECTED";
        String orderType = "BUY";
        String orderBizType = "NORMAL";
        Long accountId = 9999L;
        String startTime = null;
        String endTime = null;
        when(tradeOrderMapper.searchOrders(orderStatus, orderType, orderBizType, accountId, startTime, endTime))
                .thenReturn(Collections.emptyList());

        // When
        List<TradeOrder> actualOrders = tradeOrderService.searchOrders(orderStatus, orderType, orderBizType, accountId, startTime, endTime);

        // Then
        assertNotNull(actualOrders);
        assertTrue(actualOrders.isEmpty());
        verify(tradeOrderMapper, times(1)).searchOrders(orderStatus, orderType, orderBizType, accountId, startTime, endTime);
    }

    @Test
    void testOrderDataIntegrity() {
        // Given
        TradeOrder order = new TradeOrder();
        order.setId(1L);
        order.setOrderCode("TEST001");
        order.setAccountId(1001L);
        order.setStrategyId(2001L);
        order.setFundId(3001L);
        order.setOrderType("BUY");
        order.setOrderStatus("PENDING");
        order.setOrderBizType("NORMAL");
        order.setOrderPrice(new BigDecimal("100.00"));
        order.setOrderQuantity(new BigDecimal("100"));
        order.setFilledQuantity(new BigDecimal("50"));
        order.setFilledAmount(new BigDecimal("5000.00"));
        order.setAvgFillPrice(new BigDecimal("100.00"));
        order.setCommission(new BigDecimal("5.00"));
        order.setOrderTime(LocalDateTime.now());
        order.setExpireTime(LocalDateTime.now().plusDays(1));

        // When & Then
        assertNotNull(order.getId());
        assertNotNull(order.getOrderCode());
        assertNotNull(order.getAccountId());
        assertNotNull(order.getFundId());
        assertNotNull(order.getOrderType());
        assertNotNull(order.getOrderStatus());
        assertNotNull(order.getOrderPrice());
        assertNotNull(order.getOrderQuantity());
        assertNotNull(order.getFilledQuantity());
        assertNotNull(order.getFilledAmount());
        assertNotNull(order.getAvgFillPrice());
        assertNotNull(order.getCommission());
        assertNotNull(order.getOrderTime());

        // 验证数值逻辑
        BigDecimal expectedFilledAmount = order.getFilledQuantity().multiply(order.getAvgFillPrice());
        assertEquals(0, expectedFilledAmount.compareTo(order.getFilledAmount()));

        // 验证成交数量不能超过委托数量
        assertTrue(order.getFilledQuantity().compareTo(order.getOrderQuantity()) <= 0);
    }

    @Test
    void testOrderStatusTransitions() {
        // Given
        Long orderId = 1L;
        
        // Test PENDING -> EXECUTING
        when(tradeOrderMapper.updateOrderStatus(orderId, "EXECUTING")).thenReturn(1);
        boolean executingResult = tradeOrderService.updateOrderStatus(orderId, "EXECUTING");
        assertTrue(executingResult);
        verify(tradeOrderMapper, times(1)).updateOrderStatus(orderId, "EXECUTING");

        // Test EXECUTING -> PARTIAL
        when(tradeOrderMapper.updateOrderStatus(orderId, "PARTIAL")).thenReturn(1);
        boolean partialResult = tradeOrderService.updateOrderStatus(orderId, "PARTIAL");
        assertTrue(partialResult);
        verify(tradeOrderMapper, times(1)).updateOrderStatus(orderId, "PARTIAL");

        // Test PARTIAL -> FILLED
        when(tradeOrderMapper.updateOrderStatus(orderId, "FILLED")).thenReturn(1);
        boolean filledResult = tradeOrderService.updateOrderStatus(orderId, "FILLED");
        assertTrue(filledResult);
        verify(tradeOrderMapper, times(1)).updateOrderStatus(orderId, "FILLED");
    }

    @Test
    void testOrderExecutionFlow() {
        // Given
        Long orderId = 1L;
        
        // Step 1: Execute order
        when(tradeOrderMapper.executeOrder(orderId, "EXECUTING")).thenReturn(1);
        boolean executeResult = tradeOrderService.executeOrder(orderId);
        assertTrue(executeResult);
        verify(tradeOrderMapper, times(1)).executeOrder(orderId, "EXECUTING");

        // Step 2: Update to filled
        when(tradeOrderMapper.updateOrderStatus(orderId, "FILLED")).thenReturn(1);
        boolean filledResult = tradeOrderService.updateOrderStatus(orderId, "FILLED");
        assertTrue(filledResult);
        verify(tradeOrderMapper, times(1)).updateOrderStatus(orderId, "FILLED");
    }

    @Test
    void testOrderCancellationFlow() {
        // Given
        Long orderId = 1L;
        String cancelReason = "市场条件变化";
        
        // Cancel order
        when(tradeOrderMapper.cancelOrder(orderId, "CANCELLED", cancelReason)).thenReturn(1);
        boolean cancelResult = tradeOrderService.cancelOrder(orderId, cancelReason);
        assertTrue(cancelResult);
        verify(tradeOrderMapper, times(1)).cancelOrder(orderId, "CANCELLED", cancelReason);
    }
}
