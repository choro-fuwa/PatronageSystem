package com.example.riskprofile.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * 用户实体类
 * 包含用户基本信息和风险画像数据
 */
@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 用户名
     */
    @Column(unique = true, nullable = false)
    private String username;
    
    /**
     * 密码
     */
    @Column(nullable = false)
    private String password;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 风险等级
     * low: 保守型
     * mid: 稳健型
     * high: 进取型
     */
    @Column(name = "risk_level")
    private String riskLevel;
    
    /**
     * 投资偏好周期
     * short_term: 短期
     * medium_term: 中期
     * long_term: 长期
     */
    @Column(name = "preferred_horizon")
    private String preferredHorizon;
    
    /**
     * 投资偏好产品
     * 存储为JSON格式的字符串
     */
    @Column(name = "preferred_investment", columnDefinition = "TEXT")
    private String preferredInvestment;
    
    /**
     * 创建时间
     */
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;
    
    /**
     * 获取偏好投资产品列表
     */
    @Transient
    public List<String> getPreferredInvestmentList() {
        if (preferredInvestment == null || preferredInvestment.isEmpty()) {
            return List.of();
        }
        // 这里需要根据实际的JSON格式进行解析
        // 可以使用Jackson或Gson等库
        return List.of(preferredInvestment.split(","));
    }
    
    /**
     * 设置偏好投资产品列表
     */
    @Transient
    public void setPreferredInvestmentList(List<String> products) {
        if (products == null || products.isEmpty()) {
            this.preferredInvestment = "";
        } else {
            this.preferredInvestment = String.join(",", products);
        }
    }
    
    /**
     * 获取风险等级中文描述
     */
    @Transient
    public String getRiskLevelDescription() {
        switch (riskLevel) {
            case "low":
                return "保守型";
            case "mid":
                return "稳健型";
            case "high":
                return "进取型";
            default:
                return "未知";
        }
    }
    
    /**
     * 获取投资周期中文描述
     */
    @Transient
    public String getPreferredHorizonDescription() {
        switch (preferredHorizon) {
            case "short_term":
                return "短期（1年以内）";
            case "medium_term":
                return "中期（1-5年）";
            case "long_term":
                return "长期（5年以上）";
            default:
                return "未知";
        }
    }
}