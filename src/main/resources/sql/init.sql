-- 创建数据库
CREATE DATABASE IF NOT EXISTS Patronage_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE Patronage_system;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `status` tinyint DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 基金公司表
CREATE TABLE IF NOT EXISTS `fund_company` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `company_code` varchar(20) NOT NULL COMMENT '公司代码',
  `company_name` varchar(100) NOT NULL COMMENT '公司名称',
  `company_short_name` varchar(50) DEFAULT NULL COMMENT '公司简称',
  `establish_date` date DEFAULT NULL COMMENT '成立日期',
  `registered_capital` decimal(20,2) DEFAULT NULL COMMENT '注册资本',
  `legal_representative` varchar(50) DEFAULT NULL COMMENT '法定代表人',
  `company_type` varchar(50) DEFAULT NULL COMMENT '公司类型',
  `status` tinyint DEFAULT 1 COMMENT '状态：1-正常，0-停用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_company_code` (`company_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='基金公司表';

-- 基金经理表
CREATE TABLE IF NOT EXISTS `fund_manager` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `manager_code` varchar(20) NOT NULL COMMENT '经理代码',
  `manager_name` varchar(50) NOT NULL COMMENT '经理姓名',
  `company_id` bigint DEFAULT NULL COMMENT '所属公司ID',
  `education` varchar(100) DEFAULT NULL COMMENT '学历',
  `experience_years` int DEFAULT NULL COMMENT '从业年限',
  `introduction` text COMMENT '个人简介',
  `status` tinyint DEFAULT 1 COMMENT '状态：1-正常，0-离职',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_manager_code` (`manager_code`),
  KEY `idx_company_id` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='基金经理表';

-- 基金表
CREATE TABLE IF NOT EXISTS `fund` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fund_code` varchar(20) NOT NULL COMMENT '基金代码',
  `fund_name` varchar(100) NOT NULL COMMENT '基金名称',
  `fund_short_name` varchar(50) DEFAULT NULL COMMENT '基金简称',
  `company_id` bigint DEFAULT NULL COMMENT '基金公司ID',
  `manager_id` bigint DEFAULT NULL COMMENT '基金经理ID',
  `fund_type` varchar(50) DEFAULT NULL COMMENT '基金类型',
  `risk_level` varchar(20) DEFAULT NULL COMMENT '风险等级',
  `establish_date` date DEFAULT NULL COMMENT '成立日期',
  `fund_size` decimal(20,2) DEFAULT NULL COMMENT '基金规模',
  `nav` decimal(10,4) DEFAULT NULL COMMENT '最新净值',
  `nav_date` date DEFAULT NULL COMMENT '净值日期',
  `total_return` decimal(10,4) DEFAULT NULL COMMENT '累计收益率',
  `annual_return` decimal(10,4) DEFAULT NULL COMMENT '年化收益率',
  `max_drawdown` decimal(10,4) DEFAULT NULL COMMENT '最大回撤',
  `sharpe_ratio` decimal(10,4) DEFAULT NULL COMMENT '夏普比率',
  `status` tinyint DEFAULT 1 COMMENT '状态：1-正常，0-停用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_fund_code` (`fund_code`),
  KEY `idx_company_id` (`company_id`),
  KEY `idx_manager_id` (`manager_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='基金表';

-- 基金标签表
CREATE TABLE IF NOT EXISTS `fund_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(50) NOT NULL COMMENT '标签名称',
  `tag_category` varchar(50) DEFAULT NULL COMMENT '标签分类',
  `tag_description` varchar(200) DEFAULT NULL COMMENT '标签描述',
  `status` tinyint DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tag_name` (`tag_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='基金标签表';

-- 基金标签关联表
CREATE TABLE IF NOT EXISTS `fund_tag_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fund_id` bigint NOT NULL COMMENT '基金ID',
  `tag_id` bigint NOT NULL COMMENT '标签ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_fund_tag` (`fund_id`, `tag_id`),
  KEY `idx_fund_id` (`fund_id`),
  KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='基金标签关联表';

-- 基金组合表
CREATE TABLE IF NOT EXISTS `fund_portfolio` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `portfolio_name` varchar(100) NOT NULL COMMENT '组合名称',
  `user_id` bigint NOT NULL COMMENT '创建用户ID',
  `portfolio_type` varchar(50) DEFAULT NULL COMMENT '组合类型',
  `description` text COMMENT '组合描述',
  `status` tinyint DEFAULT 1 COMMENT '状态：1-正常，0-删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='基金组合表';

-- 基金组合明细表
CREATE TABLE IF NOT EXISTS `fund_portfolio_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `portfolio_id` bigint NOT NULL COMMENT '组合ID',
  `fund_id` bigint NOT NULL COMMENT '基金ID',
  `weight` decimal(5,4) DEFAULT NULL COMMENT '权重',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_portfolio_id` (`portfolio_id`),
  KEY `idx_fund_id` (`fund_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='基金组合明细表';

-- 插入测试数据
INSERT INTO `user` (`username`, `password`, `email`, `real_name`) VALUES
('admin', '123456', 'admin@example.com', '管理员'),
('user1', '123456', 'user1@example.com', '测试用户1');

INSERT INTO `fund_company` (`company_code`, `company_name`, `company_short_name`, `establish_date`, `registered_capital`, `legal_representative`, `company_type`) VALUES
('000001', '华夏基金管理有限公司', '华夏基金', '1998-04-09', 23800.00, '张弘弢', '公募基金'),
('000002', '易方达基金管理有限公司', '易方达基金', '2001-04-17', 12000.00, '刘晓艳', '公募基金'),
('000003', '嘉实基金管理有限公司', '嘉实基金', '1999-03-25', 15000.00, '经雷', '公募基金');

INSERT INTO `fund_manager` (`manager_code`, `manager_name`, `company_id`, `education`, `experience_years`, `introduction`) VALUES
('M001', '张坤', 2, '清华大学', 10, '易方达基金明星基金经理'),
('M002', '刘彦春', 2, '北京大学', 15, '易方达基金资深基金经理'),
('M003', '王宗合', 1, '复旦大学', 12, '华夏基金优秀基金经理');

INSERT INTO `fund` (`fund_code`, `fund_name`, `fund_short_name`, `company_id`, `manager_id`, `fund_type`, `risk_level`, `establish_date`, `fund_size`, `nav`, `nav_date`, `total_return`, `annual_return`, `max_drawdown`, `sharpe_ratio`) VALUES
('110022', '易方达消费行业股票', '易方达消费', 2, 1, '股票型', '中高风险', '2010-08-20', 500000.00, 3.2456, '2024-01-15', 224.56, 15.67, -25.34, 1.23),
('000001', '华夏成长混合', '华夏成长', 1, 3, '混合型', '中风险', '2001-12-18', 300000.00, 2.1234, '2024-01-15', 112.34, 8.45, -18.67, 0.89),
('000002', '华夏大盘精选混合', '华夏大盘', 1, 3, '混合型', '中高风险', '2004-08-11', 450000.00, 4.5678, '2024-01-15', 356.78, 12.34, -22.45, 1.45);

INSERT INTO `fund_tag` (`tag_name`, `tag_category`, `tag_description`) VALUES
('消费', '行业', '消费行业相关基金'),
('科技', '行业', '科技行业相关基金'),
('医药', '行业', '医药行业相关基金'),
('蓝筹', '风格', '蓝筹股风格基金'),
('成长', '风格', '成长股风格基金'),
('价值', '风格', '价值股风格基金'),
('大盘', '规模', '大盘股基金'),
('中小盘', '规模', '中小盘股基金');

INSERT INTO `fund_tag_relation` (`fund_id`, `tag_id`) VALUES
(1, 1), (1, 4), (1, 7),  -- 易方达消费：消费、蓝筹、大盘
(2, 4), (2, 5), (2, 7),  -- 华夏成长：蓝筹、成长、大盘
(3, 4), (3, 7);          -- 华夏大盘：蓝筹、大盘 