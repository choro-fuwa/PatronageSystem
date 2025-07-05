package com.PatronageSystem.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FundPortfolio {
    private Long id;
    private String portfolioName;
    private Long userId;
    private String portfolioType;
    private String description;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 关联信息
    private String userName;
    private List<FundPortfolioDetail> details;
} 