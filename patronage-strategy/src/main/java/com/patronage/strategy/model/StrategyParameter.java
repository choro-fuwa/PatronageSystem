package com.patronage.strategy.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * 策略参数模型
 */
@Data
public class StrategyParameter {
    // 大类资产配置策略参数
    @Data
    public static class AssetAllocationParams {
        private List<AssetClass> assetClasses;      // 资产类别列表
        private Map<String, Double> weights;        // 资产权重配置
        private List<String> boundFunds;            // 绑定的基金列表
        private RebalanceRule rebalanceRule;        // 再平衡规则
    }

    // FOF组合策略参数
    @Data
    public static class FofParams {
        private List<String> fundPool;              // 基金池
        private Map<String, Double> weightLimits;   // 权重限制
        private String optimizationModel;           // 优化模型（风险平价/均值方差）
        private Map<String, Object> constraints;    // 约束条件
    }

    // 基金指数策略参数
    @Data
    public static class FundIndexParams {
        private List<String> constituents;          // 指数成分基金
        private String weightingMethod;             // 权重计算方法
        private Double rebalanceThreshold;          // 再平衡阈值
    }

    // 择时策略参数
    @Data
    public static class TimingParams {
        private List<String> signals;               // 信号源列表
        private List<TradingRule> tradingRules;     // 交易规则
        private List<String> tradingTargets;        // 交易标的
    }

    // 资产类别
    @Data
    public static class AssetClass {
        private String name;        // 资产类别名称
        private String type;        // 类型（股票/债券/商品等）
        private String market;      // 市场（A股/港股/美股等）
    }

    // 再平衡规则
    @Data
    public static class RebalanceRule {
        private String type;        // 再平衡类型（定期/阈值触发）
        private String period;      // 定期再平衡周期
        private Double threshold;   // 阈值触发点
    }

    // 交易规则
    @Data
    public static class TradingRule {
        private String signal;      // 信号指标
        private String condition;   // 条件（如>、<、=）
        private Double threshold;   // 阈值
        private String action;      // 操作（加仓/减仓）
        private Double percentage;  // 操作比例
    }
}