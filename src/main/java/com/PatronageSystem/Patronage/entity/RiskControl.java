package com.PatronageSystem.Patronage.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RiskControl {
    private Long id;
    private Long accountId;
    private String riskType;
    private BigDecimal riskValue;
    private BigDecimal currentValue;
    private Integer isActive;
    private BigDecimal alertThreshold;
    private BigDecimal stopThreshold;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
