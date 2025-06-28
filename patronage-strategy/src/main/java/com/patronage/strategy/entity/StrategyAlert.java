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
     * 预警类型（1:收益率预警 2:回撤预警 3:持仓预警 4:资金预警）
     */
    @TableField("alert_type")
    private Integer alertType;

    /**
     * 预警条件（JSON格式）
     */
    @TableField("alert_condition")
    private String alertCondition;

    /**
     * 预警阈值
     */
    @TableField("threshold")
    private BigDecimal threshold;

    /**
     * 预警级别（1:低 2:中 3:高）
     */
    @TableField("alert_level")
    private Integer alertLevel;

    /**
     * 通知方式（1:邮件 2:短信 3:WebSocket 4:全部）
     */
    @TableField("notification_type")
    private Integer notificationType;

    /**
     * 通知接收人
     */
    @TableField("recipients")
    private String recipients;

    /**
     * 是否启用（0:禁用 1:启用）
     */
    @TableField("enabled")
    private Integer enabled;

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

    /**
     * 逻辑删除标识（0:未删除 1:已删除）
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
} 