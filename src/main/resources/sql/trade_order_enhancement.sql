-- 交易单管理增强功能SQL脚本
-- 业务类型枚举：OPEN-建仓交易单，REBALANCE-调仓交易单，ERROR_FIX-差错处理交易单

-- 1. 为trade_order表增加order_biz_type字段（如未加过）
ALTER TABLE `trade_order`
ADD COLUMN `order_biz_type` VARCHAR(20) DEFAULT 'OPEN' COMMENT '业务类型：OPEN-建仓/REBALANCE-调仓/ERROR_FIX-差错处理' AFTER `order_status`;

-- 2. 为新增字段创建索引（可选）
CREATE INDEX `idx_order_biz_type` ON `trade_order` (`order_biz_type`);

-- 3. 更新现有数据的order_biz_type字段
UPDATE `trade_order` SET `order_biz_type` = 'OPEN' WHERE `order_biz_type` = 'NORMAL' OR `order_biz_type` IS NULL;
UPDATE `trade_order` SET `order_biz_type` = 'REBALANCE' WHERE `order_biz_type` = 'REBALANCE';
UPDATE `trade_order` SET `order_biz_type` = 'ERROR_FIX' WHERE `order_biz_type` = 'OTHER';

-- 4. 插入测试数据（包含新的order_biz_type字段）
INSERT INTO `trade_order` (
  `order_code`, `account_id`, `strategy_id`, `fund_id`, `order_type`, `order_status`, `order_biz_type`,
  `order_price`, `order_quantity`, `filled_quantity`, `filled_amount`, `avg_fill_price`, `commission`, `order_time`, `remark`
) VALUES
('ORD011', 1, 1, 1, 'BUY', 'PENDING', 'OPEN', 1.2500, 10000.0000, 0.0000, 0.00, NULL, 0.00, '2024-01-17 09:30:00', '建仓买入订单'),
('ORD012', 2, 2, 2, 'SELL', 'PENDING', 'REBALANCE', 1.1800, 5000.0000, 0.0000, 0.00, NULL, 0.00, '2024-01-17 10:15:00', '调仓卖出订单'),
('ORD013', 3, 3, 3, 'BUY', 'PENDING', 'ERROR_FIX', 1.3200, 8000.0000, 0.0000, 0.00, NULL, 0.00, '2024-01-17 11:00:00', '差错处理订单');

-- 5. 验证数据插入结果
SELECT 
    order_code, 
    order_type, 
    order_status, 
    order_biz_type, 
    order_price, 
    order_quantity, 
    remark 
FROM trade_order 
WHERE order_code IN ('ORD011', 'ORD012', 'ORD013')
ORDER BY order_time DESC; 