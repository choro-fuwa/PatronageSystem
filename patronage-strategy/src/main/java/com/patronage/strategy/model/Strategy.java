package com.patronage.strategy.model;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 策略基础模型
 */
@Data
public class Strategy {
    private Long id;                     // 策略ID
    private String name;                 // 策略名称
    private String type;                 // 策略类型（大类资产配置/FOF组合/基金指数/择时组合）
    private String description;          // 策略描述
    private String parameters;           // 策略参数（JSON格式存储）
    private String status;               // 策略状态（创建/回测中/运行中/已停止）
    private LocalDateTime createTime;    // 创建时间
    private LocalDateTime updateTime;    // 更新时间
    private String creatorId;            // 创建者ID
    private String riskLevel;            // 风险等级
    private Boolean needApproval;        // 是否需要风控审核
}