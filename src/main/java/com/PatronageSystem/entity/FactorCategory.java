package com.PatronageSystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 因子分类实体类
 */
@Data
public class FactorCategory {
    
    private Long id;
    
    /**
     * 分类名称
     */
    private String categoryName;
    
    /**
     * 分类代码
     */
    private String categoryCode;
    
    /**
     * 分类描述
     */
    private String description;
    
    /**
     * 父分类ID，0表示顶级分类
     */
    private Long parentId;
    
    /**
     * 排序
     */
    private Integer sortOrder;
    
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