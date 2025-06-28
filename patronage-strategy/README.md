# 策略管理子系统

## 项目概述

策略管理子系统是Patronage量化投资平台的核心模块之一，负责策略的创建、回测、监控和预警等功能。

## 技术架构

### 后端技术栈
- **Spring Boot 2.7+**: 主框架
- **MyBatis-Plus**: ORM框架
- **MySQL**: 主数据库
- **Redis**: 缓存
- **Kafka**: 消息队列
- **Python**: 回测引擎
- **Jython**: Python与Java集成

### 前端技术栈
- **Vue 3**: 前端框架
- **Element Plus**: UI组件库
- **ECharts**: 数据可视化
- **Axios**: HTTP客户端

## 功能模块

### 1. 策略管理
- 策略创建与编辑
- 策略列表查询
- 策略状态管理（启动/停止/暂停）
- 策略参数配置

### 2. 回测引擎
- 历史数据回测
- 多策略回测
- 回测结果分析
- 性能指标计算

### 3. 监控预警
- 实时策略监控
- 预警规则配置
- 预警通知推送
- 预警历史记录

### 4. 持仓管理
- 策略持仓查询
- 持仓分析
- 风险控制

## 数据库设计

### 核心表结构

#### 1. strategy（策略表）
```sql
- id: 主键ID
- strategy_name: 策略名称
- description: 策略描述
- strategy_type: 策略类型
- status: 策略状态
- initial_capital: 初始资金
- current_capital: 当前资金
- max_drawdown: 最大回撤
- annual_return: 年化收益率
- sharpe_ratio: 夏普比率
- parameters: 策略参数（JSON）
```

#### 2. strategy_position（策略持仓表）
```sql
- id: 主键ID
- strategy_id: 策略ID
- stock_code: 股票代码
- stock_name: 股票名称
- quantity: 持仓数量
- cost_price: 持仓成本
- current_price: 当前价格
- market_value: 持仓市值
- unrealized_pnl: 浮动盈亏
- position_ratio: 持仓比例
```

#### 3. backtest_result（回测结果表）
```sql
- id: 主键ID
- strategy_id: 策略ID
- start_date: 回测开始日期
- end_date: 回测结束日期
- initial_capital: 初始资金
- final_capital: 最终资金
- total_return: 总收益率
- annual_return: 年化收益率
- max_drawdown: 最大回撤
- sharpe_ratio: 夏普比率
- win_rate: 胜率
- trade_count: 交易次数
- result_data: 回测结果数据（JSON）
```

#### 4. strategy_alert（策略预警表）
```sql
- id: 主键ID
- strategy_id: 策略ID
- alert_name: 预警名称
- alert_type: 预警类型
- condition_type: 预警条件
- threshold: 预警阈值
- is_enabled: 是否启用
- notification_type: 通知方式
- recipients: 通知接收人
```

#### 5. alert_history（预警历史表）
```sql
- id: 主键ID
- strategy_id: 策略ID
- alert_id: 预警规则ID
- alert_type: 预警类型
- alert_message: 预警消息
- trigger_value: 触发值
- threshold: 阈值
- notification_status: 通知状态
- process_status: 处理状态
```

## API接口文档

### 策略管理接口

#### 1. 分页查询策略列表
```
GET /api/strategy/list
参数：
- pageNum: 页码（默认1）
- pageSize: 页大小（默认10）
- strategyName: 策略名称（可选）
- strategyType: 策略类型（可选）
- status: 策略状态（可选）
```

#### 2. 获取策略详情
```
GET /api/strategy/{id}
```

#### 3. 创建策略
```
POST /api/strategy
请求体：Strategy对象
```

#### 4. 更新策略
```
PUT /api/strategy/{id}
请求体：Strategy对象
```

#### 5. 删除策略
```
DELETE /api/strategy/{id}
```

#### 6. 启动策略
```
POST /api/strategy/{id}/start
```

#### 7. 停止策略
```
POST /api/strategy/{id}/stop
```

#### 8. 暂停策略
```
POST /api/strategy/{id}/pause
```

### 回测接口

#### 1. 执行回测
```
POST /api/backtest/run
请求体：
{
  "strategyId": 1,
  "startDate": "2024-01-01 00:00:00",
  "endDate": "2024-12-31 23:59:59",
  "parameters": {}
}
```

#### 2. 获取回测结果
```
GET /api/backtest/{id}
```

#### 3. 获取回测历史
```
GET /api/backtest/history/{strategyId}
```

### 监控预警接口

#### 1. 创建预警规则
```
POST /api/monitor/alert
请求体：StrategyAlert对象
```

#### 2. 获取策略预警规则
```
GET /api/monitor/alert/{strategyId}
```

#### 3. 获取预警历史
```
GET /api/monitor/history/{strategyId}
参数：
- alertType: 预警类型（可选）
- processStatus: 处理状态（可选）
```

## 部署说明

### 环境要求
- JDK 8+
- MySQL 5.7+
- Redis 5.0+
- Python 3.7+

### 安装步骤

1. **克隆项目**
```bash
git clone <repository-url>
cd patronage-strategy
```

2. **配置数据库**
```bash
# 创建数据库
mysql -u root -p < database/init.sql
```

3. **修改配置文件**
```bash
# 编辑 application.yml
vim src/main/resources/application.yml
```

4. **安装Python依赖**
```bash
pip install pandas numpy
```

5. **启动应用**
```bash
mvn spring-boot:run
```

### 配置说明

#### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/patronage_strategy
    username: root
    password: 123456
```

#### Redis配置
```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: 
    database: 0
```

#### Kafka配置
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
```

## 开发指南

### 项目结构
```
patronage-strategy/
├── src/main/java/com/patronage/strategy/
│   ├── config/           # 配置类
│   ├── controller/       # 控制器
│   ├── entity/          # 实体类
│   ├── mapper/          # Mapper接口
│   ├── service/         # 服务接口
│   │   └── impl/        # 服务实现
│   ├── exception/       # 异常处理
│   └── common/          # 公共类
├── src/main/resources/
│   ├── application.yml  # 配置文件
│   └── mapper/          # MyBatis XML文件
├── python/
│   └── backtest_engine.py  # Python回测引擎
└── database/
    └── init.sql         # 数据库初始化脚本
```

### 开发规范

1. **命名规范**
   - 类名：大驼峰命名法
   - 方法名：小驼峰命名法
   - 常量：全大写+下划线
   - 包名：全小写

2. **代码规范**
   - 使用Lombok简化代码
   - 统一异常处理
   - 统一响应格式
   - 添加详细注释

3. **数据库规范**
   - 使用MyBatis-Plus
   - 字段命名使用下划线
   - 添加索引优化查询

### 测试

#### 单元测试
```bash
mvn test
```

#### 接口测试
```bash
# 启动应用后访问
http://localhost:8081/strategy/swagger-ui.html
```

## 常见问题

### 1. 数据库连接失败
- 检查MySQL服务是否启动
- 检查数据库连接配置
- 检查用户名密码是否正确

### 2. Python回测失败
- 检查Python环境是否正确安装
- 检查pandas、numpy等依赖是否安装
- 检查Python脚本路径是否正确

### 3. Redis连接失败
- 检查Redis服务是否启动
- 检查Redis连接配置
- 检查网络连接

## 更新日志

### v1.0.0 (2024-01-01)
- 初始版本发布
- 实现基础策略管理功能
- 实现回测引擎
- 实现监控预警功能

## 贡献指南

1. Fork项目
2. 创建功能分支
3. 提交代码
4. 创建Pull Request

## 许可证

MIT License
