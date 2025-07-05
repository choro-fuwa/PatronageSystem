package com.PatronageSystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 因子实体类
 */
@Data
public class Factor {
    
    private Long id;
    
    /**
     * 因子代码
     */
    private String factorCode;
    
    /**
     * 因子名称
     */
    private String factorName;
    
    /**
     * 因子简称
     */
    private String factorShortName;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 因子类型：技术因子、基本面因子、宏观因子等
     */
    private String factorType;
    
    /**
     * 因子描述
     */
    private String description;
    
    /**
     * 因子计算公式
     */
    private String formula;
    
    /**
     * 数据来源
     */
    private String dataSource;
    
    /**
     * 更新频率：daily-日频，weekly-周频，monthly-月频
     */
    private String updateFrequency;
    
    /**
     * 风险等级
     */
    private String riskLevel;
    
    /**
     * 是否公开：1-公开，0-私有
     */
    private Integer isPublic;
    
    /**
     * 创建者ID
     */
    private Long creatorId;
    
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