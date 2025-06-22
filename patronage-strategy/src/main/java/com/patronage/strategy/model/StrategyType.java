package com.patronage.strategy.model;

/**
 * 策略类型枚举
 */
public enum StrategyType {
    ASSET_ALLOCATION("大类资产配置", "多资产类别动态配置策略"),
    FOF("FOF组合", "多基金组合管理策略"),
    FUND_INDEX("基金指数", "自定义指数化配置策略"),
    TIMING("择时组合", "基于信号的动态调整策略");

    private final String name;
    private final String description;

    StrategyType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}