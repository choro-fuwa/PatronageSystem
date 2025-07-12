# PatronageSystem

## 安装部署指南

### 环境要求
- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Redis
- python 3.9+
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
4. 打开redis
   - 运行redis-server.exe
5. 安装python依赖文件
   ```bash
   pip install -r requirements.txt
   ```
6. 执行 Maven 构建
   ```bash
   mvn clean install
   ```
7. 运行项目
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
