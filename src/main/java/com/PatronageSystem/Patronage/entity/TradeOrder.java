package com.PatronageSystem.Patronage.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TradeOrder {
    private Long id;
    private String orderCode;
    private Long accountId;
    private Long strategyId;
    private Long fundId;
    private String orderType;
    private String orderStatus;
    private String orderBizType;
    private BigDecimal orderPrice;
    private BigDecimal orderQuantity;
    private BigDecimal filledQuantity;
    private BigDecimal filledAmount;
    private BigDecimal avgFillPrice;
    private BigDecimal commission;
    private LocalDateTime orderTime;
    private LocalDateTime expireTime;
    private LocalDateTime cancelTime;
    private String cancelReason;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
