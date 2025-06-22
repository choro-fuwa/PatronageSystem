package com.patronage.strategy.model;

/**
 * 策略状态枚举
 */
public enum StrategyStatus {
    CREATED("已创建", "策略已创建但尚未开始回测"),
    BACKTESTING("回测中", "策略正在进行历史回测"),
    SIMULATING("模拟中", "策略处于模拟运行状态"),
    RUNNING("运行中", "策略处于实盘运行状态"),
    PAUSED("已暂停", "策略运行已暂停"),
    STOPPED("已停止", "策略已停止运行");

    private final String name;
    private final String description;

    StrategyStatus(String name, String description) {
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