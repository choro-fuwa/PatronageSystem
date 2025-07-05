package com.PatronageSystem.Patronage.service.impl;

import com.PatronageSystem.Patronage.entity.TradeOrder;
import com.PatronageSystem.Patronage.mapper.TradeOrderMapper;
import com.PatronageSystem.Patronage.service.TradeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TradeOrderServiceImpl implements TradeOrderService {

    @Autowired
    private TradeOrderMapper tradeOrderMapper;

    @Override
    public List<TradeOrder> getAllOrders() {
        return tradeOrderMapper.selectAll();
    }

    @Override
    public TradeOrder getOrderById(Long id) {
        return tradeOrderMapper.selectById(id);
    }

    @Override
    public TradeOrder getOrderByCode(String orderCode) {
        return tradeOrderMapper.selectByOrderCode(orderCode);
    }

    @Override
    public List<TradeOrder> getOrdersByAccountId(Long accountId) {
        return tradeOrderMapper.selectByAccountId(accountId);
    }

    @Override
    public List<TradeOrder> getOrdersByStrategyId(Long strategyId) {
        return tradeOrderMapper.selectByStrategyId(strategyId);
    }

    @Override
    public List<TradeOrder> getOrdersByStatus(String status) {
        return tradeOrderMapper.selectByStatus(status);
    }

    @Override
    public List<TradeOrder> getOrdersByBizType(String orderBizType) {
        return tradeOrderMapper.selectByBizType(orderBizType);
    }

    @Override
    public List<TradeOrder> getOrdersByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return tradeOrderMapper.selectByTimeRange(startTime, endTime);
    }

    @Override
    public TradeOrder createOrder(TradeOrder order) {
        // 设置默认业务类型
        if (order.getOrderBizType() == null || order.getOrderBizType().isEmpty()) {
            order.setOrderBizType("NORMAL");
        }
        tradeOrderMapper.insert(order);
        return order;
    }

    @Override
    public TradeOrder updateOrder(TradeOrder order) {
        tradeOrderMapper.update(order);
        return order;
    }

    @Override
    public boolean deleteOrder(Long id) {
        return tradeOrderMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateOrderStatus(Long id, String status) {
        return tradeOrderMapper.updateOrderStatus(id, status) > 0;
    }

    @Override
    public boolean executeOrder(Long id) {
        // 模拟与外部系统交互，将订单状态改为执行中
        return tradeOrderMapper.executeOrder(id, "EXECUTING") > 0;
    }

    @Override
    public boolean cancelOrder(Long id, String cancelReason) {
        // 模拟与外部系统交互，将订单状态改为已取消
        return tradeOrderMapper.cancelOrder(id, "CANCELLED", cancelReason) > 0;
    }

    @Override
    public List<TradeOrder> searchOrders(String orderStatus, String orderType, String orderBizType, Long accountId, String startTime, String endTime) {
        return tradeOrderMapper.searchOrders(orderStatus, orderType, orderBizType, accountId, startTime, endTime);
    }
}
