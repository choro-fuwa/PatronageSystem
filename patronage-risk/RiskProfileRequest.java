package com.example.riskprofile.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * 风险画像请求DTO
 * 用于接收前端提交的风险评测问卷数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskProfileRequest {
    
    /**
     * 年龄范围
     */
    private String age;
    
    /**
     * 投资目标
     */
    private String investmentGoal;
    
    /**
     * 期望收益率
     */
    private String expectedReturn;
    
    /**
     * 赎回需求
     */
    private String redemptionNeed;
    
    /**
     * 风险接受程度
     */
    private String riskAcceptance;
    
    /**
     * 收入来源
     */
    private String incomeSource;
    
    /**
     * 年收入范围
     */
    private String annualIncome;
    
    /**
     * 投资资产占比
     */
    private String investableProportion;
    
    /**
     * 债务状况
     */
    private String debtStatus;
    
    /**
     * 投资知识水平
     */
    private String investmentKnowledge;
    
    /**
     * 投资经验
     */
    private String investmentExperience;
    
    /**
     * 投资期限
     */
    private String investmentDuration;
    
    /**
     * 偏好产品（可多选）
     */
    private List<String> preferredProducts;
    
    /**
     * 亏损容忍度
     */
    private String lossTolerance;
}