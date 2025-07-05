package com.PatronageSystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 策略参数实体类
 */
@Data
public class StrategyParameter {
    
    private Long id;
    
    /**
     * 策略ID
     */
    private Long strategyId;
    
    /**
     * 参数名称
     */
    private String paramName;
    
    /**
     * 参数值
     */
    private String paramValue;
    
    /**
     * 参数类型：string、number、boolean、json
     */
    private String paramType;
    
    /**
     * 参数描述
     */
    private String description;
    
    /**
     * 是否必需：1-必需，0-可选
     */
    private Integer isRequired;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 