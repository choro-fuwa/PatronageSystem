package com.PatronageSystem.Patronage.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Position {
    private Long id;
    private Long accountId;
    private Long fundId;
    private BigDecimal totalQuantity;
    private BigDecimal availableQuantity;
    private BigDecimal frozenQuantity;
    private BigDecimal avgCost;
    private BigDecimal marketPrice;
    private BigDecimal marketValue;
    private BigDecimal unrealizedPnl;
    private BigDecimal realizedPnl;
    private BigDecimal totalPnl;
    private LocalDateTime updateTime;
}
