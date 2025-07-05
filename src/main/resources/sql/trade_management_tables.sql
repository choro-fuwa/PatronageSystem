-- 交易管理子系统数据库表结构

-- 交易账户表
CREATE TABLE IF NOT EXISTS `trade_account` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_code` varchar(50) NOT NULL COMMENT '账户代码',
  `account_name` varchar(100) NOT NULL COMMENT '账户名称',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `account_type` varchar(50) NOT NULL COMMENT '账户类型：个人/机构/产品',
  `broker` varchar(100) DEFAULT NULL COMMENT '券商',
  `account_status` varchar(20) DEFAULT 'ACTIVE' COMMENT '账户状态：ACTIVE-正常/SUSPENDED-暂停/CLOSED-关闭',
  `total_assets` decimal(15,2) DEFAULT 0 COMMENT '总资产',
  `available_cash` decimal(15,2) DEFAULT 0 COMMENT '可用资金',
  `frozen_cash` decimal(15,2) DEFAULT 0 COMMENT '冻结资金',
  `market_value` decimal(15,2) DEFAULT 0 COMMENT '市值',
  `unrealized_pnl` decimal(15,2) DEFAULT 0 COMMENT '未实现盈亏',
  `realized_pnl` decimal(15,2) DEFAULT 0 COMMENT '已实现盈亏',
  `total_pnl` decimal(15,2) DEFAULT 0 COMMENT '总盈亏',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_account_code` (`account_code`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_account_status` (`account_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易账户表';

-- 交易订单表
CREATE TABLE IF NOT EXISTS `trade_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_code` varchar(50) NOT NULL COMMENT '订单编号',
  `account_id` bigint NOT NULL COMMENT '账户ID',
  `strategy_id` bigint DEFAULT NULL COMMENT '策略ID',
  `fund_id` bigint NOT NULL COMMENT '基金ID',
  `order_type` varchar(20) NOT NULL COMMENT '订单类型：BUY-买入/SELL-卖出',
  `order_status` varchar(20) DEFAULT 'PENDING' COMMENT '订单状态：PENDING-待执行/EXECUTING-执行中/PARTIAL-部分成交/FILLED-全部成交/CANCELLED-已取消/REJECTED-已拒绝',
  `order_price` decimal(10,4) NOT NULL COMMENT '委托价格',
  `order_quantity` decimal(15,4) NOT NULL COMMENT '委托数量',
  `filled_quantity` decimal(15,4) DEFAULT 0 COMMENT '已成交数量',
  `filled_amount` decimal(15,2) DEFAULT 0 COMMENT '已成交金额',
  `avg_fill_price` decimal(10,4) DEFAULT NULL COMMENT '平均成交价格',
  `commission` decimal(15,2) DEFAULT 0 COMMENT '手续费',
  `order_time` datetime NOT NULL COMMENT '委托时间',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `cancel_time` datetime DEFAULT NULL COMMENT '取消时间',
  `cancel_reason` varchar(200) DEFAULT NULL COMMENT '取消原因',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_code` (`order_code`),
  KEY `idx_account_id` (`account_id`),
  KEY `idx_strategy_id` (`strategy_id`),
  KEY `idx_fund_id` (`fund_id`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_order_time` (`order_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易订单表';

-- 交易成交表
CREATE TABLE IF NOT EXISTS `trade_execution` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `execution_code` varchar(50) NOT NULL COMMENT '成交编号',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `account_id` bigint NOT NULL COMMENT '账户ID',
  `fund_id` bigint NOT NULL COMMENT '基金ID',
  `execution_price` decimal(10,4) NOT NULL COMMENT '成交价格',
  `execution_quantity` decimal(15,4) NOT NULL COMMENT '成交数量',
  `execution_amount` decimal(15,2) NOT NULL COMMENT '成交金额',
  `commission` decimal(15,2) DEFAULT 0 COMMENT '手续费',
  `net_amount` decimal(15,2) NOT NULL COMMENT '净金额',
  `execution_time` datetime NOT NULL COMMENT '成交时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_execution_code` (`execution_code`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_account_id` (`account_id`),
  KEY `idx_fund_id` (`fund_id`),
  KEY `idx_execution_time` (`execution_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易成交表';

-- 持仓表
CREATE TABLE IF NOT EXISTS `position` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_id` bigint NOT NULL COMMENT '账户ID',
  `fund_id` bigint NOT NULL COMMENT '基金ID',
  `total_quantity` decimal(15,4) NOT NULL COMMENT '总数量',
  `available_quantity` decimal(15,4) NOT NULL COMMENT '可用数量',
  `frozen_quantity` decimal(15,4) DEFAULT 0 COMMENT '冻结数量',
  `avg_cost` decimal(10,4) NOT NULL COMMENT '平均成本',
  `market_price` decimal(10,4) DEFAULT NULL COMMENT '市价',
  `market_value` decimal(15,2) DEFAULT NULL COMMENT '市值',
  `unrealized_pnl` decimal(15,2) DEFAULT NULL COMMENT '未实现盈亏',
  `realized_pnl` decimal(15,2) DEFAULT 0 COMMENT '已实现盈亏',
  `total_pnl` decimal(15,2) DEFAULT NULL COMMENT '总盈亏',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_account_fund` (`account_id`, `fund_id`),
  KEY `idx_account_id` (`account_id`),
  KEY `idx_fund_id` (`fund_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='持仓表';

-- 资金流水表
CREATE TABLE IF NOT EXISTS `cash_flow` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `flow_code` varchar(50) NOT NULL COMMENT '流水编号',
  `account_id` bigint NOT NULL COMMENT '账户ID',
  `flow_type` varchar(20) NOT NULL COMMENT '流水类型：DEPOSIT-入金/WITHDRAW-出金/BUY-买入/SELL-卖出/DIVIDEND-分红/INTEREST-利息/FEE-费用',
  `amount` decimal(15,2) NOT NULL COMMENT '金额',
  `balance` decimal(15,2) NOT NULL COMMENT '余额',
  `related_order_id` bigint DEFAULT NULL COMMENT '关联订单ID',
  `related_execution_id` bigint DEFAULT NULL COMMENT '关联成交ID',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `flow_time` datetime NOT NULL COMMENT '流水时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_flow_code` (`flow_code`),
  KEY `idx_account_id` (`account_id`),
  KEY `idx_flow_type` (`flow_type`),
  KEY `idx_flow_time` (`flow_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资金流水表';

-- 风险控制表
CREATE TABLE IF NOT EXISTS `risk_control` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_id` bigint NOT NULL COMMENT '账户ID',
  `risk_type` varchar(50) NOT NULL COMMENT '风险类型：POSITION_LIMIT-持仓限制/SINGLE_STOCK_LIMIT-单股限制/DAILY_LOSS_LIMIT-日亏损限制',
  `risk_value` decimal(15,4) NOT NULL COMMENT '风险值',
  `current_value` decimal(15,4) DEFAULT 0 COMMENT '当前值',
  `is_active` tinyint DEFAULT 1 COMMENT '是否启用：1-启用，0-禁用',
  `alert_threshold` decimal(15,4) DEFAULT NULL COMMENT '预警阈值',
  `stop_threshold` decimal(15,4) DEFAULT NULL COMMENT '止损阈值',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_account_risk_type` (`account_id`, `risk_type`),
  KEY `idx_account_id` (`account_id`),
  KEY `idx_risk_type` (`risk_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险控制表';

-- 交易统计表
CREATE TABLE IF NOT EXISTS `trade_statistics` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_id` bigint NOT NULL COMMENT '账户ID',
  `stat_date` date NOT NULL COMMENT '统计日期',
  `total_trades` int DEFAULT 0 COMMENT '总交易次数',
  `buy_trades` int DEFAULT 0 COMMENT '买入次数',
  `sell_trades` int DEFAULT 0 COMMENT '卖出次数',
  `total_volume` decimal(15,4) DEFAULT 0 COMMENT '总成交量',
  `total_amount` decimal(15,2) DEFAULT 0 COMMENT '总成交金额',
  `total_commission` decimal(15,2) DEFAULT 0 COMMENT '总手续费',
  `realized_pnl` decimal(15,2) DEFAULT 0 COMMENT '已实现盈亏',
  `turnover_rate` decimal(10,4) DEFAULT 0 COMMENT '换手率',
  `win_rate` decimal(10,4) DEFAULT 0 COMMENT '胜率',
  `avg_hold_days` decimal(10,2) DEFAULT 0 COMMENT '平均持仓天数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_account_stat_date` (`account_id`, `stat_date`),
  KEY `idx_account_id` (`account_id`),
  KEY `idx_stat_date` (`stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易统计表'; 