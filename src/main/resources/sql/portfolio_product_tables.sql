-- 组合产品管理子系统数据库表结构

-- 组合产品表
CREATE TABLE IF NOT EXISTS `portfolio_product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_code` varchar(50) NOT NULL COMMENT '产品代码',
  `product_name` varchar(100) NOT NULL COMMENT '产品名称',
  `product_short_name` varchar(50) DEFAULT NULL COMMENT '产品简称',
  `product_type` varchar(50) NOT NULL COMMENT '产品类型：FOF/组合/定制',
  `risk_level` varchar(20) NOT NULL COMMENT '风险等级：低/中/高',
  `target_return` decimal(10,4) DEFAULT NULL COMMENT '目标收益率',
  `min_investment` decimal(15,2) DEFAULT NULL COMMENT '最小投资金额',
  `max_investment` decimal(15,2) DEFAULT NULL COMMENT '最大投资金额',
  `management_fee` decimal(5,4) DEFAULT NULL COMMENT '管理费率',
  `custodian_fee` decimal(5,4) DEFAULT NULL COMMENT '托管费率',
  `subscription_fee` decimal(5,4) DEFAULT NULL COMMENT '认购费率',
  `redemption_fee` decimal(5,4) DEFAULT NULL COMMENT '赎回费率',
  `benchmark` varchar(100) DEFAULT NULL COMMENT '业绩比较基准',
  `investment_strategy` text COMMENT '投资策略',
  `investment_scope` text COMMENT '投资范围',
  `product_status` varchar(20) DEFAULT 'DRAFT' COMMENT '产品状态：DRAFT-草稿/PENDING-待审核/APPROVED-已审核/ACTIVE-已上线/SUSPENDED-暂停/CLOSED-已关闭',
  `launch_date` date DEFAULT NULL COMMENT '成立日期',
  `maturity_date` date DEFAULT NULL COMMENT '到期日期',
  `creator_id` bigint NOT NULL COMMENT '创建人ID',
  `reviewer_id` bigint DEFAULT NULL COMMENT '审核人ID',
  `review_time` datetime DEFAULT NULL COMMENT '审核时间',
  `review_status` varchar(20) DEFAULT 'PENDING' COMMENT '审核状态：PENDING-待审核/APPROVED-通过/REJECTED-拒绝',
  `review_comment` text COMMENT '审核意见',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_code` (`product_code`),
  KEY `idx_creator_id` (`creator_id`),
  KEY `idx_reviewer_id` (`reviewer_id`),
  KEY `idx_product_status` (`product_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组合产品表';

-- 产品组合配置表
CREATE TABLE IF NOT EXISTS `product_portfolio_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `portfolio_id` bigint NOT NULL COMMENT '组合ID',
  `weight` decimal(5,4) NOT NULL COMMENT '权重',
  `is_active` tinyint DEFAULT 1 COMMENT '是否启用：1-启用，0-禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_portfolio` (`product_id`, `portfolio_id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_portfolio_id` (`portfolio_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品组合配置表';

-- 产品策略配置表
CREATE TABLE IF NOT EXISTS `product_strategy_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `strategy_id` bigint NOT NULL COMMENT '策略ID',
  `weight` decimal(5,4) NOT NULL COMMENT '权重',
  `is_active` tinyint DEFAULT 1 COMMENT '是否启用：1-启用，0-禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_strategy` (`product_id`, `strategy_id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_strategy_id` (`strategy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品策略配置表';

-- 产品业绩表
CREATE TABLE IF NOT EXISTS `product_performance` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `nav_date` date NOT NULL COMMENT '净值日期',
  `nav` decimal(10,4) NOT NULL COMMENT '单位净值',
  `accumulated_nav` decimal(10,4) DEFAULT NULL COMMENT '累计净值',
  `daily_return` decimal(10,4) DEFAULT NULL COMMENT '日收益率',
  `weekly_return` decimal(10,4) DEFAULT NULL COMMENT '周收益率',
  `monthly_return` decimal(10,4) DEFAULT NULL COMMENT '月收益率',
  `quarterly_return` decimal(10,4) DEFAULT NULL COMMENT '季度收益率',
  `yearly_return` decimal(10,4) DEFAULT NULL COMMENT '年收益率',
  `total_return` decimal(10,4) DEFAULT NULL COMMENT '累计收益率',
  `benchmark_return` decimal(10,4) DEFAULT NULL COMMENT '基准收益率',
  `excess_return` decimal(10,4) DEFAULT NULL COMMENT '超额收益',
  `volatility` decimal(10,4) DEFAULT NULL COMMENT '波动率',
  `sharpe_ratio` decimal(10,4) DEFAULT NULL COMMENT '夏普比率',
  `max_drawdown` decimal(10,4) DEFAULT NULL COMMENT '最大回撤',
  `calmar_ratio` decimal(10,4) DEFAULT NULL COMMENT '卡玛比率',
  `information_ratio` decimal(10,4) DEFAULT NULL COMMENT '信息比率',
  `tracking_error` decimal(10,4) DEFAULT NULL COMMENT '跟踪误差',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_nav_date` (`product_id`, `nav_date`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_nav_date` (`nav_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品业绩表';

-- 产品申购赎回记录表
CREATE TABLE IF NOT EXISTS `product_transaction` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `transaction_code` varchar(50) NOT NULL COMMENT '交易流水号',
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `transaction_type` varchar(20) NOT NULL COMMENT '交易类型：SUBSCRIPTION-申购/REDEMPTION-赎回',
  `transaction_amount` decimal(15,2) NOT NULL COMMENT '交易金额',
  `transaction_units` decimal(15,4) DEFAULT NULL COMMENT '交易份额',
  `nav` decimal(10,4) NOT NULL COMMENT '交易净值',
  `nav_date` date NOT NULL COMMENT '净值日期',
  `fee_amount` decimal(15,2) DEFAULT NULL COMMENT '手续费',
  `net_amount` decimal(15,2) DEFAULT NULL COMMENT '净金额',
  `transaction_status` varchar(20) DEFAULT 'PENDING' COMMENT '交易状态：PENDING-待确认/CONFIRMED-已确认/FAILED-失败/CANCELLED-已取消',
  `confirm_time` datetime DEFAULT NULL COMMENT '确认时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_transaction_code` (`transaction_code`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_transaction_type` (`transaction_type`),
  KEY `idx_transaction_status` (`transaction_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品申购赎回记录表';

-- 产品持仓表
CREATE TABLE IF NOT EXISTS `product_holding` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `total_units` decimal(15,4) NOT NULL COMMENT '总份额',
  `available_units` decimal(15,4) NOT NULL COMMENT '可用份额',
  `frozen_units` decimal(15,4) DEFAULT 0 COMMENT '冻结份额',
  `total_amount` decimal(15,2) NOT NULL COMMENT '总金额',
  `available_amount` decimal(15,2) NOT NULL COMMENT '可用金额',
  `frozen_amount` decimal(15,2) DEFAULT 0 COMMENT '冻结金额',
  `avg_cost` decimal(10,4) DEFAULT NULL COMMENT '平均成本',
  `current_nav` decimal(10,4) DEFAULT NULL COMMENT '当前净值',
  `market_value` decimal(15,2) DEFAULT NULL COMMENT '市值',
  `unrealized_pnl` decimal(15,2) DEFAULT NULL COMMENT '未实现盈亏',
  `realized_pnl` decimal(15,2) DEFAULT NULL COMMENT '已实现盈亏',
  `total_pnl` decimal(15,2) DEFAULT NULL COMMENT '总盈亏',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_user` (`product_id`, `user_id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品持仓表';

-- 产品文档表
CREATE TABLE IF NOT EXISTS `product_document` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `document_type` varchar(50) NOT NULL COMMENT '文档类型：PROSPECTUS-招募说明书/INVESTMENT_GUIDE-投资指南/RISK_DISCLOSURE-风险揭示书/PERFORMANCE_REPORT-业绩报告',
  `document_name` varchar(200) NOT NULL COMMENT '文档名称',
  `document_url` varchar(500) NOT NULL COMMENT '文档URL',
  `document_size` bigint DEFAULT NULL COMMENT '文档大小(字节)',
  `version` varchar(20) DEFAULT '1.0' COMMENT '版本号',
  `is_active` tinyint DEFAULT 1 COMMENT '是否有效：1-有效，0-无效',
  `upload_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `uploader_id` bigint NOT NULL COMMENT '上传人ID',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_document_type` (`document_type`),
  KEY `idx_uploader_id` (`uploader_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品文档表';

-- 调仓计划表
CREATE TABLE IF NOT EXISTS `rebalance_plan` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `plan_name` varchar(100) NOT NULL COMMENT '调仓计划名称',
  `portfolio_name` varchar(100) NOT NULL COMMENT '组合名称',
  `portfolio_type` varchar(50) NOT NULL COMMENT '组合类型',
  `current_holdings` text COMMENT '当前持仓快照(JSON)',
  `rebalance_instructions` text COMMENT '调仓指令列表(JSON)',
  `status` varchar(20) DEFAULT 'PENDING' COMMENT '计划状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='调仓计划表';

INSERT INTO `rebalance_plan` (
  `plan_name`, `portfolio_name`, `portfolio_type`, `current_holdings`, `rebalance_instructions`, `status`, `create_time`, `update_time`
) VALUES
('2024年一季度调仓', '稳健配置组合', 'FOF', '{"A股":100000,"债券":50000}', '[{"action":"买入","asset":"A股","amount":20000},{"action":"卖出","asset":"债券","amount":10000}]', 'PENDING', NOW(), NOW()),
('2024年二季度调仓', '成长进取组合', '组合', '{"A股":200000,"港股":80000}', '[{"action":"买入","asset":"港股","amount":20000}]', 'PENDING', NOW(), NOW()),
('2024年三季度调仓', '价值投资组合', '定制', '{"A股":150000,"美股":60000}', '[{"action":"卖出","asset":"A股","amount":5000}]', 'COMPLETED', NOW(), NOW());

-- 插入测试数据
INSERT INTO `portfolio_product` (`product_code`, `product_name`, `product_short_name`, `product_type`, `risk_level`, `target_return`, `min_investment`, `max_investment`, `management_fee`, `custodian_fee`, `subscription_fee`, `redemption_fee`, `benchmark`, `investment_strategy`, `investment_scope`, `product_status`, `launch_date`, `creator_id`) VALUES
('PP001', '稳健配置组合产品', '稳健配置', 'FOF', '中', 0.0800, 10000.00, 1000000.00, 0.0100, 0.0020, 0.0000, 0.0050, '沪深300指数*50%+中债总财富指数*50%', '采用资产配置策略，通过动态调整股票和债券的配置比例，在控制风险的前提下追求稳定收益。', '主要投资于股票型基金、债券型基金、货币市场基金等公募基金产品。', 'ACTIVE', '2024-01-01', 1),
('PP002', '成长进取组合产品', '成长进取', '组合', '高', 0.1500, 50000.00, 2000000.00, 0.0150, 0.0020, 0.0000, 0.0100, '沪深300指数*80%+中债总财富指数*20%', '采用成长投资策略，重点配置具有高成长性的股票型基金，追求较高的投资收益。', '主要投资于股票型基金、混合型基金，少量配置债券型基金。', 'ACTIVE', '2024-01-01', 1),
('PP003', '价值投资组合产品', '价值投资', '定制', '中', 0.1200, 20000.00, 1500000.00, 0.0120, 0.0020, 0.0000, 0.0080, '中证500指数*60%+中债总财富指数*40%', '采用价值投资策略，精选具有投资价值的基金产品，注重风险控制。', '主要投资于价值型股票基金、债券型基金，适当配置货币市场基金。', 'PENDING', '2024-02-01', 1);

-- 插入产品组合配置数据
INSERT INTO `product_portfolio_config` (`product_id`, `portfolio_id`, `weight`) VALUES
(1, 1, 0.6000),
(1, 2, 0.4000),
(2, 1, 0.3000),
(2, 2, 0.7000);

-- 插入产品策略配置数据
INSERT INTO `product_strategy_config` (`product_id`, `strategy_id`, `weight`) VALUES
(1, 7, 0.5000),
(1, 8, 0.5000),
(2, 7, 0.3000),
(2, 8, 0.7000);

-- 插入产品业绩数据
INSERT INTO `product_performance` (`product_id`, `nav_date`, `nav`, `accumulated_nav`, `daily_return`, `total_return`, `benchmark_return`, `excess_return`, `volatility`, `sharpe_ratio`, `max_drawdown`) VALUES
(1, '2024-01-15', 1.0856, 1.0856, 0.0023, 0.0856, 0.0756, 0.0100, 0.0856, 1.1256, 0.0456),
(1, '2024-01-14', 1.0833, 1.0833, 0.0018, 0.0833, 0.0733, 0.0100, 0.0833, 1.1233, 0.0433),
(2, '2024-01-15', 1.1256, 1.1256, 0.0034, 0.1256, 0.1156, 0.0100, 0.1256, 1.1656, 0.0656),
(2, '2024-01-14', 1.1222, 1.1222, 0.0029, 0.1222, 0.1122, 0.0100, 0.1222, 1.1622, 0.0622);

-- 插入产品文档数据
INSERT INTO `product_document` (`product_id`, `document_type`, `document_name`, `document_url`, `document_size`, `uploader_id`) VALUES
(1, 'PROSPECTUS', '稳健配置组合产品招募说明书.pdf', '/documents/PP001_prospectus.pdf', 1024000, 1),
(1, 'INVESTMENT_GUIDE', '稳健配置组合产品投资指南.pdf', '/documents/PP001_guide.pdf', 512000, 1),
(1, 'RISK_DISCLOSURE', '稳健配置组合产品风险揭示书.pdf', '/documents/PP001_risk.pdf', 256000, 1),
(2, 'PROSPECTUS', '成长进取组合产品招募说明书.pdf', '/documents/PP002_prospectus.pdf', 1024000, 1),
(2, 'INVESTMENT_GUIDE', '成长进取组合产品投资指南.pdf', '/documents/PP002_guide.pdf', 512000, 1);

-- 验证数据插入
SELECT 'portfolio_product' as table_name, COUNT(*) as record_count FROM portfolio_product
UNION ALL
SELECT 'product_portfolio_config', COUNT(*) FROM product_portfolio_config
UNION ALL
SELECT 'product_strategy_config', COUNT(*) FROM product_strategy_config
UNION ALL
SELECT 'product_performance', COUNT(*) FROM product_performance
UNION ALL
SELECT 'product_document', COUNT(*) FROM product_document; 