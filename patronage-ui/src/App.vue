<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import {
  DataBoard,
  TrendCharts,
  Monitor,
  Money,
  Warning,
  Document,
  Setting,
  ArrowDown
} from '@element-plus/icons-vue'

const route = useRoute()

// 面包屑导航
const breadcrumbs = computed(() => {
  const paths = route.path.split('/').filter(Boolean)
  const result = [{ path: '/', name: '首页' }]
  
  paths.forEach((path, index) => {
    const fullPath = '/' + paths.slice(0, index + 1).join('/')
    const name = getBreadcrumbName(path)
    result.push({ path: fullPath, name })
  })
  
  return result
})

const getBreadcrumbName = (path) => {
  const nameMap = {
    'dashboard': '仪表盘',
    'strategy': '策略管理',
    'list': '策略列表',
    'create': '创建策略',
    'backtest': '策略回测',
    'monitor': '策略监控',
    'overview': '监控概览',
    'alerts': '预警管理',
    'analysis': '绩效分析',
    'trading': '交易管理',
    'orders': '订单管理',
    'positions': '持仓管理',
    'rebalance': '再平衡',
    'risk': '风险管理',
    'limits': '限额管理',
    'stress': '压力测试',
    'reports': '报告中心',
    'performance': '绩效报告',
    'attribution': '归因分析',
    'settings': '系统设置',
    'profile': '个人设置',
    'system': '系统配置',
    'users': '用户管理'
  }
  return nameMap[path] || path
}
</script>

<template>
  <div id="app">
    <el-container class="main-container">
      <!-- 左侧导航栏 -->
      <el-aside width="250px" class="sidebar">
        <div class="logo">
          <img src="@/assets/logo.svg" alt="Logo" />
          <span>策略管理系统</span>
        </div>
        
        <el-menu
          :default-active="$route.path"
          class="sidebar-menu"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
          router
        >
          <el-menu-item index="/dashboard">
            <el-icon><DataBoard /></el-icon>
            <span>仪表盘</span>
          </el-menu-item>
          
          <el-sub-menu index="/strategy">
            <template #title>
              <el-icon><TrendCharts /></el-icon>
              <span>策略管理</span>
            </template>
            <el-menu-item index="/strategy/list">策略列表</el-menu-item>
            <el-menu-item index="/strategy/create">创建策略</el-menu-item>
            <el-menu-item index="/strategy/backtest">策略回测</el-menu-item>
          </el-sub-menu>
          
          <el-sub-menu index="/monitor">
            <template #title>
              <el-icon><Monitor /></el-icon>
              <span>策略监控</span>
            </template>
            <el-menu-item index="/monitor/overview">监控概览</el-menu-item>
            <el-menu-item index="/monitor/alerts">预警管理</el-menu-item>
            <el-menu-item index="/monitor/analysis">绩效分析</el-menu-item>
          </el-sub-menu>
          
          <el-sub-menu index="/trading">
            <template #title>
              <el-icon><Money /></el-icon>
              <span>交易管理</span>
            </template>
            <el-menu-item index="/trading/orders">订单管理</el-menu-item>
            <el-menu-item index="/trading/positions">持仓管理</el-menu-item>
            <el-menu-item index="/trading/rebalance">再平衡</el-menu-item>
          </el-sub-menu>
          
          <el-sub-menu index="/risk">
            <template #title>
              <el-icon><Warning /></el-icon>
              <span>风险管理</span>
            </template>
            <el-menu-item index="/risk/overview">风险概览</el-menu-item>
            <el-menu-item index="/risk/limits">限额管理</el-menu-item>
            <el-menu-item index="/risk/stress">压力测试</el-menu-item>
          </el-sub-menu>
          
          <el-sub-menu index="/reports">
            <template #title>
              <el-icon><Document /></el-icon>
              <span>报告中心</span>
            </template>
            <el-menu-item index="/reports/performance">绩效报告</el-menu-item>
            <el-menu-item index="/reports/risk">风险报告</el-menu-item>
            <el-menu-item index="/reports/attribution">归因分析</el-menu-item>
          </el-sub-menu>
          
          <el-sub-menu index="/settings">
            <template #title>
              <el-icon><Setting /></el-icon>
              <span>系统设置</span>
            </template>
            <el-menu-item index="/settings/profile">个人设置</el-menu-item>
            <el-menu-item index="/settings/system">系统配置</el-menu-item>
            <el-menu-item index="/settings/users">用户管理</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      
      <!-- 主内容区域 -->
      <el-container>
        <!-- 顶部导航栏 -->
        <el-header class="header">
          <div class="header-left">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item v-for="item in breadcrumbs" :key="item.path" :to="item.path">
                {{ item.name }}
              </el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          
          <div class="header-right">
            <el-dropdown>
              <span class="user-info">
                <el-avatar :size="32" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
                <span>管理员</span>
                <el-icon><ArrowDown /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>个人中心</el-dropdown-item>
                  <el-dropdown-item>修改密码</el-dropdown-item>
                  <el-dropdown-item divided>退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        
        <!-- 内容区域 -->
        <el-main class="main-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped lang="scss">
.main-container {
  height: 100vh;
  
  .sidebar {
    background-color: #304156;
    color: #bfcbd9;
    
    .logo {
      height: 60px;
      display: flex;
      align-items: center;
      padding: 0 20px;
      background-color: #2b2f3a;
      color: #fff;
      font-size: 16px;
      font-weight: bold;
      
      img {
        width: 32px;
        height: 32px;
        margin-right: 10px;
      }
    }
    
    .sidebar-menu {
      border: none;
      
      .el-menu-item, .el-sub-menu__title {
        height: 50px;
        line-height: 50px;
        
        &:hover {
          background-color: #263445;
        }
      }
      
      .el-menu-item.is-active {
        background-color: #1890ff;
        color: #fff;
      }
    }
  }
  
  .header {
    background-color: #fff;
    border-bottom: 1px solid #e6e6e6;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
    
    .header-left {
      .el-breadcrumb {
        font-size: 14px;
      }
    }
    
    .header-right {
      .user-info {
        display: flex;
        align-items: center;
        cursor: pointer;
        padding: 8px 12px;
        border-radius: 4px;
        transition: background-color 0.3s;
        
        &:hover {
          background-color: #f5f5f5;
        }
        
        .el-avatar {
          margin-right: 8px;
        }
        
        span {
          margin-right: 8px;
          color: #333;
        }
      }
    }
  }
  
  .main-content {
    background-color: #f0f2f5;
    padding: 20px;
    overflow-y: auto;
  }
}

// 全局样式
#app {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>
