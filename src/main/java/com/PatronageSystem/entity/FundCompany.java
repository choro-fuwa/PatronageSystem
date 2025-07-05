package com.PatronageSystem.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class FundCompany {
    private Long id;
    private String companyCode;
    private String companyName;
    private String companyShortName;
    private LocalDate establishDate;
    private BigDecimal registeredCapital;
    private String legalRepresentative;
    private String companyType;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 