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

-- 插入策略测试数据
INSERT INTO strategy (strategy_name, description, strategy_type, status, initial_capital, current_capital, max_drawdown, annual_return, sharpe_ratio, create_by) VALUES
('趋势跟踪策略A', '基于移动平均线的趋势跟踪策略，适用于单边行情', 1, 1, 100000.00, 105000.00, 0.0500, 0.1200, 1.25, 'admin'),
('均值回归策略B', '基于布林带的均值回归策略，适用于震荡行情', 2, 0, 200000.00, 200000.00, 0.0200, 0.0800, 0.95, 'admin'),
('多因子策略C', '基于多因子模型的选股策略，综合多个因子', 4, 2, 150000.00, 148000.00, 0.0800, 0.1500, 1.35, 'admin'),
('套利策略D', '基于价差的套利策略，低风险稳定收益', 3, 1, 300000.00, 306000.00, 0.0100, 0.0600, 2.10, 'admin'),
('动量策略E', '基于价格动量的策略，捕捉强势股票', 1, 1, 120000.00, 128000.00, 0.0600, 0.1800, 1.45, 'admin'),
('价值策略F', '基于价值因子的策略，寻找低估股票', 4, 0, 180000.00, 180000.00, 0.0300, 0.1000, 1.15, 'admin'),
('高频策略G', '基于高频数据的策略，快速进出', 1, 3, 50000.00, 48000.00, 0.1200, 0.2500, 0.85, 'admin'),
('量化对冲策略H', '基于对冲的量化策略，降低系统性风险', 4, 1, 500000.00, 515000.00, 0.0400, 0.0900, 1.80, 'admin'),
('网格交易策略I', '基于网格的交易策略，适合震荡市场', 2, 1, 80000.00, 82000.00, 0.0700, 0.1400, 1.20, 'admin'),
('事件驱动策略J', '基于事件驱动的策略，捕捉市场机会', 4, 0, 250000.00, 250000.00, 0.0500, 0.1100, 1.30, 'admin');

-- 插入策略持仓测试数据
INSERT INTO strategy_position (strategy_id, stock_code, stock_name, quantity, cost_price, current_price, market_value, unrealized_pnl, position_ratio) VALUES
-- 策略1的持仓
(1, '000001', '平安银行', 1000, 12.50, 13.20, 13200.00, 700.00, 0.1257),
(1, '000002', '万科A', 800, 18.80, 19.50, 15600.00, 560.00, 0.1486),
(1, '600036', '招商银行', 500, 45.20, 44.80, 22400.00, -200.00, 0.2133),
(1, '000858', '五粮液', 300, 180.50, 185.00, 55500.00, 1350.00, 0.5286),

-- 策略2的持仓
(2, '600519', '贵州茅台', 100, 1800.00, 1750.00, 175000.00, -5000.00, 0.8750),
(2, '000858', '五粮液', 200, 185.00, 182.00, 36400.00, -600.00, 0.1820),

-- 策略3的持仓
(3, '000001', '平安银行', 1500, 12.80, 13.20, 19800.00, 600.00, 0.1338),
(3, '600036', '招商银行', 800, 44.50, 44.80, 35840.00, 240.00, 0.2422),
(3, '000002', '万科A', 600, 19.20, 19.50, 11700.00, 180.00, 0.0791),
(3, '600519', '贵州茅台', 50, 1780.00, 1750.00, 87500.00, -1500.00, 0.5912),

-- 策略4的持仓
(4, '000001', '平安银行', 2000, 12.60, 13.20, 26400.00, 1200.00, 0.0863),
(4, '000002', '万科A', 1500, 18.90, 19.50, 29250.00, 900.00, 0.0956),
(4, '600036', '招商银行', 1000, 44.80, 44.80, 44800.00, 0.00, 0.1464),
(4, '600519', '贵州茅台', 80, 1790.00, 1750.00, 140000.00, -3200.00, 0.4575),

-- 策略5的持仓
(5, '000001', '平安银行', 1200, 12.40, 13.20, 15840.00, 960.00, 0.1238),
(5, '000002', '万科A', 1000, 18.60, 19.50, 19500.00, 900.00, 0.1523),
(5, '600036', '招商银行', 600, 44.60, 44.80, 26880.00, 120.00, 0.2100),
(5, '000858', '五粮液', 400, 181.00, 185.00, 74000.00, 1600.00, 0.5781),

-- 策略8的持仓
(8, '000001', '平安银行', 3000, 12.70, 13.20, 39600.00, 1500.00, 0.0769),
(8, '000002', '万科A', 2500, 18.85, 19.50, 48750.00, 1625.00, 0.0947),
(8, '600036', '招商银行', 2000, 44.75, 44.80, 89600.00, 100.00, 0.1740),
(8, '600519', '贵州茅台', 150, 1785.00, 1750.00, 262500.00, -5250.00, 0.5097),

-- 策略9的持仓
(9, '000001', '平安银行', 800, 12.55, 13.20, 10560.00, 520.00, 0.1288),
(9, '000002', '万科A', 600, 18.75, 19.50, 11700.00, 450.00, 0.1427),
(9, '600036', '招商银行', 400, 44.70, 44.80, 17920.00, 40.00, 0.2185),
(9, '000858', '五粮液', 200, 182.00, 185.00, 37000.00, 600.00, 0.4512);

-- 插入回测结果测试数据
INSERT INTO backtest_result (strategy_id, start_date, end_date, initial_capital, final_capital, total_return, annual_return, max_drawdown, sharpe_ratio, win_rate, trade_count, create_by) VALUES
(1, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 100000.00, 105000.00, 0.0500, 0.0500, 0.0500, 1.25, 0.65, 45, 'admin'),
(1, '2023-01-01 00:00:00', '2023-12-31 23:59:59', 100000.00, 108000.00, 0.0800, 0.0800, 0.0400, 1.35, 0.70, 52, 'admin'),
(2, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 200000.00, 210000.00, 0.0500, 0.0500, 0.0150, 1.35, 0.70, 38, 'admin'),
(2, '2023-01-01 00:00:00', '2023-12-31 23:59:59', 200000.00, 204000.00, 0.0200, 0.0200, 0.0250, 0.85, 0.55, 42, 'admin'),
(3, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 150000.00, 148000.00, -0.0133, -0.0133, 0.0800, 0.45, 0.40, 28, 'admin'),
(3, '2023-01-01 00:00:00', '2023-12-31 23:59:59', 150000.00, 162000.00, 0.0800, 0.0800, 0.0600, 1.20, 0.65, 35, 'admin'),
(4, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 300000.00, 306000.00, 0.0200, 0.0200, 0.0100, 2.10, 0.85, 15, 'admin'),
(4, '2023-01-01 00:00:00', '2023-12-31 23:59:59', 300000.00, 309000.00, 0.0300, 0.0300, 0.0080, 2.25, 0.90, 12, 'admin'),
(5, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 120000.00, 128000.00, 0.0667, 0.0667, 0.0600, 1.45, 0.75, 68, 'admin'),
(5, '2023-01-01 00:00:00', '2023-12-31 23:59:59', 120000.00, 115000.00, -0.0417, -0.0417, 0.1000, 0.35, 0.45, 72, 'admin'),
(6, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 180000.00, 180000.00, 0.0000, 0.0000, 0.0300, 0.00, 0.50, 25, 'admin'),
(7, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 50000.00, 48000.00, -0.0400, -0.0400, 0.1200, 0.85, 0.60, 156, 'admin'),
(8, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 500000.00, 515000.00, 0.0300, 0.0300, 0.0400, 1.80, 0.80, 22, 'admin'),
(9, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 80000.00, 82000.00, 0.0250, 0.0250, 0.0700, 1.20, 0.70, 45, 'admin'),
(10, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 250000.00, 250000.00, 0.0000, 0.0000, 0.0500, 0.00, 0.50, 18, 'admin');

-- 插入策略预警测试数据
INSERT INTO strategy_alert (strategy_id, alert_name, alert_type, condition_type, threshold, is_enabled, notification_type, recipients, create_by) VALUES
-- 策略1的预警规则
(1, '收益率预警', 1, 2, -0.05, 1, 1, 'admin@patronage.com', 'admin'),
(1, '回撤预警', 2, 1, 0.10, 1, 3, 'admin', 'admin'),
(1, '波动率预警', 3, 1, 0.30, 1, 2, '13800138000', 'admin'),

-- 策略2的预警规则
(2, '收益率预警', 1, 2, -0.03, 1, 1, 'admin@patronage.com', 'admin'),
(2, '回撤预警', 2, 1, 0.08, 1, 3, 'admin', 'admin'),

-- 策略3的预警规则
(3, '收益率预警', 1, 2, -0.08, 1, 1, 'admin@patronage.com', 'admin'),
(3, '回撤预警', 2, 1, 0.12, 1, 3, 'admin', 'admin'),
(3, '持仓集中度预警', 4, 1, 0.60, 1, 2, '13800138000', 'admin'),

-- 策略4的预警规则
(4, '收益率预警', 1, 2, -0.02, 1, 1, 'admin@patronage.com', 'admin'),
(4, '回撤预警', 2, 1, 0.05, 1, 3, 'admin', 'admin'),

-- 策略5的预警规则
(5, '收益率预警', 1, 2, -0.06, 1, 1, 'admin@patronage.com', 'admin'),
(5, '回撤预警', 2, 1, 0.10, 1, 3, 'admin', 'admin'),
(5, '波动率预警', 3, 1, 0.35, 1, 2, '13800138000', 'admin'),

-- 策略6的预警规则
(6, '收益率预警', 1, 2, -0.05, 1, 1, 'admin@patronage.com', 'admin'),
(6, '回撤预警', 2, 1, 0.08, 1, 3, 'admin', 'admin'),

-- 策略7的预警规则
(7, '收益率预警', 1, 2, -0.10, 1, 1, 'admin@patronage.com', 'admin'),
(7, '回撤预警', 2, 1, 0.15, 1, 3, 'admin', 'admin'),
(7, '波动率预警', 3, 1, 0.40, 1, 2, '13800138000', 'admin'),

-- 策略8的预警规则
(8, '收益率预警', 1, 2, -0.03, 1, 1, 'admin@patronage.com', 'admin'),
(8, '回撤预警', 2, 1, 0.06, 1, 3, 'admin', 'admin'),
(8, '持仓集中度预警', 4, 1, 0.50, 1, 2, '13800138000', 'admin'),

-- 策略9的预警规则
(9, '收益率预警', 1, 2, -0.05, 1, 1, 'admin@patronage.com', 'admin'),
(9, '回撤预警', 2, 1, 0.10, 1, 3, 'admin', 'admin'),

-- 策略10的预警规则
(10, '收益率预警', 1, 2, -0.05, 1, 1, 'admin@patronage.com', 'admin'),
(10, '回撤预警', 2, 1, 0.08, 1, 3, 'admin', 'admin');

-- 插入预警历史测试数据
INSERT INTO alert_history (strategy_id, alert_id, alert_type, alert_message, trigger_value, threshold, notification_status, process_status) VALUES
-- 策略1的预警历史
(1, 1, 1, '策略收益率低于-5%', -0.0520, -0.05, 1, 0),
(1, 2, 2, '策略回撤超过10%', 0.1050, 0.10, 1, 1),
(1, 3, 3, '策略波动率超过30%', 0.3200, 0.30, 1, 0),

-- 策略2的预警历史
(2, 4, 1, '策略收益率低于-3%', -0.0350, -0.03, 1, 1),
(2, 5, 2, '策略回撤超过8%', 0.0850, 0.08, 1, 0),

-- 策略3的预警历史
(3, 6, 1, '策略收益率低于-8%', -0.0820, -0.08, 1, 0),
(3, 7, 2, '策略回撤超过12%', 0.1250, 0.12, 1, 1),
(3, 8, 4, '策略持仓集中度超过60%', 0.6200, 0.60, 1, 0),

-- 策略4的预警历史
(4, 9, 1, '策略收益率低于-2%', -0.0250, -0.02, 1, 1),
(4, 10, 2, '策略回撤超过5%', 0.0550, 0.05, 1, 0),

-- 策略5的预警历史
(5, 11, 1, '策略收益率低于-6%', -0.0620, -0.06, 1, 0),
(5, 12, 2, '策略回撤超过10%', 0.1050, 0.10, 1, 1),
(5, 13, 3, '策略波动率超过35%', 0.3600, 0.35, 1, 0),

-- 策略6的预警历史
(6, 14, 1, '策略收益率低于-5%', -0.0520, -0.05, 1, 1),
(6, 15, 2, '策略回撤超过8%', 0.0850, 0.08, 1, 0),

-- 策略7的预警历史
(7, 16, 1, '策略收益率低于-10%', -0.1020, -0.10, 1, 0),
(7, 17, 2, '策略回撤超过15%', 0.1550, 0.15, 1, 1),
(7, 18, 3, '策略波动率超过40%', 0.4200, 0.40, 1, 0),

-- 策略8的预警历史
(8, 19, 1, '策略收益率低于-3%', -0.0350, -0.03, 1, 1),
(8, 20, 2, '策略回撤超过6%', 0.0650, 0.06, 1, 0),
(8, 21, 4, '策略持仓集中度超过50%', 0.5200, 0.50, 1, 0),

-- 策略9的预警历史
(9, 22, 1, '策略收益率低于-5%', -0.0520, -0.05, 1, 1),
(9, 23, 2, '策略回撤超过10%', 0.1050, 0.10, 1, 0),

-- 策略10的预警历史
(10, 24, 1, '策略收益率低于-5%', -0.0520, -0.05, 1, 0),
(10, 25, 2, '策略回撤超过8%', 0.0850, 0.08, 1, 1); 