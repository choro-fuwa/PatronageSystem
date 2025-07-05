package com.PatronageSystem.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 策略持仓实体类
 */
@Data
public class StrategyPosition {
    
    private Long id;
    
    /**
     * 策略ID
     */
    private Long strategyId;
    
    /**
     * 基金代码
     */
    private String fundCode;
    
    /**
     * 基金名称
     */
    private String fundName;
    
    /**
     * 权重
     */
    private BigDecimal weight;
    
    /**
     * 持仓份额
     */
    private BigDecimal shares;
    
    /**
     * 市值
     */
    private BigDecimal marketValue;
    
    /**
     * 成本价
     */
    private BigDecimal costPrice;
    
    /**
     * 当前价
     */
    private BigDecimal currentPrice;
    
    /**
     * 盈亏
     */
    private BigDecimal profitLoss;
    
    /**
     * 盈亏率
     */
    private BigDecimal profitLossRate;
    
    /**
     * 持仓日期
     */
    private LocalDate positionDate;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    // 关联信息
    private String strategyName;
    private String strategyCode;
} 