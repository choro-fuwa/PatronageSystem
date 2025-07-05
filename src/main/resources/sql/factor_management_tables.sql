-- 因子管理子系统数据库表结构
-- 创建时间: 2024-01-01

-- 1. 因子分类表
CREATE TABLE IF NOT EXISTS factor_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    category_name VARCHAR(100) NOT NULL COMMENT '分类名称',
    category_code VARCHAR(50) NOT NULL UNIQUE COMMENT '分类代码',
    description TEXT COMMENT '分类描述',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID，0表示顶级分类',
    sort_order INT DEFAULT 0 COMMENT '排序顺序',
    status TINYINT DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_parent_id (parent_id),
    INDEX idx_category_code (category_code),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='因子分类表';

-- 2. 因子表
CREATE TABLE IF NOT EXISTS factor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    factor_code VARCHAR(50) NOT NULL UNIQUE COMMENT '因子代码',
    factor_name VARCHAR(200) NOT NULL COMMENT '因子名称',
    factor_short_name VARCHAR(100) COMMENT '因子简称',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    factor_type VARCHAR(50) COMMENT '因子类型：技术因子、基本面因子、风险因子等',
    description TEXT COMMENT '因子描述',
    formula TEXT COMMENT '因子计算公式',
    data_source VARCHAR(200) COMMENT '数据来源',
    update_frequency VARCHAR(50) COMMENT '更新频率：日、周、月等',
    risk_level VARCHAR(20) COMMENT '风险等级：低、中、高',
    is_public TINYINT DEFAULT 1 COMMENT '是否公开：1-公开，0-私有',
    creator_id BIGINT COMMENT '创建者ID',
    status TINYINT DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_category_id (category_id),
    INDEX idx_factor_code (factor_code),
    INDEX idx_factor_type (factor_type),
    INDEX idx_risk_level (risk_level),
    INDEX idx_status (status),
    FOREIGN KEY (category_id) REFERENCES factor_category(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='因子表';

-- 3. 因子数据表
CREATE TABLE IF NOT EXISTS factor_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    factor_id BIGINT NOT NULL COMMENT '因子ID',
    fund_code VARCHAR(20) NOT NULL COMMENT '基金代码',
    trade_date DATE NOT NULL COMMENT '交易日期',
    factor_value DECIMAL(20,6) COMMENT '因子值',
    rank_value DECIMAL(20,6) COMMENT '排名值',
    percentile DECIMAL(10,4) COMMENT '百分位数',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_factor_id (factor_id),
    INDEX idx_fund_code (fund_code),
    INDEX idx_trade_date (trade_date),
    INDEX idx_factor_fund_date (factor_id, fund_code, trade_date),
    FOREIGN KEY (factor_id) REFERENCES factor(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='因子数据表';

-- 4. 因子组合表
CREATE TABLE IF NOT EXISTS factor_portfolio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    portfolio_name VARCHAR(200) NOT NULL COMMENT '组合名称',
    portfolio_code VARCHAR(50) NOT NULL UNIQUE COMMENT '组合代码',
    description TEXT COMMENT '组合描述',
    strategy_type VARCHAR(50) COMMENT '策略类型：多因子、单因子等',
    creator_id BIGINT COMMENT '创建者ID',
    is_public TINYINT DEFAULT 1 COMMENT '是否公开：1-公开，0-私有',
    status TINYINT DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_portfolio_code (portfolio_code),
    INDEX idx_creator_id (creator_id),
    INDEX idx_strategy_type (strategy_type),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='因子组合表';

-- 5. 因子组合明细表
CREATE TABLE IF NOT EXISTS factor_portfolio_detail (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    portfolio_id BIGINT NOT NULL COMMENT '组合ID',
    factor_id BIGINT NOT NULL COMMENT '因子ID',
    weight DECIMAL(10,4) DEFAULT 1.0000 COMMENT '权重',
    direction TINYINT DEFAULT 1 COMMENT '方向：1-正向，-1-反向',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_portfolio_id (portfolio_id),
    INDEX idx_factor_id (factor_id),
    INDEX idx_portfolio_factor (portfolio_id, factor_id),
    FOREIGN KEY (portfolio_id) REFERENCES factor_portfolio(id),
    FOREIGN KEY (factor_id) REFERENCES factor(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='因子组合明细表';

-- 6. 因子有效性分析表
CREATE TABLE IF NOT EXISTS factor_analysis (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    factor_id BIGINT NOT NULL COMMENT '因子ID',
    analysis_date DATE NOT NULL COMMENT '分析日期',
    ic_value DECIMAL(10,6) COMMENT 'IC值',
    rank_ic DECIMAL(10,6) COMMENT 'RankIC值',
    turnover_rate DECIMAL(10,4) COMMENT '换手率',
    win_rate DECIMAL(10,4) COMMENT '胜率',
    sharpe_ratio DECIMAL(10,4) COMMENT '夏普比率',
    max_drawdown DECIMAL(10,4) COMMENT '最大回撤',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_factor_id (factor_id),
    INDEX idx_analysis_date (analysis_date),
    INDEX idx_factor_date (factor_id, analysis_date),
    FOREIGN KEY (factor_id) REFERENCES factor(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='因子有效性分析表';

-- 插入初始数据

-- 插入因子分类数据
INSERT INTO factor_category (category_name, category_code, description, parent_id, sort_order) VALUES
('技术因子', 'TECHNICAL', '基于价格和交易量的技术分析因子', 0, 1),
('基本面因子', 'FUNDAMENTAL', '基于公司基本面的因子', 0, 2),
('风险因子', 'RISK', '风险控制相关因子', 0, 3),
('宏观因子', 'MACRO', '宏观经济相关因子', 0, 4),
('动量因子', 'MOMENTUM', '技术因子-动量类', 1, 1),
('波动率因子', 'VOLATILITY', '技术因子-波动率类', 1, 2),
('价值因子', 'VALUE', '基本面因子-价值类', 2, 1),
('质量因子', 'QUALITY', '基本面因子-质量类', 2, 2),
('规模因子', 'SIZE', '基本面因子-规模类', 2, 3),
('Beta因子', 'BETA', '风险因子-Beta类', 3, 1),
('流动性因子', 'LIQUIDITY', '风险因子-流动性类', 3, 2),
('利率因子', 'INTEREST_RATE', '宏观因子-利率类', 4, 1),
('汇率因子', 'EXCHANGE_RATE', '宏观因子-汇率类', 4, 2);

-- 插入示例因子数据
INSERT INTO factor (factor_code, factor_name, factor_short_name, category_id, factor_type, description, formula, data_source, update_frequency, risk_level, is_public, creator_id) VALUES
('MOM_20D', '20日动量因子', '20日动量', 5, 'MOMENTUM', '20日价格动量因子', 'close/close[20] - 1', '行情数据', '日', '中', 1, 1),
('VOL_20D', '20日波动率因子', '20日波动率', 6, 'VOLATILITY', '20日收益率波动率', 'std(returns, 20)', '行情数据', '日', '中', 1, 1),
('PE_RATIO', '市盈率因子', 'PE比率', 7, 'VALUE', '市盈率因子', 'price/eps', '财务数据', '季', '低', 1, 1),
('ROE', '净资产收益率因子', 'ROE', 8, 'QUALITY', '净资产收益率', 'net_income/equity', '财务数据', '季', '低', 1, 1),
('MARKET_CAP', '市值因子', '市值', 9, 'SIZE', '总市值', 'shares * price', '行情数据', '日', '低', 1, 1),
('BETA_60D', '60日Beta因子', '60日Beta', 10, 'BETA', '60日相对市场Beta', 'cov(returns, market_returns)/var(market_returns)', '行情数据', '日', '中', 1, 1),
('TURNOVER_RATE', '换手率因子', '换手率', 11, 'LIQUIDITY', '日均换手率', 'volume/shares', '行情数据', '日', '中', 1, 1);

-- 插入示例因子组合数据
INSERT INTO factor_portfolio (portfolio_name, portfolio_code, description, strategy_type, creator_id, is_public) VALUES
('多因子价值组合', 'MULTI_FACTOR_VALUE', '基于价值因子的多因子组合', 'MULTI_FACTOR', 1, 1),
('动量策略组合', 'MOMENTUM_STRATEGY', '基于动量因子的策略组合', 'SINGLE_FACTOR', 1, 1),
('风险平价组合', 'RISK_PARITY', '风险平价配置的多因子组合', 'RISK_PARITY', 1, 1);

-- 插入示例因子组合明细数据
INSERT INTO factor_portfolio_detail (portfolio_id, factor_id, weight, direction) VALUES
(1, 3, 0.4000, 1),  -- PE因子，权重40%，正向
(1, 4, 0.3000, 1),  -- ROE因子，权重30%，正向
(1, 5, 0.3000, -1), -- 市值因子，权重30%，反向
(2, 1, 1.0000, 1),  -- 动量因子，权重100%，正向
(3, 1, 0.2500, 1),  -- 动量因子，权重25%，正向
(3, 2, 0.2500, -1), -- 波动率因子，权重25%，反向
(3, 6, 0.2500, 1),  -- Beta因子，权重25%，正向
(3, 7, 0.2500, -1); -- 换手率因子，权重25%，反向 