package com.PatronageSystem.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 产品文档实体类
 */
@Data
public class ProductDocument {
    
    private Long id;
    
    /**
     * 产品ID
     */
    private Long productId;
    
    /**
     * 文档类型：PROSPECTUS-招募说明书/INVESTMENT_GUIDE-投资指南/RISK_DISCLOSURE-风险揭示书/PERFORMANCE_REPORT-业绩报告
     */
    private String documentType;
    
    /**
     * 文档名称
     */
    private String documentName;
    
    /**
     * 文档URL
     */
    private String documentUrl;
    
    /**
     * 文档大小(字节)
     */
    private Long documentSize;
    
    /**
     * 版本号
     */
    private String version;
    
    /**
     * 是否有效：1-有效，0-无效
     */
    private Integer isActive;
    
    /**
     * 上传时间
     */
    private LocalDateTime uploadTime;
    
    /**
     * 上传人ID
     */
    private Long uploaderId;
} 