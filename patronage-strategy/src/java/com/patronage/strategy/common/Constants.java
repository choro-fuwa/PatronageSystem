package com.patronage.strategy.common;

/**
 * 系统常量类
 */
public class Constants {

    /**
     * 策略状态
     */
    public static class StrategyStatus {
        public static final Integer DRAFT = 0;      // 草稿
        public static final Integer RUNNING = 1;    // 运行中
        public static final Integer PAUSED = 2;     // 暂停
        public static final Integer STOPPED = 3;    // 已停止
    }

    /**
     * 策略类型
     */
    public static class StrategyType {
        public static final Integer TREND_FOLLOWING = 1;  // 趋势跟踪
        public static final Integer MEAN_REVERSION = 2;   // 均值回归
        public static final Integer ARBITRAGE = 3;        // 套利
        public static final Integer MULTI_FACTOR = 4;     // 多因子
    }

    /**
     * 预警类型
     */
    public static class AlertType {
        public static final Integer RETURN_RATE = 1;      // 收益率
        public static final Integer DRAWDOWN = 2;         // 回撤
        public static final Integer VOLATILITY = 3;       // 波动率
        public static final Integer CONCENTRATION = 4;    // 持仓集中度
    }

    /**
     * 预警条件
     */
    public static class ConditionType {
        public static final Integer GREATER_THAN = 1;     // 大于
        public static final Integer LESS_THAN = 2;        // 小于
        public static final Integer EQUAL = 3;            // 等于
    }

    /**
     * 通知方式
     */
    public static class NotificationType {
        public static final Integer EMAIL = 1;            // 邮件
        public static final Integer SMS = 2;              // 短信
        public static final Integer SYSTEM = 3;           // 系统消息
    }

    /**
     * 处理状态
     */
    public static class ProcessStatus {
        public static final Integer UNPROCESSED = 0;      // 未处理
        public static final Integer PROCESSED = 1;        // 已处理
    }

    /**
     * 通知状态
     */
    public static class NotificationStatus {
        public static final Integer UNNOTIFIED = 0;       // 未通知
        public static final Integer NOTIFIED = 1;         // 已通知
    }

    /**
     * 是否删除
     */
    public static class Deleted {
        public static final Integer NO = 0;               // 未删除
        public static final Integer YES = 1;              // 已删除
    }

    /**
     * 是否启用
     */
    public static class Enabled {
        public static final Integer DISABLED = 0;         // 禁用
        public static final Integer ENABLED = 1;          // 启用
    }

    /**
     * 响应状态码
     */
    public static class ResponseCode {
        public static final Integer SUCCESS = 200;        // 成功
        public static final Integer BAD_REQUEST = 400;    // 请求错误
        public static final Integer UNAUTHORIZED = 401;   // 未授权
        public static final Integer FORBIDDEN = 403;      // 禁止访问
        public static final Integer NOT_FOUND = 404;      // 未找到
        public static final Integer INTERNAL_ERROR = 500; // 内部错误
    }

    /**
     * 缓存键前缀
     */
    public static class CachePrefix {
        public static final String STRATEGY = "strategy:";
        public static final String BACKTEST = "backtest:";
        public static final String ALERT = "alert:";
        public static final String USER = "user:";
    }

    /**
     * 时间格式
     */
    public static class DateFormat {
        public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
        public static final String DATE = "yyyy-MM-dd";
        public static final String TIME = "HH:mm:ss";
    }
} 