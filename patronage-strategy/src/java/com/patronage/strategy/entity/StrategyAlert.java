package com.patronage.strategy.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 策略预警实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("strategy_alert")
public class StrategyAlert {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 策略ID
     */
    @TableField("strategy_id")
    private Long strategyId;

    /**
     * 预警名称
     */
    @TableField("alert_name")
    private String alertName;

    /**
     * 预警类型（1:收益率 2:回撤 3:波动率 4:持仓集中度）
     */
    @TableField("alert_type")
    private Integer alertType;

    /**
     * 预警条件（1:大于 2:小于 3:等于）
     */
    @TableField("condition_type")
    private Integer conditionType;

    /**
     * 预警阈值
     */
    @TableField("threshold")
    private BigDecimal threshold;

    /**
     * 是否启用（0:禁用 1:启用）
     */
    @TableField("is_enabled")
    private Integer isEnabled;

    /**
     * 通知方式（1:邮件 2:短信 3:系统消息）
     */
    @TableField("notification_type")
    private Integer notificationType;

    /**
     * 通知接收人
     */
    @TableField("recipients")
    private String recipients;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField("create_by")
    private String createBy;
} 