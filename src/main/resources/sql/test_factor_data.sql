-- 简单的测试因子数据
-- 确保分类ID=5存在（动量因子分类）

-- 插入测试因子数据
INSERT INTO factor (
    factor_code, factor_name, factor_short_name, category_id, 
    factor_type, description, formula, data_source, 
    update_frequency, risk_level, is_public, creator_id, status
) VALUES 
('F001', '动量因子', 'Momentum', 5, '技术因子', '价格动量指标', 'CLOSE/CLOSE[20]', '市场数据', 'daily', '中等', 1, 1, 1),
('F002', '价值因子', 'Value', 5, '基本面因子', '估值指标', 'P/E', '财务数据', 'daily', '低', 1, 1, 1),
('F003', '质量因子', 'Quality', 5, '基本面因子', '财务质量指标', 'ROE', '财务数据', 'daily', '低', 1, 1, 1)
ON DUPLICATE KEY UPDATE 
    factor_name = VALUES(factor_name),
    factor_short_name = VALUES(factor_short_name),
    category_id = VALUES(category_id),
    factor_type = VALUES(factor_type),
    description = VALUES(description),
    formula = VALUES(formula),
    data_source = VALUES(data_source),
    update_frequency = VALUES(update_frequency),
    risk_level = VALUES(risk_level),
    is_public = VALUES(is_public),
    creator_id = VALUES(creator_id),
    status = VALUES(status);

-- 验证数据
SELECT '测试因子数据' as info, COUNT(*) as count FROM factor WHERE category_id = 5;
SELECT * FROM factor WHERE category_id = 5; 