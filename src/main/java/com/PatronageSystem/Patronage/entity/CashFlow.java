package com.PatronageSystem.Patronage.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CashFlow {
    private Long id;
    private String flowCode;
    private Long accountId;
    private String flowType;
    private BigDecimal amount;
    private BigDecimal balance;
    private Long relatedOrderId;
    private Long relatedExecutionId;
    private String description;
    private LocalDateTime flowTime;
    private LocalDateTime createTime;
}
