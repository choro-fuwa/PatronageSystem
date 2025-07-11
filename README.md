# 智能投顾系统

## 项目简介
智能投顾系统是一个集基金研究、因子管理、策略管理、组合产品管理、交易管理和实时访问统计于一体的综合性投顾平台。系统支持公募基金、基金公司、基金经理等多维度数据的查询与分析，提供因子分类、因子有效性分析、策略配置与回测、投资组合管理、自动化交易执行等核心功能。同时，系统还具备实时访问统计和可视化展示，帮助用户全面掌握投资动态与系统使用情况。

## 功能说明

### 核心功能模块
- 基金研究子系统
- 因子管理子系统
- 策略管理子系统
- 组合产品管理子系统
- 交易管理子系统
- 实时访问统计功能

## 安装部署指南

### 环境要求
- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Redis

### 后端部署
1. 克隆项目到本地
2. 配置数据库
   ```sql
   # 创建数据库
   CREATE DATABASE patronage_system;
   use patronage_system;
   source patronage_system.sql;
   ```
3. 修改配置文件
   - 配置 `application.yml` 中的数据库连接信息
   - 配置 Redis 连接信息
4. 执行 Maven 构建
   ```bash
   mvn clean install
   ```
5. 运行项目
   ```bash
   mvn spring-boot:run
   ```

### 前端部署
1. 进入前端项目目录
   ```bash
   cd patronage-vue
   ```
2. 安装依赖
   ```bash
   npm install
   ```
3. 开发环境运行
   ```bash
   npm run dev
   ```
