-- 因子管理子系统数据库表

-- 因子分类表
CREATE TABLE IF NOT EXISTS `factor_category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_name` varchar(50) NOT NULL COMMENT '分类名称',
  `category_code` varchar(20) NOT NULL COMMENT '分类代码',
  `description` varchar(200) DEFAULT NULL COMMENT '分类描述',
  `parent_id` bigint DEFAULT 0 COMMENT '父分类ID，0表示顶级分类',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `status` tinyint DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_category_code` (`category_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='因子分类表';

-- 因子表
CREATE TABLE IF NOT EXISTS `factor` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `factor_code` varchar(50) NOT NULL COMMENT '因子代码',
  `factor_name` varchar(100) NOT NULL COMMENT '因子名称',
  `factor_short_name` varchar(50) DEFAULT NULL COMMENT '因子简称',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `factor_type` varchar(50) DEFAULT NULL COMMENT '因子类型：技术因子、基本面因子、宏观因子等',
  `description` text COMMENT '因子描述',
  `formula` text COMMENT '因子计算公式',
  `data_source` varchar(100) DEFAULT NULL COMMENT '数据来源',
  `update_frequency` varchar(20) DEFAULT 'daily' COMMENT '更新频率：daily-日频，weekly-周频，monthly-月频',
  `risk_level` varchar(20) DEFAULT NULL COMMENT '风险等级',
  `is_public` tinyint DEFAULT 1 COMMENT '是否公开：1-公开，0-私有',
  `creator_id` bigint DEFAULT NULL COMMENT '创建者ID',
  `status` tinyint DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_factor_code` (`factor_code`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_creator_id` (`creator_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='因子表';

-- 因子数据表
CREATE TABLE IF NOT EXISTS `factor_data` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `factor_id` bigint NOT NULL COMMENT '因子ID',
  `fund_code` varchar(20) NOT NULL COMMENT '基金代码',
  `trade_date` date NOT NULL COMMENT '交易日期',
  `factor_value` decimal(20,6) DEFAULT NULL COMMENT '因子值',
  `rank_value` decimal(10,4) DEFAULT NULL COMMENT '排名值',
  `percentile` decimal(5,2) DEFAULT NULL COMMENT '百分位数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_factor_fund_date` (`factor_id`, `fund_code`, `trade_date`),
  KEY `idx_factor_id` (`factor_id`),
  KEY `idx_fund_code` (`fund_code`),
  KEY `idx_trade_date` (`trade_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='因子数据表';

-- 因子组合表
CREATE TABLE IF NOT EXISTS `factor_portfolio` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `portfolio_name` varchar(100) NOT NULL COMMENT '组合名称',
  `portfolio_code` varchar(50) NOT NULL COMMENT '组合代码',
  `description` text COMMENT '组合描述',
  `strategy_type` varchar(50) DEFAULT NULL COMMENT '策略类型',
  `creator_id` bigint DEFAULT NULL COMMENT '创建者ID',
  `is_public` tinyint DEFAULT 1 COMMENT '是否公开：1-公开，0-私有',
  `status` tinyint DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_portfolio_code` (`portfolio_code`),
  KEY `idx_creator_id` (`creator_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='因子组合表';

-- 因子组合明细表
CREATE TABLE IF NOT EXISTS `factor_portfolio_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `portfolio_id` bigint NOT NULL COMMENT '组合ID',
  `factor_id` bigint NOT NULL COMMENT '因子ID',
  `weight` decimal(5,4) DEFAULT NULL COMMENT '权重',
  `direction` tinyint DEFAULT 1 COMMENT '方向：1-正向，-1-反向',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_portfolio_factor` (`portfolio_id`, `factor_id`),
  KEY `idx_portfolio_id` (`portfolio_id`),
  KEY `idx_factor_id` (`factor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='因子组合明细表';

-- 因子有效性分析表
CREATE TABLE IF NOT EXISTS `factor_analysis` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `factor_id` bigint NOT NULL COMMENT '因子ID',
  `analysis_date` date NOT NULL COMMENT '分析日期',
  `ic_value` decimal(10,6) DEFAULT NULL COMMENT 'IC值',
  `rank_ic` decimal(10,6) DEFAULT NULL COMMENT 'RankIC值',
  `turnover_rate` decimal(10,4) DEFAULT NULL COMMENT '换手率',
  `win_rate` decimal(5,2) DEFAULT NULL COMMENT '胜率',
  `sharpe_ratio` decimal(10,4) DEFAULT NULL COMMENT '夏普比率',
  `max_drawdown` decimal(10,4) DEFAULT NULL COMMENT '最大回撤',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_factor_date` (`factor_id`, `analysis_date`),
  KEY `idx_factor_id` (`factor_id`),
  KEY `idx_analysis_date` (`analysis_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='因子有效性分析表';

-- 插入测试数据
INSERT INTO `factor_category` (`category_name`, `category_code`, `description`, `parent_id`, `sort_order`) VALUES
('技术因子', 'TECH', '基于价格和交易量的技术指标', 0, 1),
('基本面因子', 'FUNDAMENTAL', '基于公司基本面的指标', 0, 2),
('宏观因子', 'MACRO', '基于宏观经济环境的指标', 0, 3),
('动量因子', 'MOMENTUM', '价格动量相关指标', 1, 1),
('波动率因子', 'VOLATILITY', '波动率相关指标', 1, 2),
('价值因子', 'VALUE', '估值相关指标', 2, 1),
('质量因子', 'QUALITY', '公司质量相关指标', 2, 2);

INSERT INTO `factor` (`factor_code`, `factor_name`, `factor_short_name`, `category_id`, `factor_type`, `description`, `formula`, `data_source`, `update_frequency`, `risk_level`) VALUES
('MOM_20D', '20日动量因子', '20日动量', 4, '动量因子', '20日价格动量', 'close_price / close_price_20d_ago - 1', '价格数据', 'daily', '中风险'),
('VOL_60D', '60日波动率因子', '60日波动率', 5, '波动率因子', '60日收益率标准差', 'std(returns_60d)', '价格数据', 'daily', '中风险'),
('PE_RATIO', '市盈率因子', 'PE', 6, '价值因子', '市盈率倒数', '1 / pe_ratio', '财务数据', 'monthly', '低风险'),
('ROE', '净资产收益率因子', 'ROE', 7, '质量因子', '净资产收益率', 'net_profit / equity', '财务数据', 'quarterly', '低风险'),
('BETA', '贝塔因子', '贝塔', 5, '波动率因子', '相对市场指数的贝塔系数', 'cov(returns, market_returns) / var(market_returns)', '价格数据', 'daily', '中风险');

INSERT INTO `factor_portfolio` (`portfolio_name`, `portfolio_code`, `description`, `strategy_type`, `creator_id`) VALUES
('经典多因子模型', 'CLASSIC_MULTI', '基于价值、质量、动量的经典多因子模型', '多因子', 1),
('动量增强模型', 'MOMENTUM_ENHANCED', '以动量因子为主的增强模型', '动量策略', 1); 