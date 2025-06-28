package com.patronage.strategy.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
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
     * 回测名称
     */
    @TableField("backtest_name")
    private String backtestName;

    /**
     * 回测开始日期
     */
    @TableField("start_date")
    private LocalDate startDate;

    /**
     * 回测结束日期
     */
    @TableField("end_date")
    private LocalDate endDate;

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
     * 总交易次数
     */
    @TableField("total_trades")
    private Integer totalTrades;

    /**
     * 盈利交易次数
     */
    @TableField("winning_trades")
    private Integer winningTrades;

    /**
     * 平均持仓时间（天）
     */
    @TableField("avg_holding_days")
    private BigDecimal avgHoldingDays;

    /**
     * 回测参数（JSON格式）
     */
    @TableField("parameters")
    private String parameters;

    /**
     * 回测结果详情（JSON格式）
     */
    @TableField("result_details")
    private String resultDetails;

    /**
     * 回测状态（0:进行中 1:已完成 2:失败）
     */
    @TableField("status")
    private Integer status;

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