package com.PatronageSystem.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FundPortfolioDetail {
    private Long id;
    private Long portfolioId;
    private Long fundId;
    private BigDecimal weight;
    private LocalDateTime createTime;
    
    // 关联信息
    private String fundCode;
    private String fundName;
    private String fundShortName;
} 