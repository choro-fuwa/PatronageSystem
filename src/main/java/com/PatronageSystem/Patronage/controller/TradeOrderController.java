package com.PatronageSystem.Patronage.controller;

import com.PatronageSystem.Patronage.entity.TradeOrder;
import com.PatronageSystem.Patronage.service.TradeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trade/orders")
@CrossOrigin(origins = "*")
public class TradeOrderController {

    @Autowired
    private TradeOrderService tradeOrderService;

    @GetMapping
    public ResponseEntity<List<TradeOrder>> getAllOrders() {
        List<TradeOrder> orders = tradeOrderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TradeOrder> getOrderById(@PathVariable Long id) {
        TradeOrder order = tradeOrderService.getOrderById(id);
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/code/{orderCode}")
    public ResponseEntity<TradeOrder> getOrderByCode(@PathVariable String orderCode) {
        TradeOrder order = tradeOrderService.getOrderByCode(orderCode);
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TradeOrder>> getOrdersByAccountId(@PathVariable Long accountId) {
        List<TradeOrder> orders = tradeOrderService.getOrdersByAccountId(accountId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/strategy/{strategyId}")
    public ResponseEntity<List<TradeOrder>> getOrdersByStrategyId(@PathVariable Long strategyId) {
        List<TradeOrder> orders = tradeOrderService.getOrdersByStrategyId(strategyId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TradeOrder>> getOrdersByStatus(@PathVariable String status) {
        List<TradeOrder> orders = tradeOrderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/biztype/{orderBizType}")
    public ResponseEntity<List<TradeOrder>> getOrdersByBizType(@PathVariable String orderBizType) {
        List<TradeOrder> orders = tradeOrderService.getOrdersByBizType(orderBizType);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/timerange")
    public ResponseEntity<List<TradeOrder>> getOrdersByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<TradeOrder> orders = tradeOrderService.getOrdersByTimeRange(startTime, endTime);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TradeOrder>> searchOrders(
            @RequestParam(required = false) String orderStatus,
            @RequestParam(required = false) String orderType,
            @RequestParam(required = false) String orderBizType,
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime
    ) {
        List<TradeOrder> orders = tradeOrderService.searchOrders(orderStatus, orderType, orderBizType, accountId, startTime, endTime);
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    public ResponseEntity<TradeOrder> createOrder(@RequestBody TradeOrder order) {
        TradeOrder createdOrder = tradeOrderService.createOrder(order);
        return ResponseEntity.ok(createdOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TradeOrder> updateOrder(@PathVariable Long id, @RequestBody TradeOrder order) {
        order.setId(id);
        TradeOrder updatedOrder = tradeOrderService.updateOrder(order);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        boolean deleted = tradeOrderService.deleteOrder(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        boolean updated = tradeOrderService.updateOrderStatus(id, status);
        if (updated) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/execute")
    public ResponseEntity<Map<String, Object>> executeOrder(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        boolean success = tradeOrderService.executeOrder(id);
        if (success) {
            response.put("success", true);
            response.put("message", "订单执行成功");
            response.put("orderId", id);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "订单执行失败，可能订单状态不允许执行");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Map<String, Object>> cancelOrder(@PathVariable Long id, @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String cancelReason = request.get("cancelReason");
        if (cancelReason == null || cancelReason.trim().isEmpty()) {
            cancelReason = "用户主动取消";
        }
        
        boolean success = tradeOrderService.cancelOrder(id, cancelReason);
        if (success) {
            response.put("success", true);
            response.put("message", "订单取消成功");
            response.put("orderId", id);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "订单取消失败，可能订单状态不允许取消");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/failed")
    public List<TradeOrder> getFailedOrders() {
        return tradeOrderService.getOrdersByStatus("REJECTED");
    }

    @PutMapping("/{id}/reprocess")
    public String reprocessOrder(@PathVariable Long id, @RequestBody TradeOrder update) {
        TradeOrder order = tradeOrderService.getOrderById(id);
        if (order == null) return "not found";
        order.setOrderStatus("PENDING");
        order.setCancelReason(update.getCancelReason());
        tradeOrderService.updateOrder(order);
        return "success";
    }
}
