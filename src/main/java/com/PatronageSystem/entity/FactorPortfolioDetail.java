package com.PatronageSystem.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 因子组合明细实体类
 */
@Data
public class FactorPortfolioDetail {
    
    private Long id;
    
    /**
     * 组合ID
     */
    private Long portfolioId;
    
    /**
     * 因子ID
     */
    private Long factorId;
    
    /**
     * 权重
     */
    private BigDecimal weight;
    
    /**
     * 方向：1-正向，-1-反向
     */
    private Integer direction;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 