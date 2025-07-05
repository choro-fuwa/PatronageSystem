package com.PatronageSystem.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 策略交易记录实体类
 */
@Data
public class StrategyTrade {
    
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
     * 交易类型：BUY-买入，SELL-卖出
     */
    private String tradeType;
    
    /**
     * 交易日期
     */
    private LocalDate tradeDate;
    
    /**
     * 交易份额
     */
    private BigDecimal shares;
    
    /**
     * 交易价格
     */
    private BigDecimal price;
    
    /**
     * 交易金额
     */
    private BigDecimal amount;
    
    /**
     * 手续费
     */
    private BigDecimal commission;
    
    /**
     * 交易原因
     */
    private String reason;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    // 关联信息
    private String strategyName;
    private String strategyCode;
} 