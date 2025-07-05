package com.PatronageSystem.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 产品业绩实体类
 */
@Data
public class ProductPerformance {
    
    private Long id;
    
    /**
     * 产品ID
     */
    private Long productId;
    
    /**
     * 净值日期
     */
    private LocalDate navDate;
    
    /**
     * 单位净值
     */
    private BigDecimal nav;
    
    /**
     * 累计净值
     */
    private BigDecimal accumulatedNav;
    
    /**
     * 日收益率
     */
    private BigDecimal dailyReturn;
    
    /**
     * 周收益率
     */
    private BigDecimal weeklyReturn;
    
    /**
     * 月收益率
     */
    private BigDecimal monthlyReturn;
    
    /**
     * 季度收益率
     */
    private BigDecimal quarterlyReturn;
    
    /**
     * 年收益率
     */
    private BigDecimal yearlyReturn;
    
    /**
     * 累计收益率
     */
    private BigDecimal totalReturn;
    
    /**
     * 基准收益率
     */
    private BigDecimal benchmarkReturn;
    
    /**
     * 超额收益
     */
    private BigDecimal excessReturn;
    
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
     * 卡玛比率
     */
    private BigDecimal calmarRatio;
    
    /**
     * 信息比率
     */
    private BigDecimal informationRatio;
    
    /**
     * 跟踪误差
     */
    private BigDecimal trackingError;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 