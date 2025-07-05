package com.PatronageSystem.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 因子数据实体类
 */
@Data
public class FactorData {
    
    private Long id;
    
    /**
     * 因子ID
     */
    private Long factorId;
    
    /**
     * 基金代码
     */
    private String fundCode;
    
    /**
     * 交易日期
     */
    private LocalDate tradeDate;
    
    /**
     * 因子值
     */
    private BigDecimal factorValue;
    
    /**
     * 排名值
     */
    private BigDecimal rankValue;
    
    /**
     * 百分位数
     */
    private BigDecimal percentile;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 