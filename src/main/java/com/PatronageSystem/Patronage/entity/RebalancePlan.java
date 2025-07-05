package com.PatronageSystem.Patronage.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RebalancePlan {
    private Long id;
    private String planName; // 调仓计划名称
    private String portfolioName; // 组合名称
    private String portfolioType; // 组合类型
    private String currentHoldings; // 当前持仓快照（JSON字符串）
    private String rebalanceInstructions; // 调仓指令列表（JSON字符串）
    private String status; // 计划状态（如：待执行、已完成、失败等）
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 