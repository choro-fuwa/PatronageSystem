package com.patronage.strategy.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 策略实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("strategy")
public class Strategy {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 策略名称
     */
    @TableField("strategy_name")
    private String strategyName;

    /**
     * 策略描述
     */
    @TableField("description")
    private String description;

    /**
     * 策略类型（1:趋势策略 2:均值回归 3:套利策略 4:量化策略）
     */
    @TableField("strategy_type")
    private Integer strategyType;

    /**
     * 策略状态（0:草稿 1:运行中 2:暂停 3:已停止）
     */
    @TableField("status")
    private Integer status;

    /**
     * 初始资金
     */
    @TableField("initial_capital")
    private BigDecimal initialCapital;

    /**
     * 当前资金
     */
    @TableField("current_capital")
    private BigDecimal currentCapital;

    /**
     * 策略参数（JSON格式）
     */
    @TableField("parameters")
    private String parameters;

    /**
     * 风险控制参数（JSON格式）
     */
    @TableField("risk_control")
    private String riskControl;

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
     * 更新人
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 逻辑删除标识（0:未删除 1:已删除）
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
} 