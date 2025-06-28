package com.patronage.strategy.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 预警历史实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("alert_history")
public class AlertHistory {

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
     * 预警规则ID
     */
    @TableField("alert_id")
    private Long alertId;

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
     * 预警级别（1:低 2:中 3:高）
     */
    @TableField("alert_level")
    private Integer alertLevel;

    /**
     * 触发值
     */
    @TableField("trigger_value")
    private BigDecimal triggerValue;

    /**
     * 预警阈值
     */
    @TableField("threshold")
    private BigDecimal threshold;

    /**
     * 预警内容
     */
    @TableField("alert_content")
    private String alertContent;

    /**
     * 通知方式（1:邮件 2:短信 3:WebSocket 4:全部）
     */
    @TableField("notification_type")
    private Integer notificationType;

    /**
     * 通知状态（0:未发送 1:已发送 2:发送失败）
     */
    @TableField("notification_status")
    private Integer notificationStatus;

    /**
     * 处理状态（0:未处理 1:已处理 2:已忽略）
     */
    @TableField("process_status")
    private Integer processStatus;

    /**
     * 处理人
     */
    @TableField("process_by")
    private String processBy;

    /**
     * 处理时间
     */
    @TableField("process_time")
    private LocalDateTime processTime;

    /**
     * 处理备注
     */
    @TableField("process_remark")
    private String processRemark;

    /**
     * 触发时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 逻辑删除标识（0:未删除 1:已删除）
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
} 