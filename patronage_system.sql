/*
 Navicat Premium Data Transfer

 Source Server         : root
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : patronage_system

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 05/07/2025 14:54:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cash_flow
-- ----------------------------
DROP TABLE IF EXISTS `cash_flow`;
CREATE TABLE `cash_flow`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `flow_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流水编号',
  `account_id` bigint NOT NULL COMMENT '账户ID',
  `flow_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流水类型：DEPOSIT-入金/WITHDRAW-出金/BUY-买入/SELL-卖出/DIVIDEND-分红/INTEREST-利息/FEE-费用',
  `amount` decimal(15, 2) NOT NULL COMMENT '金额',
  `balance` decimal(15, 2) NOT NULL COMMENT '余额',
  `related_order_id` bigint NULL DEFAULT NULL COMMENT '关联订单ID',
  `related_execution_id` bigint NULL DEFAULT NULL COMMENT '关联成交ID',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `flow_time` datetime NOT NULL COMMENT '流水时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_flow_code`(`flow_code` ASC) USING BTREE,
  INDEX `idx_account_id`(`account_id` ASC) USING BTREE,
  INDEX `idx_flow_type`(`flow_type` ASC) USING BTREE,
  INDEX `idx_flow_time`(`flow_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '资金流水表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cash_flow
-- ----------------------------
INSERT INTO `cash_flow` VALUES (1, 'FLW001', 1, 'DEPOSIT', 1000000.00, 1000000.00, NULL, NULL, '账户初始资金', '2024-01-01 09:00:00', '2025-07-02 14:11:30');
INSERT INTO `cash_flow` VALUES (2, 'FLW002', 1, 'BUY', -12500.00, 987500.00, 1, 1, '买入基金A', '2024-01-15 09:31:00', '2025-07-02 14:11:30');
INSERT INTO `cash_flow` VALUES (3, 'FLW003', 1, 'SELL', 3540.00, 991040.00, 2, 2, '卖出基金B', '2024-01-15 10:16:00', '2025-07-02 14:11:30');
INSERT INTO `cash_flow` VALUES (4, 'FLW004', 2, 'DEPOSIT', 5000000.00, 5000000.00, NULL, NULL, '账户初始资金', '2024-01-01 09:00:00', '2025-07-02 14:11:30');
INSERT INTO `cash_flow` VALUES (5, 'FLW005', 3, 'DEPOSIT', 2000000.00, 2000000.00, NULL, NULL, '账户初始资金', '2024-01-01 09:00:00', '2025-07-02 14:11:30');

-- ----------------------------
-- Table structure for factor
-- ----------------------------
DROP TABLE IF EXISTS `factor`;
CREATE TABLE `factor`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `factor_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '因子代码',
  `factor_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '因子名称',
  `factor_short_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '因子简称',
  `category_id` bigint NULL DEFAULT NULL COMMENT '分类ID',
  `factor_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '因子类型：技术因子、基本面因子、宏观因子等',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '因子描述',
  `formula` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '因子计算公式',
  `data_source` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据来源',
  `update_frequency` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'daily' COMMENT '更新频率：daily-日频，weekly-周频，monthly-月频',
  `risk_level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '风险等级',
  `is_public` tinyint NULL DEFAULT 1 COMMENT '是否公开：1-公开，0-私有',
  `creator_id` bigint NULL DEFAULT NULL COMMENT '创建者ID',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_factor_code`(`factor_code` ASC) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE,
  INDEX `idx_creator_id`(`creator_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '因子表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of factor
-- ----------------------------
INSERT INTO `factor` VALUES (1, 'MOM_20D', '20日动量因子', '20日动量', 4, '动量因子', '20日价格动量', 'close_price / close_price_20d_ago - 1', '价格数据', 'daily', '中风险', 1, NULL, 1, '2025-07-02 11:17:51', '2025-07-02 11:17:51');
INSERT INTO `factor` VALUES (2, 'VOL_60D', '60日波动率因子', '60日波动率', 5, '波动率因子', '60日收益率标准差', 'std(returns_60d)', '价格数据', 'daily', '中风险', 1, NULL, 1, '2025-07-02 11:17:51', '2025-07-02 11:17:51');
INSERT INTO `factor` VALUES (3, 'PE_RATIO', '市盈率因子', 'PE', 6, '价值因子', '市盈率倒数', '1 / pe_ratio', '财务数据', 'monthly', '低风险', 1, NULL, 1, '2025-07-02 11:17:51', '2025-07-02 11:17:51');
INSERT INTO `factor` VALUES (4, 'ROE', '净资产收益率因子', 'ROE', 7, '质量因子', '净资产收益率', 'net_profit / equity', '财务数据', 'quarterly', '低风险', 1, NULL, 1, '2025-07-02 11:17:51', '2025-07-02 11:17:51');
INSERT INTO `factor` VALUES (5, 'BETA', '贝塔因子', '贝塔', 5, '波动率因子', '相对市场指数的贝塔系数', 'cov(returns, market_returns) / var(market_returns)', '价格数据', 'daily', '中风险', 1, NULL, 1, '2025-07-02 11:17:51', '2025-07-02 11:17:51');
INSERT INTO `factor` VALUES (6, '996', '996', '996', 1, '技术因子', '996', '996', '996', 'daily', '低风险', 1, NULL, 1, '2025-07-02 12:20:51', '2025-07-02 12:20:51');

-- ----------------------------
-- Table structure for factor_analysis
-- ----------------------------
DROP TABLE IF EXISTS `factor_analysis`;
CREATE TABLE `factor_analysis`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `factor_id` bigint NOT NULL COMMENT '因子ID',
  `analysis_date` date NOT NULL COMMENT '分析日期',
  `ic_value` decimal(10, 6) NULL DEFAULT NULL COMMENT 'IC值',
  `rank_ic` decimal(10, 6) NULL DEFAULT NULL COMMENT 'RankIC值',
  `turnover_rate` decimal(10, 4) NULL DEFAULT NULL COMMENT '换手率',
  `win_rate` decimal(5, 2) NULL DEFAULT NULL COMMENT '胜率',
  `sharpe_ratio` decimal(10, 4) NULL DEFAULT NULL COMMENT '夏普比率',
  `max_drawdown` decimal(10, 4) NULL DEFAULT NULL COMMENT '最大回撤',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_factor_date`(`factor_id` ASC, `analysis_date` ASC) USING BTREE,
  INDEX `idx_factor_id`(`factor_id` ASC) USING BTREE,
  INDEX `idx_analysis_date`(`analysis_date` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '因子有效性分析表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of factor_analysis
-- ----------------------------

-- ----------------------------
-- Table structure for factor_category
-- ----------------------------
DROP TABLE IF EXISTS `factor_category`;
CREATE TABLE `factor_category`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `category_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类代码',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类描述',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父分类ID，0表示顶级分类',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_category_code`(`category_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '因子分类表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of factor_category
-- ----------------------------
INSERT INTO `factor_category` VALUES (1, '技术因子', 'TECH', '基于价格和交易量的技术指标', 0, 1, 1, '2025-07-02 11:17:51', '2025-07-02 11:17:51');
INSERT INTO `factor_category` VALUES (2, '基本面因子', 'FUNDAMENTAL', '基于公司基本面的指标', 0, 2, 1, '2025-07-02 11:17:51', '2025-07-02 11:17:51');
INSERT INTO `factor_category` VALUES (3, '宏观因子', 'MACRO', '基于宏观经济环境的指标', 0, 3, 1, '2025-07-02 11:17:51', '2025-07-02 11:17:51');
INSERT INTO `factor_category` VALUES (4, '动量因子', 'MOMENTUM', '价格动量相关指标', 1, 1, 1, '2025-07-02 11:17:51', '2025-07-02 11:17:51');
INSERT INTO `factor_category` VALUES (5, '波动率因子', 'VOLATILITY', '波动率相关指标', 1, 2, 1, '2025-07-02 11:17:51', '2025-07-02 11:17:51');
INSERT INTO `factor_category` VALUES (6, '价值因子', 'VALUE', '估值相关指标', 2, 1, 1, '2025-07-02 11:17:51', '2025-07-02 11:17:51');
INSERT INTO `factor_category` VALUES (7, '质量因子', 'QUALITY', '公司质量相关指标', 2, 2, 1, '2025-07-02 11:17:51', '2025-07-02 11:17:51');
INSERT INTO `factor_category` VALUES (21, '996', '996', '996', 0, 0, 1, '2025-07-02 12:21:05', '2025-07-02 12:21:05');

-- ----------------------------
-- Table structure for factor_data
-- ----------------------------
DROP TABLE IF EXISTS `factor_data`;
CREATE TABLE `factor_data`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `factor_id` bigint NOT NULL COMMENT '因子ID',
  `fund_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '基金代码',
  `trade_date` date NOT NULL COMMENT '交易日期',
  `factor_value` decimal(20, 6) NULL DEFAULT NULL COMMENT '因子值',
  `rank_value` decimal(10, 4) NULL DEFAULT NULL COMMENT '排名值',
  `percentile` decimal(5, 2) NULL DEFAULT NULL COMMENT '百分位数',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_factor_fund_date`(`factor_id` ASC, `fund_code` ASC, `trade_date` ASC) USING BTREE,
  INDEX `idx_factor_id`(`factor_id` ASC) USING BTREE,
  INDEX `idx_fund_code`(`fund_code` ASC) USING BTREE,
  INDEX `idx_trade_date`(`trade_date` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '因子数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of factor_data
-- ----------------------------

-- ----------------------------
-- Table structure for factor_portfolio
-- ----------------------------
DROP TABLE IF EXISTS `factor_portfolio`;
CREATE TABLE `factor_portfolio`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `portfolio_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '组合名称',
  `portfolio_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '组合代码',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '组合描述',
  `strategy_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '策略类型',
  `creator_id` bigint NULL DEFAULT NULL COMMENT '创建者ID',
  `is_public` tinyint NULL DEFAULT 1 COMMENT '是否公开：1-公开，0-私有',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_portfolio_code`(`portfolio_code` ASC) USING BTREE,
  INDEX `idx_creator_id`(`creator_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '因子组合表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of factor_portfolio
-- ----------------------------
INSERT INTO `factor_portfolio` VALUES (1, '经典多因子模型', 'CLASSIC_MULTI', '基于价值、质量、动量的经典多因子模型', '多因子', 1, 1, 1, '2025-07-02 11:17:51', '2025-07-02 11:17:51');
INSERT INTO `factor_portfolio` VALUES (2, '动量增强模型', 'MOMENTUM_ENHANCED', '以动量因子为主的增强模型', '动量策略', 1, 1, 1, '2025-07-02 11:17:51', '2025-07-02 11:17:51');

-- ----------------------------
-- Table structure for factor_portfolio_detail
-- ----------------------------
DROP TABLE IF EXISTS `factor_portfolio_detail`;
CREATE TABLE `factor_portfolio_detail`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `portfolio_id` bigint NOT NULL COMMENT '组合ID',
  `factor_id` bigint NOT NULL COMMENT '因子ID',
  `weight` decimal(5, 4) NULL DEFAULT NULL COMMENT '权重',
  `direction` tinyint NULL DEFAULT 1 COMMENT '方向：1-正向，-1-反向',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_portfolio_factor`(`portfolio_id` ASC, `factor_id` ASC) USING BTREE,
  INDEX `idx_portfolio_id`(`portfolio_id` ASC) USING BTREE,
  INDEX `idx_factor_id`(`factor_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '因子组合明细表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of factor_portfolio_detail
-- ----------------------------

-- ----------------------------
-- Table structure for fund
-- ----------------------------
DROP TABLE IF EXISTS `fund`;
CREATE TABLE `fund`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fund_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '基金代码',
  `fund_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '基金名称',
  `fund_short_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '基金简称',
  `company_id` bigint NULL DEFAULT NULL COMMENT '基金公司ID',
  `manager_id` bigint NULL DEFAULT NULL COMMENT '基金经理ID',
  `fund_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '基金类型',
  `risk_level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '风险等级',
  `establish_date` date NULL DEFAULT NULL COMMENT '成立日期',
  `fund_size` decimal(20, 2) NULL DEFAULT NULL COMMENT '基金规模',
  `nav` decimal(10, 4) NULL DEFAULT NULL COMMENT '最新净值',
  `nav_date` date NULL DEFAULT NULL COMMENT '净值日期',
  `total_return` decimal(10, 4) NULL DEFAULT NULL COMMENT '累计收益率',
  `annual_return` decimal(10, 4) NULL DEFAULT NULL COMMENT '年化收益率',
  `max_drawdown` decimal(10, 4) NULL DEFAULT NULL COMMENT '最大回撤',
  `sharpe_ratio` decimal(10, 4) NULL DEFAULT NULL COMMENT '夏普比率',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：1-正常，0-停用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_fund_code`(`fund_code` ASC) USING BTREE,
  INDEX `idx_company_id`(`company_id` ASC) USING BTREE,
  INDEX `idx_manager_id`(`manager_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '基金表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fund
-- ----------------------------
INSERT INTO `fund` VALUES (1, '110022', '易方达消费行业股票', '易方达消费', 2, 1, '股票型', '中高风险', '2010-08-20', 500000.00, 3.2456, '2024-01-15', 224.5600, 15.6700, -25.3400, 1.2300, 1, '2025-07-02 10:42:27', '2025-07-02 10:42:27');
INSERT INTO `fund` VALUES (2, '000001', '华夏成长混合', '华夏成长', 1, 3, '混合型', '中风险', '2001-12-18', 300000.00, 2.1234, '2024-01-15', 112.3400, 8.4500, -18.6700, 0.8900, 1, '2025-07-02 10:42:27', '2025-07-02 10:42:27');
INSERT INTO `fund` VALUES (3, '000002', '华夏大盘精选混合', '华夏大盘', 1, 3, '混合型', '中高风险', '2004-08-11', 450000.00, 4.5678, '2024-01-15', 356.7800, 12.3400, -22.4500, 1.4500, 1, '2025-07-02 10:42:27', '2025-07-02 10:42:27');

-- ----------------------------
-- Table structure for fund_company
-- ----------------------------
DROP TABLE IF EXISTS `fund_company`;
CREATE TABLE `fund_company`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `company_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司代码',
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司名称',
  `company_short_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司简称',
  `establish_date` date NULL DEFAULT NULL COMMENT '成立日期',
  `registered_capital` decimal(20, 2) NULL DEFAULT NULL COMMENT '注册资本',
  `legal_representative` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '法定代表人',
  `company_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司类型',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：1-正常，0-停用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_company_code`(`company_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '基金公司表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fund_company
-- ----------------------------
INSERT INTO `fund_company` VALUES (1, '000001', '华夏基金管理有限公司', '华夏基金', '1998-04-09', 23800.00, '张弘弢', '公募基金', 1, '2025-07-02 10:42:27', '2025-07-02 10:42:27');
INSERT INTO `fund_company` VALUES (2, '000002', '易方达基金管理有限公司', '易方达基金', '2001-04-17', 12000.00, '刘晓艳', '公募基金', 1, '2025-07-02 10:42:27', '2025-07-02 10:42:27');
INSERT INTO `fund_company` VALUES (3, '000003', '嘉实基金管理有限公司', '嘉实基金', '1999-03-25', 15000.00, '经雷', '公募基金', 1, '2025-07-02 10:42:27', '2025-07-02 10:42:27');

-- ----------------------------
-- Table structure for fund_manager
-- ----------------------------
DROP TABLE IF EXISTS `fund_manager`;
CREATE TABLE `fund_manager`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `manager_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '经理代码',
  `manager_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '经理姓名',
  `company_id` bigint NULL DEFAULT NULL COMMENT '所属公司ID',
  `education` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学历',
  `experience_years` int NULL DEFAULT NULL COMMENT '从业年限',
  `introduction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '个人简介',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：1-正常，0-离职',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_manager_code`(`manager_code` ASC) USING BTREE,
  INDEX `idx_company_id`(`company_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '基金经理表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fund_manager
-- ----------------------------
INSERT INTO `fund_manager` VALUES (1, 'M001', '张坤', 2, '清华大学', 10, '易方达基金明星基金经理', 1, '2025-07-02 10:42:27', '2025-07-02 10:42:27');
INSERT INTO `fund_manager` VALUES (2, 'M002', '刘彦春', 2, '北京大学', 15, '易方达基金资深基金经理', 1, '2025-07-02 10:42:27', '2025-07-02 10:42:27');
INSERT INTO `fund_manager` VALUES (3, 'M003', '王宗合', 1, '复旦大学', 12, '华夏基金优秀基金经理', 1, '2025-07-02 10:42:27', '2025-07-02 10:42:27');

-- ----------------------------
-- Table structure for fund_portfolio
-- ----------------------------
DROP TABLE IF EXISTS `fund_portfolio`;
CREATE TABLE `fund_portfolio`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `portfolio_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '组合名称',
  `user_id` bigint NOT NULL COMMENT '创建用户ID',
  `portfolio_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组合类型',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '组合描述',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：1-正常，0-删除',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '基金组合表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fund_portfolio
-- ----------------------------

-- ----------------------------
-- Table structure for fund_portfolio_detail
-- ----------------------------
DROP TABLE IF EXISTS `fund_portfolio_detail`;
CREATE TABLE `fund_portfolio_detail`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `portfolio_id` bigint NOT NULL COMMENT '组合ID',
  `fund_id` bigint NOT NULL COMMENT '基金ID',
  `weight` decimal(5, 4) NULL DEFAULT NULL COMMENT '权重',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_portfolio_id`(`portfolio_id` ASC) USING BTREE,
  INDEX `idx_fund_id`(`fund_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '基金组合明细表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fund_portfolio_detail
-- ----------------------------

-- ----------------------------
-- Table structure for fund_tag
-- ----------------------------
DROP TABLE IF EXISTS `fund_tag`;
CREATE TABLE `fund_tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名称',
  `tag_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签分类',
  `tag_description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签描述',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tag_name`(`tag_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '基金标签表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fund_tag
-- ----------------------------
INSERT INTO `fund_tag` VALUES (1, '消费', '行业', '消费行业相关基金', 1, '2025-07-02 10:42:27');
INSERT INTO `fund_tag` VALUES (2, '科技', '行业', '科技行业相关基金', 1, '2025-07-02 10:42:27');
INSERT INTO `fund_tag` VALUES (3, '医药', '行业', '医药行业相关基金', 1, '2025-07-02 10:42:27');
INSERT INTO `fund_tag` VALUES (4, '蓝筹', '风格', '蓝筹股风格基金', 1, '2025-07-02 10:42:27');
INSERT INTO `fund_tag` VALUES (5, '成长', '风格', '成长股风格基金', 1, '2025-07-02 10:42:27');
INSERT INTO `fund_tag` VALUES (6, '价值', '风格', '价值股风格基金', 1, '2025-07-02 10:42:27');
INSERT INTO `fund_tag` VALUES (7, '大盘', '规模', '大盘股基金', 1, '2025-07-02 10:42:27');
INSERT INTO `fund_tag` VALUES (8, '中小盘', '规模', '中小盘股基金', 1, '2025-07-02 10:42:27');

-- ----------------------------
-- Table structure for fund_tag_relation
-- ----------------------------
DROP TABLE IF EXISTS `fund_tag_relation`;
CREATE TABLE `fund_tag_relation`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fund_id` bigint NOT NULL COMMENT '基金ID',
  `tag_id` bigint NOT NULL COMMENT '标签ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_fund_tag`(`fund_id` ASC, `tag_id` ASC) USING BTREE,
  INDEX `idx_fund_id`(`fund_id` ASC) USING BTREE,
  INDEX `idx_tag_id`(`tag_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '基金标签关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fund_tag_relation
-- ----------------------------
INSERT INTO `fund_tag_relation` VALUES (1, 1, 1, '2025-07-02 10:42:27');
INSERT INTO `fund_tag_relation` VALUES (2, 1, 4, '2025-07-02 10:42:27');
INSERT INTO `fund_tag_relation` VALUES (3, 1, 7, '2025-07-02 10:42:27');
INSERT INTO `fund_tag_relation` VALUES (4, 2, 4, '2025-07-02 10:42:27');
INSERT INTO `fund_tag_relation` VALUES (5, 2, 5, '2025-07-02 10:42:27');
INSERT INTO `fund_tag_relation` VALUES (6, 2, 7, '2025-07-02 10:42:27');
INSERT INTO `fund_tag_relation` VALUES (7, 3, 4, '2025-07-02 10:42:27');
INSERT INTO `fund_tag_relation` VALUES (8, 3, 7, '2025-07-02 10:42:27');

-- ----------------------------
-- Table structure for ip_info
-- ----------------------------
DROP TABLE IF EXISTS `ip_info`;
CREATE TABLE `ip_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ua` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `axis` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `count` bigint NULL DEFAULT NULL,
  `status` tinyint NULL DEFAULT NULL COMMENT '0为下线；1为上线',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ip_info
-- ----------------------------
INSERT INTO `ip_info` VALUES (17, '192.168.3.181', '深圳', 'chome', '2025-07-02 17:08:30', '114.0579,22.5431', 10, 1);

-- ----------------------------
-- Table structure for portfolio_product
-- ----------------------------
DROP TABLE IF EXISTS `portfolio_product`;
CREATE TABLE `portfolio_product`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '产品代码',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '产品名称',
  `product_short_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '产品简称',
  `product_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '产品类型：FOF/组合/定制',
  `risk_level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '风险等级：低/中/高',
  `target_return` decimal(10, 4) NULL DEFAULT NULL COMMENT '目标收益率',
  `min_investment` decimal(15, 2) NULL DEFAULT NULL COMMENT '最小投资金额',
  `max_investment` decimal(15, 2) NULL DEFAULT NULL COMMENT '最大投资金额',
  `management_fee` decimal(5, 4) NULL DEFAULT NULL COMMENT '管理费率',
  `custodian_fee` decimal(5, 4) NULL DEFAULT NULL COMMENT '托管费率',
  `subscription_fee` decimal(5, 4) NULL DEFAULT NULL COMMENT '认购费率',
  `redemption_fee` decimal(5, 4) NULL DEFAULT NULL COMMENT '赎回费率',
  `benchmark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业绩比较基准',
  `investment_strategy` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '投资策略',
  `investment_scope` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '投资范围',
  `product_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'DRAFT' COMMENT '产品状态：DRAFT-草稿/PENDING-待审核/APPROVED-已审核/ACTIVE-已上线/SUSPENDED-暂停/CLOSED-已关闭',
  `launch_date` date NULL DEFAULT NULL COMMENT '成立日期',
  `maturity_date` date NULL DEFAULT NULL COMMENT '到期日期',
  `creator_id` bigint NOT NULL COMMENT '创建人ID',
  `reviewer_id` bigint NULL DEFAULT NULL COMMENT '审核人ID',
  `review_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `review_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'PENDING' COMMENT '审核状态：PENDING-待审核/APPROVED-通过/REJECTED-拒绝',
  `review_comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '审核意见',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_product_code`(`product_code` ASC) USING BTREE,
  INDEX `idx_creator_id`(`creator_id` ASC) USING BTREE,
  INDEX `idx_reviewer_id`(`reviewer_id` ASC) USING BTREE,
  INDEX `idx_product_status`(`product_status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '组合产品表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of portfolio_product
-- ----------------------------
INSERT INTO `portfolio_product` VALUES (1, '996', '稳健配置组合产品', '稳健配置', 'FOF', '中', 0.0800, 10000.00, 1000000.00, 0.0100, 0.0020, 0.0000, 0.0050, '沪深300指数*50%+中债总财富指数*50%', '采用资产配置策略，通过动态调整股票和债券的配置比例，在控制风险的前提下追求稳定收益。', '主要投资于股票型基金、债券型基金、货币市场基金等公募基金产品。', 'ACTIVE', '2024-01-01', NULL, 1, NULL, NULL, 'PENDING', NULL, '2025-07-02 12:54:33', '2025-07-02 13:42:53');
INSERT INTO `portfolio_product` VALUES (2, 'PP002', '成长进取组合产品', '成长进取', '组合', '高', 0.1500, 50000.00, 2000000.00, 0.0150, 0.0020, 0.0000, 0.0100, '沪深300指数*80%+中债总财富指数*20%', '采用成长投资策略，重点配置具有高成长性的股票型基金，追求较高的投资收益。', '主要投资于股票型基金、混合型基金，少量配置债券型基金。', 'ACTIVE', '2024-01-01', NULL, 1, NULL, NULL, 'PENDING', NULL, '2025-07-02 12:54:33', '2025-07-02 12:54:33');
INSERT INTO `portfolio_product` VALUES (3, 'PP003', '价值投资组合产品', '价值投资', '定制', '中', 0.1200, 20000.00, 1500000.00, 0.0120, 0.0020, 0.0000, 0.0080, '中证500指数*60%+中债总财富指数*40%', '采用价值投资策略，精选具有投资价值的基金产品，注重风险控制。', '主要投资于价值型股票基金、债券型基金，适当配置货币市场基金。', 'PENDING', '2024-02-01', NULL, 1, NULL, NULL, 'PENDING', NULL, '2025-07-02 12:54:33', '2025-07-02 12:54:33');
INSERT INTO `portfolio_product` VALUES (10, '998', '998', '998', '组合', '中', 0.0300, 2000.00, 20000.00, 0.0020, 0.0030, 0.0000, 0.0000, '998', '998', '998', 'DRAFT', NULL, NULL, 1, NULL, NULL, 'PENDING', NULL, '2025-07-02 13:43:17', '2025-07-02 14:19:19');

-- ----------------------------
-- Table structure for position
-- ----------------------------
DROP TABLE IF EXISTS `position`;
CREATE TABLE `position`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_id` bigint NOT NULL COMMENT '账户ID',
  `fund_id` bigint NOT NULL COMMENT '基金ID',
  `total_quantity` decimal(15, 4) NOT NULL COMMENT '总数量',
  `available_quantity` decimal(15, 4) NOT NULL COMMENT '可用数量',
  `frozen_quantity` decimal(15, 4) NULL DEFAULT 0.0000 COMMENT '冻结数量',
  `avg_cost` decimal(10, 4) NOT NULL COMMENT '平均成本',
  `market_price` decimal(10, 4) NULL DEFAULT NULL COMMENT '市价',
  `market_value` decimal(15, 2) NULL DEFAULT NULL COMMENT '市值',
  `unrealized_pnl` decimal(15, 2) NULL DEFAULT NULL COMMENT '未实现盈亏',
  `realized_pnl` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '已实现盈亏',
  `total_pnl` decimal(15, 2) NULL DEFAULT NULL COMMENT '总盈亏',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_account_fund`(`account_id` ASC, `fund_id` ASC) USING BTREE,
  INDEX `idx_account_id`(`account_id` ASC) USING BTREE,
  INDEX `idx_fund_id`(`fund_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '持仓表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of position
-- ----------------------------
INSERT INTO `position` VALUES (1, 1, 1, 10000.0000, 10000.0000, 0.0000, 1.2500, 1.2750, 12750.00, 250.00, 0.00, 250.00, '2025-07-02 14:11:30');
INSERT INTO `position` VALUES (2, 1, 2, 2000.0000, 2000.0000, 0.0000, 1.1800, 1.1850, 2370.00, 10.00, 0.00, 10.00, '2025-07-02 14:11:30');
INSERT INTO `position` VALUES (3, 2, 3, 8000.0000, 8000.0000, 0.0000, 1.3200, 1.3250, 10600.00, 40.00, 0.00, 40.00, '2025-07-02 14:11:30');
INSERT INTO `position` VALUES (4, 3, 5, 12000.0000, 12000.0000, 0.0000, 1.2800, 1.2850, 15420.00, 60.00, 0.00, 60.00, '2025-07-02 14:11:30');

-- ----------------------------
-- Table structure for product_document
-- ----------------------------
DROP TABLE IF EXISTS `product_document`;
CREATE TABLE `product_document`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `document_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文档类型：PROSPECTUS-招募说明书/INVESTMENT_GUIDE-投资指南/RISK_DISCLOSURE-风险揭示书/PERFORMANCE_REPORT-业绩报告',
  `document_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文档名称',
  `document_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文档URL',
  `document_size` bigint NULL DEFAULT NULL COMMENT '文档大小(字节)',
  `version` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1.0' COMMENT '版本号',
  `is_active` tinyint NULL DEFAULT 1 COMMENT '是否有效：1-有效，0-无效',
  `upload_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `uploader_id` bigint NOT NULL COMMENT '上传人ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE,
  INDEX `idx_document_type`(`document_type` ASC) USING BTREE,
  INDEX `idx_uploader_id`(`uploader_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '产品文档表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product_document
-- ----------------------------
INSERT INTO `product_document` VALUES (1, 1, 'PROSPECTUS', '稳健配置组合产品招募说明书.pdf', '/documents/PP001_prospectus.pdf', 1024000, '1.0', 1, '2025-07-02 12:54:33', 1);
INSERT INTO `product_document` VALUES (2, 1, 'INVESTMENT_GUIDE', '稳健配置组合产品投资指南.pdf', '/documents/PP001_guide.pdf', 512000, '1.0', 1, '2025-07-02 12:54:33', 1);
INSERT INTO `product_document` VALUES (3, 1, 'RISK_DISCLOSURE', '稳健配置组合产品风险揭示书.pdf', '/documents/PP001_risk.pdf', 256000, '1.0', 1, '2025-07-02 12:54:33', 1);
INSERT INTO `product_document` VALUES (4, 2, 'PROSPECTUS', '成长进取组合产品招募说明书.pdf', '/documents/PP002_prospectus.pdf', 1024000, '1.0', 1, '2025-07-02 12:54:33', 1);
INSERT INTO `product_document` VALUES (5, 2, 'INVESTMENT_GUIDE', '成长进取组合产品投资指南.pdf', '/documents/PP002_guide.pdf', 512000, '1.0', 1, '2025-07-02 12:54:33', 1);
INSERT INTO `product_document` VALUES (6, 1, 'PROSPECTUS', '稳健配置组合产品招募说明书.pdf', '/documents/PP001_prospectus.pdf', 1024000, '1.0', 1, '2025-07-02 12:55:27', 1);
INSERT INTO `product_document` VALUES (7, 1, 'INVESTMENT_GUIDE', '稳健配置组合产品投资指南.pdf', '/documents/PP001_guide.pdf', 512000, '1.0', 1, '2025-07-02 12:55:27', 1);
INSERT INTO `product_document` VALUES (8, 1, 'RISK_DISCLOSURE', '稳健配置组合产品风险揭示书.pdf', '/documents/PP001_risk.pdf', 256000, '1.0', 1, '2025-07-02 12:55:27', 1);
INSERT INTO `product_document` VALUES (9, 2, 'PROSPECTUS', '成长进取组合产品招募说明书.pdf', '/documents/PP002_prospectus.pdf', 1024000, '1.0', 1, '2025-07-02 12:55:27', 1);
INSERT INTO `product_document` VALUES (10, 2, 'INVESTMENT_GUIDE', '成长进取组合产品投资指南.pdf', '/documents/PP002_guide.pdf', 512000, '1.0', 1, '2025-07-02 12:55:27', 1);

-- ----------------------------
-- Table structure for product_holding
-- ----------------------------
DROP TABLE IF EXISTS `product_holding`;
CREATE TABLE `product_holding`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `total_units` decimal(15, 4) NOT NULL COMMENT '总份额',
  `available_units` decimal(15, 4) NOT NULL COMMENT '可用份额',
  `frozen_units` decimal(15, 4) NULL DEFAULT 0.0000 COMMENT '冻结份额',
  `total_amount` decimal(15, 2) NOT NULL COMMENT '总金额',
  `available_amount` decimal(15, 2) NOT NULL COMMENT '可用金额',
  `frozen_amount` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '冻结金额',
  `avg_cost` decimal(10, 4) NULL DEFAULT NULL COMMENT '平均成本',
  `current_nav` decimal(10, 4) NULL DEFAULT NULL COMMENT '当前净值',
  `market_value` decimal(15, 2) NULL DEFAULT NULL COMMENT '市值',
  `unrealized_pnl` decimal(15, 2) NULL DEFAULT NULL COMMENT '未实现盈亏',
  `realized_pnl` decimal(15, 2) NULL DEFAULT NULL COMMENT '已实现盈亏',
  `total_pnl` decimal(15, 2) NULL DEFAULT NULL COMMENT '总盈亏',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_product_user`(`product_id` ASC, `user_id` ASC) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '产品持仓表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product_holding
-- ----------------------------

-- ----------------------------
-- Table structure for product_performance
-- ----------------------------
DROP TABLE IF EXISTS `product_performance`;
CREATE TABLE `product_performance`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `nav_date` date NOT NULL COMMENT '净值日期',
  `nav` decimal(10, 4) NOT NULL COMMENT '单位净值',
  `accumulated_nav` decimal(10, 4) NULL DEFAULT NULL COMMENT '累计净值',
  `daily_return` decimal(10, 4) NULL DEFAULT NULL COMMENT '日收益率',
  `weekly_return` decimal(10, 4) NULL DEFAULT NULL COMMENT '周收益率',
  `monthly_return` decimal(10, 4) NULL DEFAULT NULL COMMENT '月收益率',
  `quarterly_return` decimal(10, 4) NULL DEFAULT NULL COMMENT '季度收益率',
  `yearly_return` decimal(10, 4) NULL DEFAULT NULL COMMENT '年收益率',
  `total_return` decimal(10, 4) NULL DEFAULT NULL COMMENT '累计收益率',
  `benchmark_return` decimal(10, 4) NULL DEFAULT NULL COMMENT '基准收益率',
  `excess_return` decimal(10, 4) NULL DEFAULT NULL COMMENT '超额收益',
  `volatility` decimal(10, 4) NULL DEFAULT NULL COMMENT '波动率',
  `sharpe_ratio` decimal(10, 4) NULL DEFAULT NULL COMMENT '夏普比率',
  `max_drawdown` decimal(10, 4) NULL DEFAULT NULL COMMENT '最大回撤',
  `calmar_ratio` decimal(10, 4) NULL DEFAULT NULL COMMENT '卡玛比率',
  `information_ratio` decimal(10, 4) NULL DEFAULT NULL COMMENT '信息比率',
  `tracking_error` decimal(10, 4) NULL DEFAULT NULL COMMENT '跟踪误差',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_product_nav_date`(`product_id` ASC, `nav_date` ASC) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE,
  INDEX `idx_nav_date`(`nav_date` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '产品业绩表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product_performance
-- ----------------------------
INSERT INTO `product_performance` VALUES (1, 1, '2024-01-15', 1.0856, 1.0856, 0.0023, NULL, NULL, NULL, NULL, 0.0856, 0.0756, 0.0100, 0.0856, 1.1256, 0.0456, NULL, NULL, NULL, '2025-07-02 12:54:33');
INSERT INTO `product_performance` VALUES (2, 1, '2024-01-14', 1.0833, 1.0833, 0.0018, NULL, NULL, NULL, NULL, 0.0833, 0.0733, 0.0100, 0.0833, 1.1233, 0.0433, NULL, NULL, NULL, '2025-07-02 12:54:33');
INSERT INTO `product_performance` VALUES (3, 2, '2024-01-15', 1.1256, 1.1256, 0.0034, NULL, NULL, NULL, NULL, 0.1256, 0.1156, 0.0100, 0.1256, 1.1656, 0.0656, NULL, NULL, NULL, '2025-07-02 12:54:33');
INSERT INTO `product_performance` VALUES (4, 2, '2024-01-14', 1.1222, 1.1222, 0.0029, NULL, NULL, NULL, NULL, 0.1222, 0.1122, 0.0100, 0.1222, 1.1622, 0.0622, NULL, NULL, NULL, '2025-07-02 12:54:33');

-- ----------------------------
-- Table structure for product_portfolio_config
-- ----------------------------
DROP TABLE IF EXISTS `product_portfolio_config`;
CREATE TABLE `product_portfolio_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `portfolio_id` bigint NOT NULL COMMENT '组合ID',
  `weight` decimal(5, 4) NOT NULL COMMENT '权重',
  `is_active` tinyint NULL DEFAULT 1 COMMENT '是否启用：1-启用，0-禁用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_product_portfolio`(`product_id` ASC, `portfolio_id` ASC) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE,
  INDEX `idx_portfolio_id`(`portfolio_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '产品组合配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product_portfolio_config
-- ----------------------------
INSERT INTO `product_portfolio_config` VALUES (1, 1, 1, 0.6000, 1, '2025-07-02 12:54:33', '2025-07-02 12:54:33');
INSERT INTO `product_portfolio_config` VALUES (2, 1, 2, 0.4000, 1, '2025-07-02 12:54:33', '2025-07-02 12:54:33');
INSERT INTO `product_portfolio_config` VALUES (3, 2, 1, 0.3000, 1, '2025-07-02 12:54:33', '2025-07-02 12:54:33');
INSERT INTO `product_portfolio_config` VALUES (4, 2, 2, 0.7000, 1, '2025-07-02 12:54:33', '2025-07-02 12:54:33');

-- ----------------------------
-- Table structure for product_strategy_config
-- ----------------------------
DROP TABLE IF EXISTS `product_strategy_config`;
CREATE TABLE `product_strategy_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `strategy_id` bigint NOT NULL COMMENT '策略ID',
  `weight` decimal(5, 4) NOT NULL COMMENT '权重',
  `is_active` tinyint NULL DEFAULT 1 COMMENT '是否启用：1-启用，0-禁用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_product_strategy`(`product_id` ASC, `strategy_id` ASC) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE,
  INDEX `idx_strategy_id`(`strategy_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '产品策略配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product_strategy_config
-- ----------------------------
INSERT INTO `product_strategy_config` VALUES (1, 1, 7, 0.5000, 1, '2025-07-02 12:54:33', '2025-07-02 12:54:33');
INSERT INTO `product_strategy_config` VALUES (2, 1, 8, 0.5000, 1, '2025-07-02 12:54:33', '2025-07-02 12:54:33');
INSERT INTO `product_strategy_config` VALUES (3, 2, 7, 0.3000, 1, '2025-07-02 12:54:33', '2025-07-02 12:54:33');
INSERT INTO `product_strategy_config` VALUES (4, 2, 8, 0.7000, 1, '2025-07-02 12:54:33', '2025-07-02 12:54:33');

-- ----------------------------
-- Table structure for product_transaction
-- ----------------------------
DROP TABLE IF EXISTS `product_transaction`;
CREATE TABLE `product_transaction`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `transaction_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '交易流水号',
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `transaction_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '交易类型：SUBSCRIPTION-申购/REDEMPTION-赎回',
  `transaction_amount` decimal(15, 2) NOT NULL COMMENT '交易金额',
  `transaction_units` decimal(15, 4) NULL DEFAULT NULL COMMENT '交易份额',
  `nav` decimal(10, 4) NOT NULL COMMENT '交易净值',
  `nav_date` date NOT NULL COMMENT '净值日期',
  `fee_amount` decimal(15, 2) NULL DEFAULT NULL COMMENT '手续费',
  `net_amount` decimal(15, 2) NULL DEFAULT NULL COMMENT '净金额',
  `transaction_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'PENDING' COMMENT '交易状态：PENDING-待确认/CONFIRMED-已确认/FAILED-失败/CANCELLED-已取消',
  `confirm_time` datetime NULL DEFAULT NULL COMMENT '确认时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_transaction_code`(`transaction_code` ASC) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_transaction_type`(`transaction_type` ASC) USING BTREE,
  INDEX `idx_transaction_status`(`transaction_status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '产品申购赎回记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product_transaction
-- ----------------------------

-- ----------------------------
-- Table structure for rebalance_plan
-- ----------------------------
DROP TABLE IF EXISTS `rebalance_plan`;
CREATE TABLE `rebalance_plan`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `plan_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调仓计划名称',
  `portfolio_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '组合名称',
  `portfolio_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '组合类型',
  `current_holdings` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '当前持仓快照(JSON)',
  `rebalance_instructions` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '调仓指令列表(JSON)',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'PENDING' COMMENT '计划状态',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '调仓计划表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rebalance_plan
-- ----------------------------
INSERT INTO `rebalance_plan` VALUES (2, '2024年二季度调仓', '成长进取组合', '组合', '{\"A股\":200000,\"港股\":80000}', '[{\"action\":\"买入\",\"asset\":\"港股\",\"amount\":20000}]', 'DISPATCHED', '2025-07-05 00:46:40', '2025-07-05 00:46:40');
INSERT INTO `rebalance_plan` VALUES (3, '2024年三季度调仓', '价值投资组合', '定制', '{\"A股\":150000,\"美股\":60000}', '[{\"action\":\"卖出\",\"asset\":\"A股\",\"amount\":5000}]', 'DISPATCHED', '2025-07-05 00:46:40', '2025-07-05 00:46:40');
INSERT INTO `rebalance_plan` VALUES (4, '测试', '测试', 'FOF', '[\n  {\n    \"id\": 1,\n    \"accountId\": 1,\n    \"fundId\": 1,\n    \"totalQuantity\": 10000,\n    \"availableQuantity\": 10000,\n    \"frozenQuantity\": 0,\n    \"avgCost\": 1.25,\n    \"marketPrice\": 1.275,\n    \"marketValue\": 12750,\n    \"unrealizedPnl\": 250,\n    \"realizedPnl\": 0,\n    \"totalPnl\": 250,\n    \"updateTime\": \"2025-07-02T14:11:30\"\n  },\n  {\n    \"id\": 2,\n    \"accountId\": 1,\n    \"fundId\": 2,\n    \"totalQuantity\": 2000,\n    \"availableQuantity\": 2000,\n    \"frozenQuantity\": 0,\n    \"avgCost\": 1.18,\n    \"marketPrice\": 1.185,\n    \"marketValue\": 2370,\n    \"unrealizedPnl\": 10,\n    \"realizedPnl\": 0,\n    \"totalPnl\": 10,\n    \"updateTime\": \"2025-07-02T14:11:30\"\n  }\n]', NULL, 'PENDING', NULL, NULL);

-- ----------------------------
-- Table structure for risk_control
-- ----------------------------
DROP TABLE IF EXISTS `risk_control`;
CREATE TABLE `risk_control`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_id` bigint NOT NULL COMMENT '账户ID',
  `risk_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '风险类型：POSITION_LIMIT-持仓限制/SINGLE_STOCK_LIMIT-单股限制/DAILY_LOSS_LIMIT-日亏损限制',
  `risk_value` decimal(15, 4) NOT NULL COMMENT '风险值',
  `current_value` decimal(15, 4) NULL DEFAULT 0.0000 COMMENT '当前值',
  `is_active` tinyint NULL DEFAULT 1 COMMENT '是否启用：1-启用，0-禁用',
  `alert_threshold` decimal(15, 4) NULL DEFAULT NULL COMMENT '预警阈值',
  `stop_threshold` decimal(15, 4) NULL DEFAULT NULL COMMENT '止损阈值',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_account_risk_type`(`account_id` ASC, `risk_type` ASC) USING BTREE,
  INDEX `idx_account_id`(`account_id` ASC) USING BTREE,
  INDEX `idx_risk_type`(`risk_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '风险控制表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of risk_control
-- ----------------------------
INSERT INTO `risk_control` VALUES (1, 1, 'POSITION_LIMIT', 0.3000, 0.2500, 1, 0.2500, 0.3000, '单只基金持仓限制30%', '2025-07-02 14:11:30', '2025-07-02 14:11:30');
INSERT INTO `risk_control` VALUES (2, 1, 'DAILY_LOSS_LIMIT', 0.0500, 0.0200, 1, 0.0300, 0.0500, '日亏损限制5%', '2025-07-02 14:11:30', '2025-07-02 14:11:30');
INSERT INTO `risk_control` VALUES (3, 2, 'POSITION_LIMIT', 0.2000, 0.1800, 1, 0.1500, 0.2000, '单只基金持仓限制20%', '2025-07-02 14:11:30', '2025-07-02 14:11:30');
INSERT INTO `risk_control` VALUES (4, 2, 'DAILY_LOSS_LIMIT', 0.0300, 0.0100, 1, 0.0200, 0.0300, '日亏损限制3%', '2025-07-02 14:11:30', '2025-07-02 14:11:30');
INSERT INTO `risk_control` VALUES (5, 3, 'POSITION_LIMIT', 0.2500, 0.2200, 1, 0.2000, 0.2500, '单只基金持仓限制25%', '2025-07-02 14:11:30', '2025-07-02 14:11:30');

-- ----------------------------
-- Table structure for strategy
-- ----------------------------
DROP TABLE IF EXISTS `strategy`;
CREATE TABLE `strategy`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `strategy_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '策略代码',
  `strategy_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '策略名称',
  `strategy_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '策略类型：多因子、单因子、技术分析、基本面等',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '策略描述',
  `portfolio_id` bigint NULL DEFAULT NULL COMMENT '关联的因子组合ID',
  `risk_level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '风险等级：低、中、高',
  `target_return` decimal(10, 4) NULL DEFAULT NULL COMMENT '目标收益率',
  `max_drawdown` decimal(10, 4) NULL DEFAULT NULL COMMENT '最大回撤限制',
  `rebalance_frequency` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '调仓频率：日、周、月、季',
  `benchmark` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '基准指数',
  `creator_id` bigint NULL DEFAULT NULL COMMENT '创建者ID',
  `is_public` tinyint NULL DEFAULT 1 COMMENT '是否公开：1-公开，0-私有',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `strategy_code`(`strategy_code` ASC) USING BTREE,
  INDEX `idx_strategy_code`(`strategy_code` ASC) USING BTREE,
  INDEX `idx_strategy_type`(`strategy_type` ASC) USING BTREE,
  INDEX `idx_creator_id`(`creator_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `portfolio_id`(`portfolio_id` ASC) USING BTREE,
  CONSTRAINT `strategy_ibfk_1` FOREIGN KEY (`portfolio_id`) REFERENCES `factor_portfolio` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '策略表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of strategy
-- ----------------------------
INSERT INTO `strategy` VALUES (7, 'MULTI_FACTOR_STRATEGY', '多因子策略', '多因子', '基于价值、质量、动量的多因子策略', 1, '中', 0.1500, 0.2000, '月', '沪深300', 1, 1, 1, '2025-07-02 12:34:54', '2025-07-02 12:34:54');
INSERT INTO `strategy` VALUES (8, 'MOMENTUM_STRATEGY', '动量策略', '技术分析', '基于价格动量的技术分析策略', 2, '高', 0.2000, 0.2500, '周', '中证500', 1, 1, 1, '2025-07-02 12:34:54', '2025-07-02 12:34:54');
INSERT INTO `strategy` VALUES (9, '996', '996', '多因子', '996', NULL, '低', 0.0100, 0.0100, '日', '996', NULL, 1, 1, '2025-07-02 12:39:06', '2025-07-02 12:39:06');

-- ----------------------------
-- Table structure for strategy_backtest
-- ----------------------------
DROP TABLE IF EXISTS `strategy_backtest`;
CREATE TABLE `strategy_backtest`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `strategy_id` bigint NOT NULL COMMENT '策略ID',
  `backtest_date` date NOT NULL COMMENT '回测日期',
  `start_date` date NOT NULL COMMENT '回测开始日期',
  `end_date` date NOT NULL COMMENT '回测结束日期',
  `total_return` decimal(10, 6) NULL DEFAULT NULL COMMENT '总收益率',
  `annual_return` decimal(10, 6) NULL DEFAULT NULL COMMENT '年化收益率',
  `volatility` decimal(10, 6) NULL DEFAULT NULL COMMENT '波动率',
  `sharpe_ratio` decimal(10, 6) NULL DEFAULT NULL COMMENT '夏普比率',
  `max_drawdown` decimal(10, 6) NULL DEFAULT NULL COMMENT '最大回撤',
  `win_rate` decimal(10, 4) NULL DEFAULT NULL COMMENT '胜率',
  `turnover_rate` decimal(10, 4) NULL DEFAULT NULL COMMENT '换手率',
  `benchmark_return` decimal(10, 6) NULL DEFAULT NULL COMMENT '基准收益率',
  `excess_return` decimal(10, 6) NULL DEFAULT NULL COMMENT '超额收益率',
  `information_ratio` decimal(10, 6) NULL DEFAULT NULL COMMENT '信息比率',
  `calmar_ratio` decimal(10, 6) NULL DEFAULT NULL COMMENT '卡玛比率',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_strategy_id`(`strategy_id` ASC) USING BTREE,
  INDEX `idx_backtest_date`(`backtest_date` ASC) USING BTREE,
  INDEX `idx_start_date`(`start_date` ASC) USING BTREE,
  INDEX `idx_end_date`(`end_date` ASC) USING BTREE,
  CONSTRAINT `strategy_backtest_ibfk_1` FOREIGN KEY (`strategy_id`) REFERENCES `strategy` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '策略回测结果表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of strategy_backtest
-- ----------------------------
INSERT INTO `strategy_backtest` VALUES (7, 7, '2024-01-15', '2023-01-01', '2024-01-15', 0.185600, 0.156800, 0.125600, 1.245600, 0.085600, 0.6256, 0.0856, 0.125600, 0.060000, 0.485600, 1.835600, '2025-07-02 12:38:15');
INSERT INTO `strategy_backtest` VALUES (8, 8, '2024-01-15', '2023-01-01', '2024-01-15', 0.225600, 0.195600, 0.185600, 1.055600, 0.125600, 0.5856, 0.1256, 0.125600, 0.070000, 0.555600, 1.555600, '2025-07-02 12:38:15');

-- ----------------------------
-- Table structure for strategy_monitor
-- ----------------------------
DROP TABLE IF EXISTS `strategy_monitor`;
CREATE TABLE `strategy_monitor`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `strategy_id` bigint NOT NULL COMMENT '策略ID',
  `monitor_date` date NOT NULL COMMENT '监控日期',
  `current_return` decimal(10, 6) NULL DEFAULT NULL COMMENT '当前收益率',
  `current_drawdown` decimal(10, 6) NULL DEFAULT NULL COMMENT '当前回撤',
  `risk_alert` tinyint NULL DEFAULT 0 COMMENT '风险预警：1-预警，0-正常',
  `alert_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '预警信息',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '监控状态：NORMAL-正常，WARNING-预警，DANGER-危险',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_strategy_id`(`strategy_id` ASC) USING BTREE,
  INDEX `idx_monitor_date`(`monitor_date` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  CONSTRAINT `strategy_monitor_ibfk_1` FOREIGN KEY (`strategy_id`) REFERENCES `strategy` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '策略监控表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of strategy_monitor
-- ----------------------------

-- ----------------------------
-- Table structure for strategy_parameter
-- ----------------------------
DROP TABLE IF EXISTS `strategy_parameter`;
CREATE TABLE `strategy_parameter`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `strategy_id` bigint NOT NULL COMMENT '策略ID',
  `param_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '参数名称',
  `param_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '参数值',
  `param_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数类型：string、number、boolean、json',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数描述',
  `is_required` tinyint NULL DEFAULT 0 COMMENT '是否必需：1-必需，0-可选',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_strategy_id`(`strategy_id` ASC) USING BTREE,
  INDEX `idx_param_name`(`param_name` ASC) USING BTREE,
  CONSTRAINT `strategy_parameter_ibfk_1` FOREIGN KEY (`strategy_id`) REFERENCES `strategy` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '策略参数表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of strategy_parameter
-- ----------------------------
INSERT INTO `strategy_parameter` VALUES (15, 7, 'lookback_period', '252', 'number', '回看期（交易日）', 1, '2025-07-02 12:37:14', '2025-07-02 12:37:14');
INSERT INTO `strategy_parameter` VALUES (16, 7, 'rebalance_threshold', '0.05', 'number', '调仓阈值', 1, '2025-07-02 12:37:14', '2025-07-02 12:37:14');
INSERT INTO `strategy_parameter` VALUES (17, 7, 'max_position_weight', '0.1', 'number', '最大单只基金权重', 1, '2025-07-02 12:37:14', '2025-07-02 12:37:14');
INSERT INTO `strategy_parameter` VALUES (18, 8, 'momentum_period', '20', 'number', '动量计算周期', 1, '2025-07-02 12:37:14', '2025-07-02 12:37:14');
INSERT INTO `strategy_parameter` VALUES (19, 8, 'volatility_period', '60', 'number', '波动率计算周期', 1, '2025-07-02 12:37:14', '2025-07-02 12:37:14');

-- ----------------------------
-- Table structure for strategy_position
-- ----------------------------
DROP TABLE IF EXISTS `strategy_position`;
CREATE TABLE `strategy_position`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `strategy_id` bigint NOT NULL COMMENT '策略ID',
  `fund_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '基金代码',
  `fund_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '基金名称',
  `weight` decimal(10, 6) NULL DEFAULT NULL COMMENT '权重',
  `shares` decimal(20, 4) NULL DEFAULT NULL COMMENT '持仓份额',
  `market_value` decimal(20, 2) NULL DEFAULT NULL COMMENT '市值',
  `cost_price` decimal(10, 4) NULL DEFAULT NULL COMMENT '成本价',
  `current_price` decimal(10, 4) NULL DEFAULT NULL COMMENT '当前价',
  `profit_loss` decimal(20, 2) NULL DEFAULT NULL COMMENT '盈亏',
  `profit_loss_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '盈亏率',
  `position_date` date NOT NULL COMMENT '持仓日期',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_strategy_id`(`strategy_id` ASC) USING BTREE,
  INDEX `idx_fund_code`(`fund_code` ASC) USING BTREE,
  INDEX `idx_position_date`(`position_date` ASC) USING BTREE,
  CONSTRAINT `strategy_position_ibfk_1` FOREIGN KEY (`strategy_id`) REFERENCES `strategy` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '策略持仓表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of strategy_position
-- ----------------------------

-- ----------------------------
-- Table structure for strategy_trade
-- ----------------------------
DROP TABLE IF EXISTS `strategy_trade`;
CREATE TABLE `strategy_trade`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `strategy_id` bigint NOT NULL COMMENT '策略ID',
  `fund_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '基金代码',
  `fund_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '基金名称',
  `trade_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '交易类型：BUY-买入，SELL-卖出',
  `trade_date` date NOT NULL COMMENT '交易日期',
  `shares` decimal(20, 4) NULL DEFAULT NULL COMMENT '交易份额',
  `price` decimal(10, 4) NULL DEFAULT NULL COMMENT '交易价格',
  `amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '交易金额',
  `commission` decimal(10, 2) NULL DEFAULT NULL COMMENT '手续费',
  `reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '交易原因',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_strategy_id`(`strategy_id` ASC) USING BTREE,
  INDEX `idx_fund_code`(`fund_code` ASC) USING BTREE,
  INDEX `idx_trade_date`(`trade_date` ASC) USING BTREE,
  INDEX `idx_trade_type`(`trade_type` ASC) USING BTREE,
  CONSTRAINT `strategy_trade_ibfk_1` FOREIGN KEY (`strategy_id`) REFERENCES `strategy` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '策略交易记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of strategy_trade
-- ----------------------------

-- ----------------------------
-- Table structure for trade_account
-- ----------------------------
DROP TABLE IF EXISTS `trade_account`;
CREATE TABLE `trade_account`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账户代码',
  `account_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账户名称',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `account_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账户类型：个人/机构/产品',
  `broker` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '券商',
  `account_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'ACTIVE' COMMENT '账户状态：ACTIVE-正常/SUSPENDED-暂停/CLOSED-关闭',
  `total_assets` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '总资产',
  `available_cash` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '可用资金',
  `frozen_cash` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '冻结资金',
  `market_value` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '市值',
  `unrealized_pnl` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '未实现盈亏',
  `realized_pnl` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '已实现盈亏',
  `total_pnl` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '总盈亏',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_account_code`(`account_code` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_account_status`(`account_status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '交易账户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of trade_account
-- ----------------------------
INSERT INTO `trade_account` VALUES (1, 'ACC001', '个人投资账户A', 1, '个人', '中信证券', 'ACTIVE', 1000000.00, 500000.00, 0.00, 500000.00, 25000.00, 15000.00, 40000.00, '2025-07-02 14:11:30', '2025-07-02 14:11:30');
INSERT INTO `trade_account` VALUES (2, 'ACC002', '机构投资账户B', 2, '机构', '华泰证券', 'ACTIVE', 5000000.00, 2000000.00, 0.00, 3000000.00, 150000.00, 80000.00, 230000.00, '2025-07-02 14:11:30', '2025-07-02 14:11:30');
INSERT INTO `trade_account` VALUES (3, 'ACC003', '产品投资账户C', 3, '产品', '国泰君安', 'ACTIVE', 2000000.00, 800000.00, 0.00, 1200000.00, 60000.00, 30000.00, 90000.00, '2025-07-02 14:11:30', '2025-07-02 15:45:12');
INSERT INTO `trade_account` VALUES (4, 'ACC004', '个人投资账户D', 4, '个人', '招商证券', 'ACTIVE', 800000.00, 400000.00, 0.00, 400000.00, 20000.00, 10000.00, 30000.00, '2025-07-02 14:11:30', '2025-07-02 15:45:16');
INSERT INTO `trade_account` VALUES (5, 'ACC005', '机构投资账户E', 5, '机构', '广发证券', 'SUSPENDED', 3000000.00, 1500000.00, 0.00, 1500000.00, 75000.00, 45000.00, 120000.00, '2025-07-02 14:11:30', '2025-07-02 15:45:21');
INSERT INTO `trade_account` VALUES (6, '996', '996', 996, '机构', '996', 'ACTIVE', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-07-02 14:53:25', '2025-07-02 16:09:01');

-- ----------------------------
-- Table structure for trade_execution
-- ----------------------------
DROP TABLE IF EXISTS `trade_execution`;
CREATE TABLE `trade_execution`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `execution_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '成交编号',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `account_id` bigint NOT NULL COMMENT '账户ID',
  `fund_id` bigint NOT NULL COMMENT '基金ID',
  `execution_price` decimal(10, 4) NOT NULL COMMENT '成交价格',
  `execution_quantity` decimal(15, 4) NOT NULL COMMENT '成交数量',
  `execution_amount` decimal(15, 2) NOT NULL COMMENT '成交金额',
  `commission` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '手续费',
  `net_amount` decimal(15, 2) NOT NULL COMMENT '净金额',
  `execution_time` datetime NOT NULL COMMENT '成交时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_execution_code`(`execution_code` ASC) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_account_id`(`account_id` ASC) USING BTREE,
  INDEX `idx_fund_id`(`fund_id` ASC) USING BTREE,
  INDEX `idx_execution_time`(`execution_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '交易成交表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of trade_execution
-- ----------------------------
INSERT INTO `trade_execution` VALUES (1, 'EXE001', 1, 1, 1, 1.2500, 10000.0000, 12500.00, 12.50, 12487.50, '2024-01-15 09:31:00', '2025-07-02 14:11:30');
INSERT INTO `trade_execution` VALUES (2, 'EXE002', 2, 1, 2, 1.1800, 3000.0000, 3540.00, 3.54, 3536.46, '2024-01-15 10:16:00', '2025-07-02 14:11:30');
INSERT INTO `trade_execution` VALUES (3, 'EXE003', 2, 1, 2, 1.1750, 2000.0000, 2350.00, 2.35, 2347.65, '2024-01-15 10:20:00', '2025-07-02 14:11:30');

-- ----------------------------
-- Table structure for trade_order
-- ----------------------------
DROP TABLE IF EXISTS `trade_order`;
CREATE TABLE `trade_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `account_id` bigint NOT NULL COMMENT '账户ID',
  `strategy_id` bigint NULL DEFAULT NULL COMMENT '策略ID',
  `fund_id` bigint NOT NULL COMMENT '基金ID',
  `order_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单类型：BUY-买入/SELL-卖出',
  `order_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'PENDING' COMMENT '订单状态：PENDING-待执行/EXECUTING-执行中/PARTIAL-部分成交/FILLED-全部成交/CANCELLED-已取消/REJECTED-已拒绝',
  `order_biz_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'NORMAL' COMMENT '业务类型：NORMAL-普通/REBALANCE-调仓补单/OTHER-其他',
  `order_price` decimal(10, 4) NOT NULL COMMENT '委托价格',
  `order_quantity` decimal(15, 4) NOT NULL COMMENT '委托数量',
  `filled_quantity` decimal(15, 4) NULL DEFAULT 0.0000 COMMENT '已成交数量',
  `filled_amount` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '已成交金额',
  `avg_fill_price` decimal(10, 4) NULL DEFAULT NULL COMMENT '平均成交价格',
  `commission` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '手续费',
  `order_time` datetime NOT NULL COMMENT '委托时间',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '过期时间',
  `cancel_time` datetime NULL DEFAULT NULL COMMENT '取消时间',
  `cancel_reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '取消原因',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_code`(`order_code` ASC) USING BTREE,
  INDEX `idx_account_id`(`account_id` ASC) USING BTREE,
  INDEX `idx_strategy_id`(`strategy_id` ASC) USING BTREE,
  INDEX `idx_fund_id`(`fund_id` ASC) USING BTREE,
  INDEX `idx_order_status`(`order_status` ASC) USING BTREE,
  INDEX `idx_order_time`(`order_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '交易订单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of trade_order
-- ----------------------------
INSERT INTO `trade_order` VALUES (1, 'ORD001', 1, 1, 1, 'BUY', 'FILLED', 'OPEN', 1.2500, 10000.0000, 10000.0000, 12500.00, 1.2500, 12.50, '2024-01-15 09:30:00', NULL, NULL, NULL, '买入基金A', '2025-07-02 14:11:30', '2025-07-05 04:18:36');
INSERT INTO `trade_order` VALUES (2, 'ORD002', 1, 1, 2, 'SELL', 'PARTIAL', 'OPEN', 1.1800, 5000.0000, 3000.0000, 3540.00, 1.1800, 3.54, '2024-01-15 10:15:00', NULL, NULL, NULL, '卖出基金B', '2025-07-02 14:11:30', '2025-07-05 04:18:36');
INSERT INTO `trade_order` VALUES (3, 'ORD003', 2, 2, 3, 'BUY', 'PENDING', 'OPEN', 1.3200, 8000.0000, 0.0000, 0.00, NULL, 0.00, '2024-01-15 11:00:00', NULL, NULL, NULL, '买入基金C', '2025-07-02 14:11:30', '2025-07-05 04:18:36');
INSERT INTO `trade_order` VALUES (4, 'ORD004', 2, 2, 4, 'SELL', 'CANCELLED', 'OPEN', 1.1500, 6000.0000, 0.0000, 0.00, NULL, 0.00, '2024-01-15 14:30:00', NULL, NULL, NULL, '卖出基金D', '2025-07-02 14:11:30', '2025-07-05 04:18:36');
INSERT INTO `trade_order` VALUES (5, 'ORD005', 3, 3, 5, 'BUY', 'EXECUTING', 'OPEN', 1.2800, 12000.0000, 0.0000, 0.00, NULL, 0.00, '2024-01-15 15:45:00', NULL, NULL, NULL, '买入基金E996', '2025-07-02 14:11:30', '2025-07-05 04:18:36');
INSERT INTO `trade_order` VALUES (6, 'ORD1003', 1, 1, 1, 'BUY', 'PENDING', 'OPEN', 10.8000, 200.0000, 0.0000, 0.00, NULL, 2.00, '2025-07-05 02:46:27', NULL, NULL, NULL, '测试待执行订单', '2025-07-05 02:46:27', '2025-07-05 04:18:36');
INSERT INTO `trade_order` VALUES (7, 'ORD1004', 2, 2, 2, 'SELL', 'FILLED', 'OPEN', 19.8000, 80.0000, 80.0000, 1584.00, 19.8000, 0.80, '2025-07-05 02:46:27', NULL, NULL, NULL, '测试已成交订单', '2025-07-05 02:46:27', '2025-07-05 04:18:36');
INSERT INTO `trade_order` VALUES (8, 'ORD1001', 1, 1, 1, 'BUY', 'REJECTED', 'OPEN', 10.5000, 100.0000, 0.0000, 0.00, NULL, 1.00, '2025-07-05 02:48:40', NULL, '2025-07-05 02:48:40', '余额不足', '测试失败订单1', '2025-07-05 02:48:40', '2025-07-05 04:18:36');
INSERT INTO `trade_order` VALUES (9, 'ORD1002', 2, 2, 2, 'SELL', 'REJECTED', 'OPEN', 20.0000, 50.0000, 0.0000, 0.00, NULL, 0.50, '2025-07-05 02:48:40', NULL, '2025-07-05 02:48:40', '风控拒绝', '测试失败订单2', '2025-07-05 02:48:40', '2025-07-05 04:18:36');
INSERT INTO `trade_order` VALUES (14, 'ORD006', 1, 1, 1, 'BUY', 'PENDING', 'OPEN', 1.2500, 10000.0000, 0.0000, 0.00, NULL, 0.00, '2024-01-16 09:30:00', NULL, NULL, NULL, '建仓交易单', '2025-07-05 03:56:51', '2025-07-05 04:19:22');
INSERT INTO `trade_order` VALUES (15, 'ORD007', 2, 2, 2, 'SELL', 'PENDING', 'REBALANCE', 1.1800, 5000.0000, 0.0000, 0.00, NULL, 0.00, '2024-01-16 10:15:00', NULL, NULL, NULL, '调仓交易单', '2025-07-05 03:56:51', '2025-07-05 04:19:33');
INSERT INTO `trade_order` VALUES (16, 'ORD008', 3, 3, 3, 'BUY', 'PENDING', 'ERROR_FIX', 1.3200, 8000.0000, 0.0000, 0.00, NULL, 0.00, '2024-01-16 11:00:00', NULL, NULL, NULL, '差错处理交易单', '2025-07-05 03:56:51', '2025-07-05 04:19:44');

-- ----------------------------
-- Table structure for trade_statistics
-- ----------------------------
DROP TABLE IF EXISTS `trade_statistics`;
CREATE TABLE `trade_statistics`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_id` bigint NOT NULL COMMENT '账户ID',
  `stat_date` date NOT NULL COMMENT '统计日期',
  `total_trades` int NULL DEFAULT 0 COMMENT '总交易次数',
  `buy_trades` int NULL DEFAULT 0 COMMENT '买入次数',
  `sell_trades` int NULL DEFAULT 0 COMMENT '卖出次数',
  `total_volume` decimal(15, 4) NULL DEFAULT 0.0000 COMMENT '总成交量',
  `total_amount` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '总成交金额',
  `total_commission` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '总手续费',
  `realized_pnl` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '已实现盈亏',
  `turnover_rate` decimal(10, 4) NULL DEFAULT 0.0000 COMMENT '换手率',
  `win_rate` decimal(10, 4) NULL DEFAULT 0.0000 COMMENT '胜率',
  `avg_hold_days` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '平均持仓天数',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_account_stat_date`(`account_id` ASC, `stat_date` ASC) USING BTREE,
  INDEX `idx_account_id`(`account_id` ASC) USING BTREE,
  INDEX `idx_stat_date`(`stat_date` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '交易统计表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of trade_statistics
-- ----------------------------
INSERT INTO `trade_statistics` VALUES (1, 1, '2024-01-15', 2, 1, 1, 15000.0000, 16040.00, 15.89, 0.00, 0.0160, 0.5000, 5.00, '2025-07-02 14:11:30', '2025-07-02 14:11:30');
INSERT INTO `trade_statistics` VALUES (2, 2, '2024-01-15', 1, 0, 1, 0.0000, 0.00, 0.00, 0.00, 0.0000, 0.0000, 0.00, '2025-07-02 14:11:30', '2025-07-02 14:11:30');
INSERT INTO `trade_statistics` VALUES (3, 3, '2024-01-15', 1, 1, 0, 12000.0000, 15360.00, 15.36, 0.00, 0.0077, 0.0000, 0.00, '2025-07-02 14:11:30', '2025-07-02 14:11:30');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '123456', 'admin@example.com', NULL, '管理员', 1, '2025-07-02 10:42:26', '2025-07-02 10:42:26');
INSERT INTO `user` VALUES (2, 'user1', '123456', 'user1@example.com', NULL, '交易员', 1, '2025-07-02 10:42:26', '2025-07-04 20:40:18');

SET FOREIGN_KEY_CHECKS = 1;
