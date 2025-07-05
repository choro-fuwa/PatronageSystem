-- 因子管理数据初始化脚本
-- 执行前请确保数据库连接正常

-- 1. 创建因子分类表
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

-- 2. 清空并重新插入因子分类数据
DELETE FROM factor_category;

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

-- 3. 创建因子表
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
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='因子表';

-- 4. 清空并重新插入示例因子数据
DELETE FROM factor;

INSERT INTO factor (factor_code, factor_name, factor_short_name, category_id, factor_type, description, formula, data_source, update_frequency, risk_level, is_public, creator_id) VALUES
('MOM_20D', '20日动量因子', '20日动量', 5, 'MOMENTUM', '20日价格动量因子', 'close/close[20] - 1', '行情数据', '日', '中', 1, 1),
('VOL_20D', '20日波动率因子', '20日波动率', 6, 'VOLATILITY', '20日收益率波动率', 'std(returns, 20)', '行情数据', '日', '中', 1, 1),
('PE_RATIO', '市盈率因子', 'PE比率', 7, 'VALUE', '市盈率因子', 'price/eps', '财务数据', '季', '低', 1, 1),
('ROE', '净资产收益率因子', 'ROE', 8, 'QUALITY', '净资产收益率', 'net_income/equity', '财务数据', '季', '低', 1, 1),
('MARKET_CAP', '市值因子', '市值', 9, 'SIZE', '总市值', 'shares * price', '行情数据', '日', '低', 1, 1),
('BETA_60D', '60日Beta因子', '60日Beta', 10, 'BETA', '60日相对市场Beta', 'cov(returns, market_returns)/var(market_returns)', '行情数据', '日', '中', 1, 1),
('TURNOVER_RATE', '换手率因子', '换手率', 11, 'LIQUIDITY', '日均换手率', 'volume/shares', '行情数据', '日', '中', 1, 1);

-- 5. 验证数据插入
SELECT '因子分类数据统计' as info, COUNT(*) as count FROM factor_category;
SELECT '因子数据统计' as info, COUNT(*) as count FROM factor;

-- 6. 显示分类数据
SELECT id, category_name, category_code, parent_id, sort_order, status FROM factor_category ORDER BY sort_order, id; 