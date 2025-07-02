 # 风险画像服务 (Risk Profile Service)

基于 Spring Boot + Element Plus 的风险承受能力评测系统

## 项目结构

```
test/
├── risk-profile-frontend.vue    # Vue 3 + Element Plus 前端页面
├── RiskProfileController.java   # Spring Boot 控制器
├── RiskProfileService.java      # 风险评分计算服务
├── RiskProfileRequest.java      # 请求DTO
├── RiskProfileResponse.java     # 响应DTO
├── User.java                    # 用户实体类
├── UserRepository.java          # 数据访问层
├── RiskProfileApplication.java  # 主启动类
├── application.yml              # 配置文件
├── pom.xml                      # Maven配置
└── README.md                    # 项目说明
```

## 功能特性

### 1. 风险评测问卷
- 14个维度的风险承受能力评估
- 包含年龄、收入、投资目标、风险态度等
- 支持多选和单选问题

### 2. 风险评分算法
- 基于权重的评分系统
- 总分395分，分为三个风险等级
- 动态权重配置

### 3. 风险等级划分
- **保守型 (low)**: 0-150分，适合低风险投资
- **稳健型 (mid)**: 151-250分，平衡收益与风险
- **进取型 (high)**: 251-395分，追求高收益

## 快速开始

### 1. 后端启动

```bash
# 1. 确保已安装 Java 11+ 和 Maven
# 2. 配置数据库连接 (application.yml)
# 3. 启动服务
mvn spring-boot:run
```

### 2. 前端使用

```vue
<!-- 在 Vue 项目中使用 -->
<template>
  <RiskProfile />
</template>

<script setup>
import RiskProfile from './test/risk-profile-frontend.vue'
</script>
```

## API 接口

### 提交风险画像问卷

**POST** `/api/risk-profile`

**请求体:**
```json
{
  "age": "18_30",
  "investmentGoal": "personal_goals",
  "expectedReturn": "slightly_higher",
  "redemptionNeed": "no",
  "riskAcceptance": "yes",
  "incomeSource": "salary",
  "annualIncome": "50k_200k",
  "investableProportion": "20%-50%",
  "debtStatus": "manageable",
  "investmentKnowledge": "basic",
  "investmentExperience": "limited",
  "investmentDuration": "medium_term",
  "preferredProducts": ["fund", "stocks"],
  "lossTolerance": "neutral"
}
```

**响应:**
```json
{
  "success": true,
  "message": "问卷提交成功",
  "riskScore": 185,
  "riskLevel": "mid",
  "riskLevelDescription": "稳健型 - 适合中等风险投资，平衡收益与风险",
  "timestamp": 1640995200000
}
```

### 获取风险等级说明

**GET** `/api/risk-levels`

**响应:**
```json
{
  "low": {
    "name": "保守型",
    "description": "适合低风险投资，偏好稳定收益",
    "score_range": "0-150"
  },
  "mid": {
    "name": "稳健型", 
    "description": "适合中等风险投资，平衡收益与风险",
    "score_range": "151-250"
  },
  "high": {
    "name": "进取型",
    "description": "适合高风险投资，追求高收益", 
    "score_range": "251-395"
  }
}
```

## 评分权重配置

| 维度 | 选项 | 权重 |
|------|------|------|
| 年龄 | 18-30岁 | 30分 |
| 投资目标 | 财富增长 | 30分 |
| 期望回报 | 远超过存款利率 | 30分 |
| 风险接受 | 接受不保本产品 | 30分 |
| 收入来源 | 金融投资收入 | 25分 |
| 年收入 | 200万以上 | 30分 |
| 投资占比 | 50%以上 | 30分 |
| 债务状况 | 无负债 | 20分 |
| 投资知识 | 较专业 | 30分 |
| 投资经验 | 丰富经验 | 30分 |
| 投资期限 | 长期 | 30分 |
| 偏好产品 | 期货 | 30分 |
| 亏损容忍 | 风险偏好 | 30分 |

## 数据库设计

### 用户表 (user)

```sql
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(60) NOT NULL,
    nickname VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(11),
    risk_level VARCHAR(20),
    preferred_horizon VARCHAR(20),
    preferred_investment TEXT,
    created_at DATETIME,
    updated_at DATETIME
);
```

## 技术栈

### 后端
- **Spring Boot 2.7.0**
- **Spring Data JPA**
- **MySQL 8.0**
- **Lombok**
- **Jackson**

### 前端
- **Vue 3**
- **Element Plus**
- **Axios**

## 注意事项

1. 确保数据库已创建并配置正确连接信息
2. 前端需要配置正确的API基础URL
3. 问卷所有字段都是必填的
4. 风险评分算法可以根据业务需求调整权重

## 扩展功能

- 用户认证和授权
- 历史风险评估记录
- 个性化投资建议
- 风险评估报告生成
- 数据统计和分析