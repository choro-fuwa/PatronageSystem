package com.PatronageSystem.Patronage.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TradeStatistics {
    private Long id;
    private Long accountId;
    private LocalDate statDate;
    private Integer totalTrades;
    private Integer buyTrades;
    private Integer sellTrades;
    private BigDecimal totalVolume;
    private BigDecimal totalAmount;
    private BigDecimal totalCommission;
    private BigDecimal realizedPnl;
    private BigDecimal turnoverRate;
    private BigDecimal winRate;
    private BigDecimal avgHoldDays;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
