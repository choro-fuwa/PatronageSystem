package com.patronage.strategy.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 策略持仓实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("strategy_position")
public class StrategyPosition {

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
     * 股票代码
     */
    @TableField("stock_code")
    private String stockCode;

    /**
     * 股票名称
     */
    @TableField("stock_name")
    private String stockName;

    /**
     * 持仓数量
     */
    @TableField("quantity")
    private Integer quantity;

    /**
     * 持仓成本
     */
    @TableField("cost_price")
    private BigDecimal costPrice;

    /**
     * 当前价格
     */
    @TableField("current_price")
    private BigDecimal currentPrice;

    /**
     * 持仓市值
     */
    @TableField("market_value")
    private BigDecimal marketValue;

    /**
     * 浮动盈亏
     */
    @TableField("unrealized_pnl")
    private BigDecimal unrealizedPnl;

    /**
     * 持仓比例
     */
    @TableField("position_ratio")
    private BigDecimal positionRatio;

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
} 