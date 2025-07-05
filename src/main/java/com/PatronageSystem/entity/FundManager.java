package com.PatronageSystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FundManager {
    private Long id;
    private String managerCode;
    private String managerName;
    private Long companyId;
    private String education;
    private Integer experienceYears;
    private String introduction;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 关联信息
    private String companyName;
} 