package com.PatronageSystem.Patronage.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TradeExecution {
    private Long id;
    private String executionCode;
    private Long orderId;
    private Long accountId;
    private Long fundId;
    private BigDecimal executionPrice;
    private BigDecimal executionQuantity;
    private BigDecimal executionAmount;
    private BigDecimal commission;
    private BigDecimal netAmount;
    private LocalDateTime executionTime;
    private LocalDateTime createTime;
}
