package com.patronage.strategy.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 回测结果实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("backtest_result")
public class BacktestResult {

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
     * 回测开始日期
     */
    @TableField("start_date")
    private LocalDateTime startDate;

    /**
     * 回测结束日期
     */
    @TableField("end_date")
    private LocalDateTime endDate;

    /**
     * 初始资金
     */
    @TableField("initial_capital")
    private BigDecimal initialCapital;

    /**
     * 最终资金
     */
    @TableField("final_capital")
    private BigDecimal finalCapital;

    /**
     * 总收益率
     */
    @TableField("total_return")
    private BigDecimal totalReturn;

    /**
     * 年化收益率
     */
    @TableField("annual_return")
    private BigDecimal annualReturn;

    /**
     * 最大回撤
     */
    @TableField("max_drawdown")
    private BigDecimal maxDrawdown;

    /**
     * 夏普比率
     */
    @TableField("sharpe_ratio")
    private BigDecimal sharpeRatio;

    /**
     * 胜率
     */
    @TableField("win_rate")
    private BigDecimal winRate;

    /**
     * 交易次数
     */
    @TableField("trade_count")
    private Integer tradeCount;

    /**
     * 回测参数（JSON格式）
     */
    @TableField("parameters")
    private String parameters;

    /**
     * 回测结果数据（JSON格式，包含每日收益等）
     */
    @TableField("result_data")
    private String resultData;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField("create_by")
    private String createBy;
} 