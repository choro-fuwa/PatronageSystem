# 策略管理前端（patronage-ui）

## 模块简介
本模块为策略管理子系统前端，基于Vue3 + Element Plus + ECharts，支持策略的创建、列表、详情、回测、监控、预警等功能。

## 主要功能
- 策略创建、编辑、列表、详情
- 回测结果展示（含图表）
- 策略监控看板、预警管理
- RESTful API对接后端

## 技术栈
- Vue3
- Element Plus
- Vue Router
- Pinia
- Axios
- ECharts

## 启动方式
1. 安装依赖：`npm install`
2. 启动开发环境：`npm run dev`
3. 打包构建：`npm run build`

## 目录结构
- src/api/         API接口封装
- src/views/       页面视图（策略、回测、监控等）
- src/components/  公共组件
- src/router/      路由配置
- src/store/       状态管理
- src/utils/       工具函数

## 统一响应格式
所有API接口建议返回如下结构：
```json
{
  "success": true,
  "data": {},
  "message": "操作成功"
}
```

## 其他
- 推荐使用VSCode/IDEA开发
- 推荐配合后端Swagger文档调试接口
- 如需扩展页面或组件，请在`src/views`和`src/components`下补充实现 