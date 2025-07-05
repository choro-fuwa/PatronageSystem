package com.patronage.trading.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SettlementOrder {
    private Long settlementId;     // 交割单ID
    private Long tradeId;          // 关联交易单ID
    private String clientId;       // 客户ID
    private String fundCode;       // 基金代码
    private String status;         // 状态：success/failed
    private String errorReason;    // 失败原因
    private LocalDateTime settlementTime; // 交割时间
}