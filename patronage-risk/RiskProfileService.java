package com.example.riskprofile.service;

import com.example.riskprofile.dto.RiskProfileRequest;
import com.example.riskprofile.dto.RiskProfileResponse;
import com.example.riskprofile.entity.User;
import com.example.riskprofile.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 风险画像服务类
 * 负责风险评分计算和用户画像更新
 */
@Service
public class RiskProfileService {

    @Autowired
    private UserRepository userRepository;

    // 年龄权重配置
    private static final Map<String, Integer> AGE_WEIGHTS = Map.of(
            "under_18", 0,
            "18_30", 30,
            "31_40", 25,
            "41_50", 20,
            "51_64", 10,
            "65_above", 5
    );

    // 投资目标权重配置
    private static final Map<String, Integer> GOAL_WEIGHTS = Map.of(
            "education_retirement", 10,
            "personal_goals", 20,
            "wealth_growth", 30
    );

    // 期望收益率权重配置
    private static final Map<String, Integer> RETURN_WEIGHTS = Map.of(
            "same_as_deposit", 5,
            "slightly_higher", 15,
            "much_higher", 30
    );

    // 赎回需求权重配置
    private static final Map<String, Integer> REDEMPTION_WEIGHTS = Map.of(
            "yes", 0,
            "not_sure", 10,
            "no", 20
    );

    // 风险接受程度权重配置
    private static final Map<String, Integer> RISK_ACCEPTANCE_WEIGHTS = Map.of(
            "no", 0,
            "yes", 30
    );

    // 收入来源权重配置
    private static final Map<String, Integer> INCOME_SOURCE_WEIGHTS = Map.of(
            "no_fixed_income", 0,
            "non_financial_income", 10,
            "salary", 15,
            "business_income", 20,
            "financial_income", 25
    );

    // 年收入权重配置
    private static final Map<String, Integer> INCOME_WEIGHTS = Map.of(
            "under_50k", 5,
            "50k_200k", 15,
            "200k_above", 30
    );

    // 投资资产占比权重配置
    private static final Map<String, Integer> PROPORTION_WEIGHTS = Map.of(
            "under_20%", 10,
            "20%-50%", 20,
            "above_50%", 30
    );

    // 债务状况权重配置
    private static final Map<String, Integer> DEBT_WEIGHTS = Map.of(
            "high", 0,
            "manageable", 10,
            "none", 20
    );

    // 投资知识权重配置
    private static final Map<String, Integer> KNOWLEDGE_WEIGHTS = Map.of(
            "none", 0,
            "basic", 15,
            "proficient", 30
    );

    // 投资经验权重配置
    private static final Map<String, Integer> EXPERIENCE_WEIGHTS = Map.of(
            "none", 0,
            "limited", 15,
            "rich", 30
    );

    // 投资期限权重配置
    private static final Map<String, Integer> DURATION_WEIGHTS = Map.of(
            "short_term", 10,
            "medium_term", 20,
            "long_term", 30
    );

    // 偏好产品权重配置
    private static final Map<String, Integer> PRODUCT_WEIGHTS = Map.of(
            "savings", 5,
            "fund", 15,
            "stocks", 20,
            "futures", 30,
            "others", 10
    );

    // 亏损容忍度权重配置
    private static final Map<String, Integer> LOSS_TOLERANCE_WEIGHTS = Map.of(
            "very_sensitive", 0,
            "neutral", 15,
            "risk_taker", 30
    );

    /**
     * 计算风险画像
     * @param request 风险画像请求数据
     * @return 风险画像响应结果
     */
    public RiskProfileResponse calculateRiskProfile(RiskProfileRequest request) {
        // 验证必填字段
        validateRequest(request);

        // 计算风险评分
        int riskScore = calculateRiskScore(request);

        // 确定风险等级
        String riskLevel = determineRiskLevel(riskScore);

        // 更新用户画像（这里简化处理，实际应该根据用户ID更新）
        updateUserProfile(request, riskLevel);

        return RiskProfileResponse.builder()
                .success(true)
                .message("问卷提交成功")
                .riskScore(riskScore)
                .riskLevel(riskLevel)
                .build();
    }

    /**
     * 验证请求数据
     */
    private void validateRequest(RiskProfileRequest request) {
        if (request.getAge() == null || request.getAge().isEmpty()) {
            throw new IllegalArgumentException("年龄不能为空");
        }
        if (request.getInvestmentGoal() == null || request.getInvestmentGoal().isEmpty()) {
            throw new IllegalArgumentException("投资目标不能为空");
        }
        if (request.getExpectedReturn() == null || request.getExpectedReturn().isEmpty()) {
            throw new IllegalArgumentException("期望回报不能为空");
        }
        if (request.getRedemptionNeed() == null || request.getRedemptionNeed().isEmpty()) {
            throw new IllegalArgumentException("赎回需求不能为空");
        }
        if (request.getRiskAcceptance() == null || request.getRiskAcceptance().isEmpty()) {
            throw new IllegalArgumentException("风险接受程度不能为空");
        }
        if (request.getIncomeSource() == null || request.getIncomeSource().isEmpty()) {
            throw new IllegalArgumentException("收入来源不能为空");
        }
        if (request.getAnnualIncome() == null || request.getAnnualIncome().isEmpty()) {
            throw new IllegalArgumentException("年收入不能为空");
        }
        if (request.getInvestableProportion() == null || request.getInvestableProportion().isEmpty()) {
            throw new IllegalArgumentException("投资资产占比不能为空");
        }
        if (request.getDebtStatus() == null || request.getDebtStatus().isEmpty()) {
            throw new IllegalArgumentException("债务状况不能为空");
        }
        if (request.getInvestmentKnowledge() == null || request.getInvestmentKnowledge().isEmpty()) {
            throw new IllegalArgumentException("投资知识不能为空");
        }
        if (request.getInvestmentExperience() == null || request.getInvestmentExperience().isEmpty()) {
            throw new IllegalArgumentException("投资经验不能为空");
        }
        if (request.getInvestmentDuration() == null || request.getInvestmentDuration().isEmpty()) {
            throw new IllegalArgumentException("投资期限不能为空");
        }
        if (request.getPreferredProducts() == null || request.getPreferredProducts().isEmpty()) {
            throw new IllegalArgumentException("偏好产品不能为空");
        }
        if (request.getLossTolerance() == null || request.getLossTolerance().isEmpty()) {
            throw new IllegalArgumentException("亏损容忍度不能为空");
        }
    }

    /**
     * 计算风险评分
     * @param request 风险画像请求数据
     * @return 风险评分
     */
    private int calculateRiskScore(RiskProfileRequest request) {
        int riskScore = 0;

        // 1. 根据年龄范围加分
        riskScore += AGE_WEIGHTS.getOrDefault(request.getAge(), 0);

        // 2. 根据投资目标加分
        riskScore += GOAL_WEIGHTS.getOrDefault(request.getInvestmentGoal(), 0);

        // 3. 根据期望收益率加分
        riskScore += RETURN_WEIGHTS.getOrDefault(request.getExpectedReturn(), 0);

        // 4. 根据是否有赎回需求加分
        riskScore += REDEMPTION_WEIGHTS.getOrDefault(request.getRedemptionNeed(), 0);

        // 5. 根据对不保本产品的风险接受程度加分
        riskScore += RISK_ACCEPTANCE_WEIGHTS.getOrDefault(request.getRiskAcceptance(), 0);

        // 6. 根据主要收入来源加分
        riskScore += INCOME_SOURCE_WEIGHTS.getOrDefault(request.getIncomeSource(), 0);

        // 7. 根据年收入范围加分
        riskScore += INCOME_WEIGHTS.getOrDefault(request.getAnnualIncome(), 0);

        // 8. 根据投资资产占比加分
        riskScore += PROPORTION_WEIGHTS.getOrDefault(request.getInvestableProportion(), 0);

        // 9. 根据当前债务状况加分
        riskScore += DEBT_WEIGHTS.getOrDefault(request.getDebtStatus(), 0);

        // 10. 根据投资知识水平加分
        riskScore += KNOWLEDGE_WEIGHTS.getOrDefault(request.getInvestmentKnowledge(), 0);

        // 11. 根据投资经验加分
        riskScore += EXPERIENCE_WEIGHTS.getOrDefault(request.getInvestmentExperience(), 0);

        // 12. 根据投资期限加分
        riskScore += DURATION_WEIGHTS.getOrDefault(request.getInvestmentDuration(), 0);

        // 13. 根据偏好产品加分(可多选)
        for (String product : request.getPreferredProducts()) {
            riskScore += PRODUCT_WEIGHTS.getOrDefault(product, 0);
        }

        // 14. 根据对亏损的容忍度加分
        riskScore += LOSS_TOLERANCE_WEIGHTS.getOrDefault(request.getLossTolerance(), 0);

        return riskScore;
    }

    /**
     * 确定风险等级
     * @param riskScore 风险评分
     * @return 风险等级
     */
    private String determineRiskLevel(int riskScore) {
        if (riskScore < 150) {
            return "low";
        } else if (riskScore < 250) {
            return "mid";
        } else {
            return "high";
        }
    }

    /**
     * 更新用户画像
     * @param request 风险画像请求数据
     * @param riskLevel 风险等级
     */
    private void updateUserProfile(RiskProfileRequest request, String riskLevel) {
        // 这里简化处理，实际应该根据用户ID查找并更新用户信息
        // 在实际应用中，需要：
        // 1. 从JWT token或session中获取用户ID
        // 2. 查找用户记录
        // 3. 更新用户的风险画像信息
        // 4. 保存到数据库

        User user = new User();
        user.setRiskLevel(riskLevel);
        user.setPreferredHorizon(request.getInvestmentDuration());
        user.setPreferredInvestment(request.getPreferredProducts());

        // 保存用户信息（这里简化处理）
        System.out.println("更新用户画像: " + user);
    }
}