package com.PatronageSystem.Patronage.service;

import com.PatronageSystem.Patronage.entity.TradeOrder;

import java.time.LocalDateTime;
import java.util.List;

public interface TradeOrderService {
    List<TradeOrder> getAllOrders();

    TradeOrder getOrderById(Long id);

    TradeOrder getOrderByCode(String orderCode);

    List<TradeOrder> getOrdersByAccountId(Long accountId);

    List<TradeOrder> getOrdersByStrategyId(Long strategyId);

    List<TradeOrder> getOrdersByStatus(String status);

    List<TradeOrder> getOrdersByBizType(String orderBizType);

    List<TradeOrder> getOrdersByTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    TradeOrder createOrder(TradeOrder order);

    TradeOrder updateOrder(TradeOrder order);

    boolean deleteOrder(Long id);

    boolean updateOrderStatus(Long id, String status);

    boolean executeOrder(Long id);

    boolean cancelOrder(Long id, String cancelReason);

    List<TradeOrder> searchOrders(String orderStatus, String orderType, String orderBizType, Long accountId, String startTime, String endTime);
}
