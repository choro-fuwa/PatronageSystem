package src.java.com.patronage.strategy.entity;

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
     * 预警类型
     */
    @TableField("alert_type")
    private Integer alertType;

    /**
     * 预警消息
     */
    @TableField("alert_message")
    private String alertMessage;

    /**
     * 触发值
     */
    @TableField("trigger_value")
    private BigDecimal triggerValue;

    /**
     * 阈值
     */
    @TableField("threshold")
    private BigDecimal threshold;

    /**
     * 通知状态（0:未通知 1:已通知）
     */
    @TableField("notification_status")
    private Integer notificationStatus;

    /**
     * 通知时间
     */
    @TableField("notification_time")
    private LocalDateTime notificationTime;

    /**
     * 处理状态（0:未处理 1:已处理）
     */
    @TableField("process_status")
    private Integer processStatus;

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
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
} 