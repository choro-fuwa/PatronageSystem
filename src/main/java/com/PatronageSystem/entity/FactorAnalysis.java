package com.PatronageSystem.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 因子有效性分析实体类
 */
@Data
public class FactorAnalysis {
    
    private Long id;
    
    /**
     * 因子ID
     */
    private Long factorId;
    
    /**
     * 分析日期
     */
    private LocalDate analysisDate;
    
    /**
     * IC值
     */
    private BigDecimal icValue;
    
    /**
     * RankIC值
     */
    private BigDecimal rankIc;
    
    /**
     * 换手率
     */
    private BigDecimal turnoverRate;
    
    /**
     * 胜率
     */
    private BigDecimal winRate;
    
    /**
     * 夏普比率
     */
    private BigDecimal sharpeRatio;
    
    /**
     * 最大回撤
     */
    private BigDecimal maxDrawdown;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 