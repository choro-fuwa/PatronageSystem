-- 策略管理子系统数据库表结构
-- 执行前请确保数据库连接正常

-- 1. 策略表
CREATE TABLE IF NOT EXISTS strategy (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    strategy_code VARCHAR(50) NOT NULL UNIQUE COMMENT '策略代码',
    strategy_name VARCHAR(200) NOT NULL COMMENT '策略名称',
    strategy_type VARCHAR(50) NOT NULL COMMENT '策略类型：多因子、单因子、技术分析、基本面等',
    description TEXT COMMENT '策略描述',
    portfolio_id BIGINT COMMENT '关联的因子组合ID',
    risk_level VARCHAR(20) COMMENT '风险等级：低、中、高',
    target_return DECIMAL(10,4) COMMENT '目标收益率',
    max_drawdown DECIMAL(10,4) COMMENT '最大回撤限制',
    rebalance_frequency VARCHAR(20) COMMENT '调仓频率：日、周、月、季',
    benchmark VARCHAR(50) COMMENT '基准指数',
    creator_id BIGINT COMMENT '创建者ID',
    is_public TINYINT DEFAULT 1 COMMENT '是否公开：1-公开，0-私有',
    status TINYINT DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_strategy_code (strategy_code),
    INDEX idx_strategy_type (strategy_type),
    INDEX idx_creator_id (creator_id),
    INDEX idx_status (status),
    FOREIGN KEY (portfolio_id) REFERENCES factor_portfolio(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='策略表';

-- 2. 策略参数表
CREATE TABLE IF NOT EXISTS strategy_parameter (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    strategy_id BIGINT NOT NULL COMMENT '策略ID',
    param_name VARCHAR(100) NOT NULL COMMENT '参数名称',
    param_value TEXT COMMENT '参数值',
    param_type VARCHAR(20) COMMENT '参数类型：string、number、boolean、json',
    description VARCHAR(500) COMMENT '参数描述',
    is_required TINYINT DEFAULT 0 COMMENT '是否必需：1-必需，0-可选',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_strategy_id (strategy_id),
    INDEX idx_param_name (param_name),
    FOREIGN KEY (strategy_id) REFERENCES strategy(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='策略参数表';

-- 3. 策略回测结果表
CREATE TABLE IF NOT EXISTS strategy_backtest (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    strategy_id BIGINT NOT NULL COMMENT '策略ID',
    backtest_date DATE NOT NULL COMMENT '回测日期',
    start_date DATE NOT NULL COMMENT '回测开始日期',
    end_date DATE NOT NULL COMMENT '回测结束日期',
    total_return DECIMAL(10,6) COMMENT '总收益率',
    annual_return DECIMAL(10,6) COMMENT '年化收益率',
    volatility DECIMAL(10,6) COMMENT '波动率',
    sharpe_ratio DECIMAL(10,6) COMMENT '夏普比率',
    max_drawdown DECIMAL(10,6) COMMENT '最大回撤',
    win_rate DECIMAL(10,4) COMMENT '胜率',
    turnover_rate DECIMAL(10,4) COMMENT '换手率',
    benchmark_return DECIMAL(10,6) COMMENT '基准收益率',
    excess_return DECIMAL(10,6) COMMENT '超额收益率',
    information_ratio DECIMAL(10,6) COMMENT '信息比率',
    calmar_ratio DECIMAL(10,6) COMMENT '卡玛比率',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_strategy_id (strategy_id),
    INDEX idx_backtest_date (backtest_date),
    INDEX idx_start_date (start_date),
    INDEX idx_end_date (end_date),
    FOREIGN KEY (strategy_id) REFERENCES strategy(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='策略回测结果表';

-- 4. 策略持仓表
CREATE TABLE IF NOT EXISTS strategy_position (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    strategy_id BIGINT NOT NULL COMMENT '策略ID',
    fund_code VARCHAR(20) NOT NULL COMMENT '基金代码',
    fund_name VARCHAR(200) COMMENT '基金名称',
    weight DECIMAL(10,6) COMMENT '权重',
    shares DECIMAL(20,4) COMMENT '持仓份额',
    market_value DECIMAL(20,2) COMMENT '市值',
    cost_price DECIMAL(10,4) COMMENT '成本价',
    current_price DECIMAL(10,4) COMMENT '当前价',
    profit_loss DECIMAL(20,2) COMMENT '盈亏',
    profit_loss_rate DECIMAL(10,6) COMMENT '盈亏率',
    position_date DATE NOT NULL COMMENT '持仓日期',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_strategy_id (strategy_id),
    INDEX idx_fund_code (fund_code),
    INDEX idx_position_date (position_date),
    FOREIGN KEY (strategy_id) REFERENCES strategy(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='策略持仓表';

-- 5. 策略交易记录表
CREATE TABLE IF NOT EXISTS strategy_trade (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    strategy_id BIGINT NOT NULL COMMENT '策略ID',
    fund_code VARCHAR(20) NOT NULL COMMENT '基金代码',
    fund_name VARCHAR(200) COMMENT '基金名称',
    trade_type VARCHAR(10) NOT NULL COMMENT '交易类型：BUY-买入，SELL-卖出',
    trade_date DATE NOT NULL COMMENT '交易日期',
    shares DECIMAL(20,4) COMMENT '交易份额',
    price DECIMAL(10,4) COMMENT '交易价格',
    amount DECIMAL(20,2) COMMENT '交易金额',
    commission DECIMAL(10,2) COMMENT '手续费',
    reason VARCHAR(500) COMMENT '交易原因',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_strategy_id (strategy_id),
    INDEX idx_fund_code (fund_code),
    INDEX idx_trade_date (trade_date),
    INDEX idx_trade_type (trade_type),
    FOREIGN KEY (strategy_id) REFERENCES strategy(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='策略交易记录表';

-- 6. 策略监控表
CREATE TABLE IF NOT EXISTS strategy_monitor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    strategy_id BIGINT NOT NULL COMMENT '策略ID',
    monitor_date DATE NOT NULL COMMENT '监控日期',
    current_return DECIMAL(10,6) COMMENT '当前收益率',
    current_drawdown DECIMAL(10,6) COMMENT '当前回撤',
    risk_alert TINYINT DEFAULT 0 COMMENT '风险预警：1-预警，0-正常',
    alert_message TEXT COMMENT '预警信息',
    status VARCHAR(20) COMMENT '监控状态：NORMAL-正常，WARNING-预警，DANGER-危险',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_strategy_id (strategy_id),
    INDEX idx_monitor_date (monitor_date),
    INDEX idx_status (status),
    FOREIGN KEY (strategy_id) REFERENCES strategy(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='策略监控表';

-- 插入示例数据

-- 插入策略数据
INSERT INTO strategy (strategy_code, strategy_name, strategy_type, description, portfolio_id, risk_level, target_return, max_drawdown, rebalance_frequency, benchmark, creator_id) VALUES
('MULTI_FACTOR_STRATEGY', '多因子策略', '多因子', '基于价值、质量、动量的多因子策略', 1, '中', 0.1500, 0.2000, '月', '沪深300', 1),
('MOMENTUM_STRATEGY', '动量策略', '技术分析', '基于价格动量的技术分析策略', 2, '高', 0.2000, 0.2500, '周', '中证500', 1);

-- 插入策略参数数据
INSERT INTO strategy_parameter (strategy_id, param_name, param_value, param_type, description, is_required) VALUES
(7, 'lookback_period', '252', 'number', '回看期（交易日）', 1),
(7, 'rebalance_threshold', '0.05', 'number', '调仓阈值', 1),
(7, 'max_position_weight', '0.1', 'number', '最大单只基金权重', 1),
(8, 'momentum_period', '20', 'number', '动量计算周期', 1),
(8, 'volatility_period', '60', 'number', '波动率计算周期', 1);

-- 插入策略回测结果数据
INSERT INTO strategy_backtest (strategy_id, backtest_date, start_date, end_date, total_return, annual_return, volatility, sharpe_ratio, max_drawdown, win_rate, turnover_rate, benchmark_return, excess_return, information_ratio, calmar_ratio) VALUES
(7, '2024-01-15', '2023-01-01', '2024-01-15', 0.1856, 0.1568, 0.1256, 1.2456, 0.0856, 0.6256, 0.0856, 0.1256, 0.0600, 0.4856, 1.8356),
(8, '2024-01-15', '2023-01-01', '2024-01-15', 0.2256, 0.1956, 0.1856, 1.0556, 0.1256, 0.5856, 0.1256, 0.1256, 0.0700, 0.5556, 1.5556);

-- 验证数据插入
SELECT '策略数据统计' as info, COUNT(*) as count FROM strategy;
SELECT '策略参数统计' as info, COUNT(*) as count FROM strategy_parameter;
SELECT '策略回测统计' as info, COUNT(*) as count FROM strategy_backtest;
