package com.PatronageSystem.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Fund {
    private Long id;
    private String fundCode;
    private String fundName;
    private String fundShortName;
    private Long companyId;
    private Long managerId;
    private String fundType;
    private String riskLevel;
    private LocalDate establishDate;
    private BigDecimal fundSize;
    private BigDecimal nav;
    private LocalDate navDate;
    private BigDecimal totalReturn;
    private BigDecimal annualReturn;
    private BigDecimal maxDrawdown;
    private BigDecimal sharpeRatio;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 关联信息
    private String companyName;
    private String managerName;
    private List<String> tags;
} 