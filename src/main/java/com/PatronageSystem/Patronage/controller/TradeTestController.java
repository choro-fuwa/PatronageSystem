package com.PatronageSystem.Patronage.controller;

import com.PatronageSystem.Patronage.entity.TradeAccount;
import com.PatronageSystem.Patronage.entity.TradeOrder;
import com.PatronageSystem.Patronage.entity.Position;
import com.PatronageSystem.Patronage.service.TradeAccountService;
import com.PatronageSystem.Patronage.service.TradeOrderService;
import com.PatronageSystem.Patronage.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trade/test")
@CrossOrigin(origins = "*")
public class TradeTestController {

    @Autowired
    private TradeAccountService tradeAccountService;

    @Autowired
    private TradeOrderService tradeOrderService;

    @Autowired
    private PositionService positionService;

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getSystemStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("message", "交易管理子系统运行正常");
        status.put("timestamp", System.currentTimeMillis());
        status.put("version", "1.0.0");
        return ResponseEntity.ok(status);
    }

    @GetMapping("/accounts/count")
    public ResponseEntity<Map<String, Object>> getAccountCount() {
        List<TradeAccount> accounts = tradeAccountService.getAllAccounts();
        Map<String, Object> result = new HashMap<>();
        result.put("count", accounts.size());
        result.put("accounts", accounts);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/orders/count")
    public ResponseEntity<Map<String, Object>> getOrderCount() {
        List<TradeOrder> orders = tradeOrderService.getAllOrders();
        Map<String, Object> result = new HashMap<>();
        result.put("count", orders.size());
        result.put("orders", orders);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/positions/count")
    public ResponseEntity<Map<String, Object>> getPositionCount() {
        List<Position> positions = positionService.getAllPositions();
        Map<String, Object> result = new HashMap<>();
        result.put("count", positions.size());
        result.put("positions", positions);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSystemSummary() {
        Map<String, Object> summary = new HashMap<>();

        List<TradeAccount> accounts = tradeAccountService.getAllAccounts();
        List<TradeOrder> orders = tradeOrderService.getAllOrders();
        List<Position> positions = positionService.getAllPositions();

        summary.put("accountCount", accounts.size());
        summary.put("orderCount", orders.size());
        summary.put("positionCount", positions.size());

        // 计算总资产
        double totalAssets = accounts.stream()
                .mapToDouble(account -> account.getTotalAssets() != null ? account.getTotalAssets().doubleValue() : 0)
                .sum();
        summary.put("totalAssets", totalAssets);

        // 计算总盈亏
        double totalPnl = accounts.stream()
                .mapToDouble(account -> account.getTotalPnl() != null ? account.getTotalPnl().doubleValue() : 0)
                .sum();
        summary.put("totalPnl", totalPnl);

        return ResponseEntity.ok(summary);
    }
}
