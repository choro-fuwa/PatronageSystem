-- 策略管理子系统数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS patronage_strategy DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE patronage_strategy;

-- 策略表
CREATE TABLE IF NOT EXISTS `strategy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `strategy_name` varchar(100) NOT NULL COMMENT '策略名称',
  `description` text COMMENT '策略描述',
  `strategy_type` int(11) NOT NULL COMMENT '策略类型（1:趋势策略 2:均值回归 3:套利策略 4:量化策略）',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '策略状态（0:草稿 1:运行中 2:暂停 3:已停止）',
  `initial_capital` decimal(15,2) NOT NULL COMMENT '初始资金',
  `current_capital` decimal(15,2) NOT NULL COMMENT '当前资金',
  `parameters` text COMMENT '策略参数（JSON格式）',
  `risk_control` text COMMENT '风险控制参数（JSON格式）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标识（0:未删除 1:已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_strategy_name` (`strategy_name`),
  KEY `idx_strategy_type` (`strategy_type`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='策略表';

-- 策略持仓表
CREATE TABLE IF NOT EXISTS `strategy_position` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `strategy_id` bigint(20) NOT NULL COMMENT '策略ID',
  `stock_code` varchar(20) NOT NULL COMMENT '股票代码',
  `stock_name` varchar(100) NOT NULL COMMENT '股票名称',
  `quantity` int(11) NOT NULL COMMENT '持仓数量',
  `cost_price` decimal(10,2) NOT NULL COMMENT '持仓成本',
  `current_price` decimal(10,2) NOT NULL COMMENT '当前价格',
  `market_value` decimal(15,2) NOT NULL COMMENT '持仓市值',
  `unrealized_pnl` decimal(15,2) NOT NULL COMMENT '浮动盈亏',
  `position_ratio` decimal(5,4) NOT NULL COMMENT '持仓比例',
  `direction` int(11) NOT NULL COMMENT '持仓方向（1:多头 -1:空头）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标识（0:未删除 1:已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_strategy_id` (`strategy_id`),
  KEY `idx_stock_code` (`stock_code`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='策略持仓表';

-- 回测结果表
CREATE TABLE IF NOT EXISTS `backtest_result` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `strategy_id` bigint(20) NOT NULL COMMENT '策略ID',
  `backtest_name` varchar(100) NOT NULL COMMENT '回测名称',
  `start_date` date NOT NULL COMMENT '回测开始日期',
  `end_date` date NOT NULL COMMENT '回测结束日期',
  `initial_capital` decimal(15,2) NOT NULL COMMENT '初始资金',
  `final_capital` decimal(15,2) DEFAULT NULL COMMENT '最终资金',
  `total_return` decimal(8,4) DEFAULT NULL COMMENT '总收益率',
  `annual_return` decimal(8,4) DEFAULT NULL COMMENT '年化收益率',
  `max_drawdown` decimal(8,4) DEFAULT NULL COMMENT '最大回撤',
  `sharpe_ratio` decimal(8,4) DEFAULT NULL COMMENT '夏普比率',
  `win_rate` decimal(8,4) DEFAULT NULL COMMENT '胜率',
  `total_trades` int(11) DEFAULT NULL COMMENT '总交易次数',
  `winning_trades` int(11) DEFAULT NULL COMMENT '盈利交易次数',
  `avg_holding_days` decimal(8,2) DEFAULT NULL COMMENT '平均持仓时间（天）',
  `parameters` text COMMENT '回测参数（JSON格式）',
  `result_details` text COMMENT '回测结果详情（JSON格式）',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '回测状态（0:进行中 1:已完成 2:失败）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标识（0:未删除 1:已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_strategy_id` (`strategy_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='回测结果表';

-- 策略预警表
CREATE TABLE IF NOT EXISTS `strategy_alert` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `strategy_id` bigint(20) NOT NULL COMMENT '策略ID',
  `alert_name` varchar(100) NOT NULL COMMENT '预警名称',
  `alert_type` int(11) NOT NULL COMMENT '预警类型（1:收益率预警 2:回撤预警 3:持仓预警 4:资金预警）',
  `alert_condition` text COMMENT '预警条件（JSON格式）',
  `threshold` decimal(10,4) NOT NULL COMMENT '预警阈值',
  `alert_level` int(11) NOT NULL COMMENT '预警级别（1:低 2:中 3:高）',
  `notification_type` int(11) NOT NULL COMMENT '通知方式（1:邮件 2:短信 3:WebSocket 4:全部）',
  `recipients` text COMMENT '通知接收人',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用（0:禁用 1:启用）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标识（0:未删除 1:已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_strategy_id` (`strategy_id`),
  KEY `idx_alert_type` (`alert_type`),
  KEY `idx_enabled` (`enabled`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='策略预警表';

-- 预警历史表
CREATE TABLE IF NOT EXISTS `alert_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `strategy_id` bigint(20) NOT NULL COMMENT '策略ID',
  `alert_id` bigint(20) NOT NULL COMMENT '预警规则ID',
  `alert_name` varchar(100) NOT NULL COMMENT '预警名称',
  `alert_type` int(11) NOT NULL COMMENT '预警类型（1:收益率预警 2:回撤预警 3:持仓预警 4:资金预警）',
  `alert_level` int(11) NOT NULL COMMENT '预警级别（1:低 2:中 3:高）',
  `trigger_value` decimal(10,4) NOT NULL COMMENT '触发值',
  `threshold` decimal(10,4) NOT NULL COMMENT '预警阈值',
  `alert_content` text COMMENT '预警内容',
  `notification_type` int(11) NOT NULL COMMENT '通知方式（1:邮件 2:短信 3:WebSocket 4:全部）',
  `notification_status` int(11) NOT NULL DEFAULT '0' COMMENT '通知状态（0:未发送 1:已发送 2:发送失败）',
  `process_status` int(11) NOT NULL DEFAULT '0' COMMENT '处理状态（0:未处理 1:已处理 2:已忽略）',
  `process_by` varchar(50) DEFAULT NULL COMMENT '处理人',
  `process_time` datetime DEFAULT NULL COMMENT '处理时间',
  `process_remark` text COMMENT '处理备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '触发时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标识（0:未删除 1:已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_strategy_id` (`strategy_id`),
  KEY `idx_alert_id` (`alert_id`),
  KEY `idx_alert_type` (`alert_type`),
  KEY `idx_process_status` (`process_status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警历史表';

-- 插入示例数据
INSERT INTO `strategy` (`strategy_name`, `description`, `strategy_type`, `status`, `initial_capital`, `current_capital`, `parameters`, `risk_control`, `create_by`) VALUES
('双均线策略', '基于短期和长期移动平均线的趋势跟踪策略', 1, 1, 100000.00, 105000.00, '{"short_period": 5, "long_period": 20}', '{"max_position_ratio": 0.8, "stop_loss": 0.1}', 'admin'),
('均值回归策略', '基于价格偏离均值的回归策略', 2, 0, 50000.00, 50000.00, '{"lookback_period": 20, "std_multiplier": 2}', '{"max_position_ratio": 0.6, "stop_loss": 0.05}', 'admin'),
('网格交易策略', '基于价格区间的网格交易策略', 3, 2, 80000.00, 82000.00, '{"grid_levels": 10, "grid_spacing": 0.02}', '{"max_position_ratio": 0.7, "stop_loss": 0.08}', 'admin');

-- 插入示例持仓数据
INSERT INTO `strategy_position` (`strategy_id`, `stock_code`, `stock_name`, `quantity`, `cost_price`, `current_price`, `market_value`, `unrealized_pnl`, `position_ratio`, `direction`) VALUES
(1, '000001', '平安银行', 1000, 12.50, 13.20, 13200.00, 700.00, 0.126, 1),
(1, '000002', '万科A', 800, 18.80, 19.50, 15600.00, 560.00, 0.149, 1),
(1, '600036', '招商银行', 600, 45.20, 46.80, 28080.00, 960.00, 0.267, 1);

-- 插入示例回测结果
INSERT INTO `backtest_result` (`strategy_id`, `backtest_name`, `start_date`, `end_date`, `initial_capital`, `final_capital`, `total_return`, `annual_return`, `max_drawdown`, `sharpe_ratio`, `win_rate`, `total_trades`, `winning_trades`, `avg_holding_days`, `status`, `create_by`) VALUES
(1, '双均线策略回测2023', '2023-01-01', '2023-12-31', 100000.00, 115000.00, 0.1500, 0.1200, 0.0800, 1.2000, 0.6500, 100, 65, 5.50, 1, 'admin'),
(2, '均值回归策略回测2023', '2023-01-01', '2023-12-31', 50000.00, 52000.00, 0.0400, 0.0400, 0.0300, 0.8000, 0.5500, 50, 28, 8.20, 1, 'admin');

-- 插入示例预警规则
INSERT INTO `strategy_alert` (`strategy_id`, `alert_name`, `alert_type`, `alert_condition`, `threshold`, `alert_level`, `notification_type`, `recipients`, `create_by`) VALUES
(1, '收益率预警', 1, '{"condition": "return_rate < threshold"}', -0.0500, 2, 4, 'admin@example.com', 'admin'),
(1, '回撤预警', 2, '{"condition": "drawdown > threshold"}', 0.1000, 3, 4, 'admin@example.com', 'admin'),
(1, '资金预警', 4, '{"condition": "capital < threshold"}', 80000.00, 2, 4, 'admin@example.com', 'admin');

-- 插入示例预警历史
INSERT INTO `alert_history` (`strategy_id`, `alert_id`, `alert_name`, `alert_type`, `alert_level`, `trigger_value`, `threshold`, `alert_content`, `notification_type`, `notification_status`, `process_status`) VALUES
(1, 1, '收益率预警', 1, 2, -0.0600, -0.0500, '策略收益率已降至-6%，低于预警阈值-5%', 4, 1, 0),
(1, 2, '回撤预警', 2, 3, 0.1200, 0.1000, '策略回撤已达到12%，超过预警阈值10%', 4, 1, 1); 