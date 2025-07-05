package com.patronage.strategy.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 策略监控实体类
 */
@Data
public class StrategyMonitor {

    private Long id;

    /**
     * 策略ID
     */
    private Long strategyId;

    /**
     * 监控日期
     */
    private LocalDate monitorDate;

    /**
     * 当前收益率
     */
    private BigDecimal currentReturn;

    /**
     * 当前回撤
     */
    private BigDecimal currentDrawdown;

    /**
     * 风险预警：1-预警，0-正常
     */
    private Integer riskAlert;

    /**
     * 预警信息
     */
    private String alertMessage;

    /**
     * 监控状态：NORMAL-正常，WARNING-预警，DANGER-危险
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    // 关联信息
    private String strategyName;
    private String strategyCode;
}
