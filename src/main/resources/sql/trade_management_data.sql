-- 交易管理子系统测试数据

-- 插入交易账户测试数据
INSERT INTO `trade_account` (`account_code`, `account_name`, `user_id`, `account_type`, `broker`, `account_status`, `total_assets`, `available_cash`, `frozen_cash`, `market_value`, `unrealized_pnl`, `realized_pnl`, `total_pnl`) VALUES
('ACC001', '个人投资账户A', 1, '个人', '中信证券', 'ACTIVE', 1000000.00, 500000.00, 0.00, 500000.00, 25000.00, 15000.00, 40000.00),
('ACC002', '机构投资账户B', 2, '机构', '华泰证券', 'ACTIVE', 5000000.00, 2000000.00, 0.00, 3000000.00, 150000.00, 80000.00, 230000.00),
('ACC003', '产品投资账户C', 3, '产品', '国泰君安', 'ACTIVE', 2000000.00, 800000.00, 0.00, 1200000.00, 60000.00, 30000.00, 90000.00),
('ACC004', '个人投资账户D', 4, '个人', '招商证券', 'SUSPENDED', 800000.00, 400000.00, 0.00, 400000.00, 20000.00, 10000.00, 30000.00),
('ACC005', '机构投资账户E', 5, '机构', '广发证券', 'CLOSED', 3000000.00, 1500000.00, 0.00, 1500000.00, 75000.00, 45000.00, 120000.00);

-- 插入交易订单测试数据
INSERT INTO `trade_order` (`order_code`, `account_id`, `strategy_id`, `fund_id`, `order_type`, `order_status`, `order_price`, `order_quantity`, `filled_quantity`, `filled_amount`, `avg_fill_price`, `commission`, `order_time`, `remark`) VALUES
('ORD001', 1, 1, 1, 'BUY', 'FILLED', 1.2500, 10000.0000, 10000.0000, 12500.00, 1.2500, 12.50, '2024-01-15 09:30:00', '买入基金A'),
('ORD002', 1, 1, 2, 'SELL', 'PARTIAL', 1.1800, 5000.0000, 3000.0000, 3540.00, 1.1800, 3.54, '2024-01-15 10:15:00', '卖出基金B'),
('ORD003', 2, 2, 3, 'BUY', 'PENDING', 1.3200, 8000.0000, 0.0000, 0.00, NULL, 0.00, '2024-01-15 11:00:00', '买入基金C'),
('ORD004', 2, 2, 4, 'SELL', 'CANCELLED', 1.1500, 6000.0000, 0.0000, 0.00, NULL, 0.00, '2024-01-15 14:30:00', '卖出基金D'),
('ORD005', 3, 3, 5, 'BUY', 'EXECUTING', 1.2800, 12000.0000, 0.0000, 0.00, NULL, 0.00, '2024-01-15 15:45:00', '买入基金E');

-- 插入交易成交测试数据
INSERT INTO `trade_execution` (`execution_code`, `order_id`, `account_id`, `fund_id`, `execution_price`, `execution_quantity`, `execution_amount`, `commission`, `net_amount`, `execution_time`) VALUES
('EXE001', 1, 1, 1, 1.2500, 10000.0000, 12500.00, 12.50, 12487.50, '2024-01-15 09:31:00'),
('EXE002', 2, 1, 2, 1.1800, 3000.0000, 3540.00, 3.54, 3536.46, '2024-01-15 10:16:00'),
('EXE003', 2, 1, 2, 1.1750, 2000.0000, 2350.00, 2.35, 2347.65, '2024-01-15 10:20:00');

-- 插入持仓测试数据
INSERT INTO `position` (`account_id`, `fund_id`, `total_quantity`, `available_quantity`, `frozen_quantity`, `avg_cost`, `market_price`, `market_value`, `unrealized_pnl`, `realized_pnl`, `total_pnl`) VALUES
(1, 1, 10000.0000, 10000.0000, 0.0000, 1.2500, 1.2750, 12750.00, 250.00, 0.00, 250.00),
(1, 2, 2000.0000, 2000.0000, 0.0000, 1.1800, 1.1850, 2370.00, 10.00, 0.00, 10.00),
(2, 3, 8000.0000, 8000.0000, 0.0000, 1.3200, 1.3250, 10600.00, 40.00, 0.00, 40.00),
(3, 5, 12000.0000, 12000.0000, 0.0000, 1.2800, 1.2850, 15420.00, 60.00, 0.00, 60.00);

-- 插入资金流水测试数据
INSERT INTO `cash_flow` (`flow_code`, `account_id`, `flow_type`, `amount`, `balance`, `related_order_id`, `related_execution_id`, `description`, `flow_time`) VALUES
('FLW001', 1, 'DEPOSIT', 1000000.00, 1000000.00, NULL, NULL, '账户初始资金', '2024-01-01 09:00:00'),
('FLW002', 1, 'BUY', -12500.00, 987500.00, 1, 1, '买入基金A', '2024-01-15 09:31:00'),
('FLW003', 1, 'SELL', 3540.00, 991040.00, 2, 2, '卖出基金B', '2024-01-15 10:16:00'),
('FLW004', 2, 'DEPOSIT', 5000000.00, 5000000.00, NULL, NULL, '账户初始资金', '2024-01-01 09:00:00'),
('FLW005', 3, 'DEPOSIT', 2000000.00, 2000000.00, NULL, NULL, '账户初始资金', '2024-01-01 09:00:00');

-- 插入风险控制测试数据
INSERT INTO `risk_control` (`account_id`, `risk_type`, `risk_value`, `current_value`, `is_active`, `alert_threshold`, `stop_threshold`, `description`) VALUES
(1, 'POSITION_LIMIT', 0.3000, 0.2500, 1, 0.2500, 0.3000, '单只基金持仓限制30%'),
(1, 'DAILY_LOSS_LIMIT', 0.0500, 0.0200, 1, 0.0300, 0.0500, '日亏损限制5%'),
(2, 'POSITION_LIMIT', 0.2000, 0.1800, 1, 0.1500, 0.2000, '单只基金持仓限制20%'),
(2, 'DAILY_LOSS_LIMIT', 0.0300, 0.0100, 1, 0.0200, 0.0300, '日亏损限制3%'),
(3, 'POSITION_LIMIT', 0.2500, 0.2200, 1, 0.2000, 0.2500, '单只基金持仓限制25%');

-- 插入交易统计测试数据
INSERT INTO `trade_statistics` (`account_id`, `stat_date`, `total_trades`, `buy_trades`, `sell_trades`, `total_volume`, `total_amount`, `total_commission`, `realized_pnl`, `turnover_rate`, `win_rate`, `avg_hold_days`) VALUES
(1, '2024-01-15', 2, 1, 1, 15000.0000, 16040.00, 15.89, 0.00, 0.0160, 0.5000, 5.00),
(2, '2024-01-15', 1, 0, 1, 0.0000, 0.00, 0.00, 0.00, 0.0000, 0.0000, 0.00),
(3, '2024-01-15', 1, 1, 0, 12000.0000, 15360.00, 15.36, 0.00, 0.0077, 0.0000, 0.00); 