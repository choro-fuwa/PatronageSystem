package com.example.riskprofile.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 风险画像响应DTO
 * 用于返回风险评测结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskProfileResponse {
    
    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 风险评分
     */
    private Integer riskScore;
    
    /**
     * 风险等级
     */
    private String riskLevel;
    
    /**
     * 风险等级描述
     */
    private String riskLevelDescription;
    
    /**
     * 错误代码
     */
    private String errorCode;
    
    /**
     * 时间戳
     */
    private Long timestamp;
    
    /**
     * 创建成功响应
     */
    public static RiskProfileResponse success(String message, Integer riskScore, String riskLevel) {
        return RiskProfileResponse.builder()
                .success(true)
                .message(message)
                .riskScore(riskScore)
                .riskLevel(riskLevel)
                .riskLevelDescription(getRiskLevelDescription(riskLevel))
                .timestamp(System.currentTimeMillis())
                .build();
    }
    
    /**
     * 创建失败响应
     */
    public static RiskProfileResponse error(String message, String errorCode) {
        return RiskProfileResponse.builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .timestamp(System.currentTimeMillis())
                .build();
    }
    
    /**
     * 获取风险等级描述
     */
    private static String getRiskLevelDescription(String riskLevel) {
        switch (riskLevel) {
            case "low":
                return "保守型 - 适合低风险投资，偏好稳定收益";
            case "mid":
                return "稳健型 - 适合中等风险投资，平衡收益与风险";
            case "high":
                return "进取型 - 适合高风险投资，追求高收益";
            default:
                return "未知风险等级";
        }
    }
}