package com.PatronageSystem.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 策略实体类
 */
@Data
public class Strategy {
    
    private Long id;
    
    /**
     * 策略代码
     */
    private String strategyCode;
    
    /**
     * 策略名称
     */
    private String strategyName;
    
    /**
     * 策略类型：多因子、单因子、技术分析、基本面等
     */
    private String strategyType;
    
    /**
     * 策略描述
     */
    private String description;
    
    /**
     * 关联的因子组合ID
     */
    private Long portfolioId;
    
    /**
     * 风险等级：低、中、高
     */
    private String riskLevel;
    
    /**
     * 目标收益率
     */
    private BigDecimal targetReturn;
    
    /**
     * 最大回撤限制
     */
    private BigDecimal maxDrawdown;
    
    /**
     * 调仓频率：日、周、月、季
     */
    private String rebalanceFrequency;
    
    /**
     * 基准指数
     */
    private String benchmark;
    
    /**
     * 创建者ID
     */
    private Long creatorId;
    
    /**
     * 是否公开：1-公开，0-私有
     */
    private Integer isPublic;
    
    /**
     * 状态：1-启用，0-禁用
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
    
    // 关联信息
    private String portfolioName;
    private String creatorName;
} 