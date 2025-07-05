package com.PatronageSystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 因子组合实体类
 */
@Data
public class FactorPortfolio {
    
    private Long id;
    
    /**
     * 组合名称
     */
    private String portfolioName;
    
    /**
     * 组合代码
     */
    private String portfolioCode;
    
    /**
     * 组合描述
     */
    private String description;
    
    /**
     * 策略类型
     */
    private String strategyType;
    
    /**
     * 创建者ID
     */
    private Long creatorId;
    
    /**
     * 是否公开：1-公开，0-私有
     */
    private Integer isPublic;
    
    /**
     * 状态：1-正常，0-禁用
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 