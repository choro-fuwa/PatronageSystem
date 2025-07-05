package com.PatronageSystem.Patronage.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TradeAccount {
    private Long id;
    private String accountCode;
    private String accountName;
    private Long userId;
    private String accountType;
    private String broker;
    private String accountStatus;
    private BigDecimal totalAssets;
    private BigDecimal availableCash;
    private BigDecimal frozenCash;
    private BigDecimal marketValue;
    private BigDecimal unrealizedPnl;
    private BigDecimal realizedPnl;
    private BigDecimal totalPnl;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
