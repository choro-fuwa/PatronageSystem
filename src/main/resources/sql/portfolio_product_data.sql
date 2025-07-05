-- 组合产品管理子系统测试数据

-- 插入组合产品数据
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