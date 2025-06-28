-- 策略管理子系统数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS patronage_strategy DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE patronage_strategy;

-- 策略表
CREATE TABLE IF NOT EXISTS strategy (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    strategy_name VARCHAR(100) NOT NULL COMMENT '策略名称',
    description TEXT COMMENT '策略描述',
    strategy_type INT NOT NULL COMMENT '策略类型（1:趋势跟踪 2:均值回归 3:套利 4:多因子）',
    status INT NOT NULL DEFAULT 0 COMMENT '策略状态（0:草稿 1:运行中 2:暂停 3:已停止）',
    initial_capital DECIMAL(15,2) NOT NULL COMMENT '初始资金',
    current_capital DECIMAL(15,2) COMMENT '当前资金',
    max_drawdown DECIMAL(10,4) COMMENT '最大回撤',
    annual_return DECIMAL(10,4) COMMENT '年化收益率',
    sharpe_ratio DECIMAL(10,4) COMMENT '夏普比率',
    parameters TEXT COMMENT '策略参数（JSON格式）',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    is_deleted INT NOT NULL DEFAULT 0 COMMENT '是否删除（0:未删除 1:已删除）',
    INDEX idx_strategy_name (strategy_name),
    INDEX idx_strategy_type (strategy_type),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='策略表';

-- 策略持仓表
CREATE TABLE IF NOT EXISTS strategy_position (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    strategy_id BIGINT NOT NULL COMMENT '策略ID',
    stock_code VARCHAR(20) NOT NULL COMMENT '股票代码',
    stock_name VARCHAR(100) NOT NULL COMMENT '股票名称',
    quantity INT NOT NULL COMMENT '持仓数量',
    cost_price DECIMAL(10,2) NOT NULL COMMENT '持仓成本',
    current_price DECIMAL(10,2) COMMENT '当前价格',
    market_value DECIMAL(15,2) COMMENT '持仓市值',
    unrealized_pnl DECIMAL(15,2) COMMENT '浮动盈亏',
    position_ratio DECIMAL(10,4) COMMENT '持仓比例',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_strategy_id (strategy_id),
    INDEX idx_stock_code (stock_code),
    FOREIGN KEY (strategy_id) REFERENCES strategy(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='策略持仓表';

-- 回测结果表
CREATE TABLE IF NOT EXISTS backtest_result (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    strategy_id BIGINT NOT NULL COMMENT '策略ID',
    start_date DATETIME NOT NULL COMMENT '回测开始日期',
    end_date DATETIME NOT NULL COMMENT '回测结束日期',
    initial_capital DECIMAL(15,2) NOT NULL COMMENT '初始资金',
    final_capital DECIMAL(15,2) NOT NULL COMMENT '最终资金',
    total_return DECIMAL(10,4) NOT NULL COMMENT '总收益率',
    annual_return DECIMAL(10,4) COMMENT '年化收益率',
    max_drawdown DECIMAL(10,4) COMMENT '最大回撤',
    sharpe_ratio DECIMAL(10,4) COMMENT '夏普比率',
    win_rate DECIMAL(10,4) COMMENT '胜率',
    trade_count INT COMMENT '交易次数',
    parameters TEXT COMMENT '回测参数（JSON格式）',
    result_data TEXT COMMENT '回测结果数据（JSON格式，包含每日收益等）',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by VARCHAR(50) COMMENT '创建人',
    INDEX idx_strategy_id (strategy_id),
    INDEX idx_start_date (start_date),
    INDEX idx_end_date (end_date),
    INDEX idx_create_time (create_time),
    FOREIGN KEY (strategy_id) REFERENCES strategy(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='回测结果表';

-- 策略预警表
CREATE TABLE IF NOT EXISTS strategy_alert (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    strategy_id BIGINT NOT NULL COMMENT '策略ID',
    alert_name VARCHAR(100) NOT NULL COMMENT '预警名称',
    alert_type INT NOT NULL COMMENT '预警类型（1:收益率 2:回撤 3:波动率 4:持仓集中度）',
    condition_type INT NOT NULL COMMENT '预警条件（1:大于 2:小于 3:等于）',
    threshold DECIMAL(10,4) NOT NULL COMMENT '预警阈值',
    is_enabled INT NOT NULL DEFAULT 1 COMMENT '是否启用（0:禁用 1:启用）',
    notification_type INT NOT NULL COMMENT '通知方式（1:邮件 2:短信 3:系统消息）',
    recipients VARCHAR(500) COMMENT '通知接收人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    INDEX idx_strategy_id (strategy_id),
    INDEX idx_alert_type (alert_type),
    INDEX idx_is_enabled (is_enabled),
    FOREIGN KEY (strategy_id) REFERENCES strategy(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='策略预警表';

-- 预警历史表
CREATE TABLE IF NOT EXISTS alert_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    strategy_id BIGINT NOT NULL COMMENT '策略ID',
    alert_id BIGINT NOT NULL COMMENT '预警规则ID',
    alert_type INT NOT NULL COMMENT '预警类型',
    alert_message TEXT NOT NULL COMMENT '预警消息',
    trigger_value DECIMAL(10,4) COMMENT '触发值',
    threshold DECIMAL(10,4) COMMENT '阈值',
    notification_status INT NOT NULL DEFAULT 0 COMMENT '通知状态（0:未通知 1:已通知）',
    notification_time DATETIME COMMENT '通知时间',
    process_status INT NOT NULL DEFAULT 0 COMMENT '处理状态（0:未处理 1:已处理）',
    process_time DATETIME COMMENT '处理时间',
    process_remark TEXT COMMENT '处理备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_strategy_id (strategy_id),
    INDEX idx_alert_id (alert_id),
    INDEX idx_alert_type (alert_type),
    INDEX idx_process_status (process_status),
    INDEX idx_create_time (create_time),
    FOREIGN KEY (strategy_id) REFERENCES strategy(id) ON DELETE CASCADE,
    FOREIGN KEY (alert_id) REFERENCES strategy_alert(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警历史表';

-- 插入测试数据
INSERT INTO strategy (strategy_name, description, strategy_type, status, initial_capital, current_capital, create_by) VALUES
('趋势跟踪策略A', '基于移动平均线的趋势跟踪策略', 1, 1, 100000.00, 105000.00, 'admin'),
('均值回归策略B', '基于布林带的均值回归策略', 2, 0, 200000.00, 200000.00, 'admin'),
('多因子策略C', '基于多因子模型的选股策略', 4, 2, 150000.00, 148000.00, 'admin');

-- 插入测试持仓数据
INSERT INTO strategy_position (strategy_id, stock_code, stock_name, quantity, cost_price, current_price, market_value, unrealized_pnl, position_ratio) VALUES
(1, '000001', '平安银行', 1000, 12.50, 13.20, 13200.00, 700.00, 0.1257),
(1, '000002', '万科A', 800, 18.80, 19.50, 15600.00, 560.00, 0.1486),
(2, '600036', '招商银行', 500, 45.20, 44.80, 22400.00, -200.00, 0.1120);

-- 插入测试回测结果
INSERT INTO backtest_result (strategy_id, start_date, end_date, initial_capital, final_capital, total_return, annual_return, max_drawdown, sharpe_ratio, win_rate, trade_count, create_by) VALUES
(1, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 100000.00, 105000.00, 0.0500, 0.0500, -0.0200, 1.25, 0.65, 45, 'admin'),
(2, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 200000.00, 210000.00, 0.0500, 0.0500, -0.0150, 1.35, 0.70, 38, 'admin');

-- 插入测试预警规则
INSERT INTO strategy_alert (strategy_id, alert_name, alert_type, condition_type, threshold, is_enabled, notification_type, recipients, create_by) VALUES
(1, '收益率预警', 1, 2, -0.05, 1, 1, 'admin@patronage.com', 'admin'),
(1, '回撤预警', 2, 1, 0.10, 1, 3, 'admin', 'admin'),
(2, '波动率预警', 3, 1, 0.30, 1, 2, '13800138000', 'admin');

-- 插入测试预警历史
INSERT INTO alert_history (strategy_id, alert_id, alert_type, alert_message, trigger_value, threshold, notification_status, process_status) VALUES
(1, 1, 1, '策略收益率低于-5%', -0.0520, -0.05, 1, 0),
(1, 2, 2, '策略回撤超过10%', 0.1050, 0.10, 1, 1); 