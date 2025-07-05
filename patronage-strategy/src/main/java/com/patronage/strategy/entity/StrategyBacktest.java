package com.patronage.strategy.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 策略回测实体类
 */
@Data
public class StrategyBacktest {

    private Long id;

    /**
     * 策略ID
     */
    private Long strategyId;

    /**
     * 回测日期
     */
    private LocalDate backtestDate;

    /**
     * 回测开始日期
     */
    private LocalDate startDate;

    /**
     * 回测结束日期
     */
    private LocalDate endDate;

    /**
     * 总收益率
     */
    private BigDecimal totalReturn;

    /**
     * 年化收益率
     */
    private BigDecimal annualReturn;

    /**
     * 波动率
     */
    private BigDecimal volatility;

    /**
     * 夏普比率
     */
    private BigDecimal sharpeRatio;

    /**
     * 最大回撤
     */
    private BigDecimal maxDrawdown;

    /**
     * 胜率
     */
    private BigDecimal winRate;

    /**
     * 换手率
     */
    private BigDecimal turnoverRate;

    /**
     * 基准收益率
     */
    private BigDecimal benchmarkReturn;

    /**
     * 超额收益率
     */
    private BigDecimal excessReturn;

    /**
     * 信息比率
     */
    private BigDecimal informationRatio;

    /**
     * 卡玛比率
     */
    private BigDecimal calmarRatio;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    // 关联信息
    private String strategyName;
    private String strategyCode;
}
