package main.java.com.patronage.product.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 组合产品实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PortfolioProduct {

    private Long id;

    /**
     * 产品代码
     */
    private String productCode;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品简称
     */
    private String productShortName;

    /**
     * 产品类型：FOF/组合/定制
     */
    private String productType;

    /**
     * 风险等级：低/中/高
     */
    private String riskLevel;

    /**
     * 目标收益率
     */
    private BigDecimal targetReturn;

    /**
     * 最小投资金额
     */
    private BigDecimal minInvestment;

    /**
     * 最大投资金额
     */
    private BigDecimal maxInvestment;

    /**
     * 管理费率
     */
    private BigDecimal managementFee;

    /**
     * 托管费率
     */
    private BigDecimal custodianFee;

    /**
     * 认购费率
     */
    private BigDecimal subscriptionFee;

    /**
     * 赎回费率
     */
    private BigDecimal redemptionFee;

    /**
     * 业绩比较基准
     */
    private String benchmark;

    /**
     * 投资策略
     */
    private String investmentStrategy;

    /**
     * 投资范围
     */
    private String investmentScope;

    /**
     * 产品状态：DRAFT-草稿/PENDING-待审核/APPROVED-已审核/ACTIVE-已上线/SUSPENDED-暂停/CLOSED-已关闭
     */
    private String productStatus;

    /**
     * 成立日期
     */
    private LocalDate launchDate;

    /**
     * 到期日期
     */
    private LocalDate maturityDate;

    /**
     * 创建人ID
     */
    private Long creatorId;

    /**
     * 审核人ID
     */
    private Long reviewerId;

    /**
     * 审核时间
     */
    private LocalDateTime reviewTime;

    /**
     * 审核状态：PENDING-待审核/APPROVED-通过/REJECTED-拒绝
     */
    private String reviewStatus;

    /**
     * 审核意见
     */
    private String reviewComment;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
