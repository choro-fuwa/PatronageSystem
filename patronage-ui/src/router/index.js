import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/dashboard'
    },
    {
      path: '/dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index.vue'),
      meta: {
        title: '仪表盘',
        icon: 'DataBoard'
      }
    },
    {
      path: '/strategy',
      name: 'Strategy',
      redirect: '/strategy/list',
      meta: {
        title: '策略管理',
        icon: 'TrendCharts'
      },
      children: [
        {
          path: 'list',
      name: 'StrategyList',
          component: () => import('@/views/strategy/index.vue'),
          meta: {
            title: '策略列表',
            icon: 'List'
          }
        },
        {
          path: 'create',
          name: 'StrategyCreate',
          component: () => import('@/views/strategy/create.vue'),
          meta: {
            title: '创建策略',
            icon: 'Plus'
          }
        },
        {
          path: 'edit/:id',
          name: 'StrategyEdit',
          component: () => import('@/views/strategy/edit.vue'),
          meta: {
            title: '编辑策略',
            icon: 'Edit'
          }
        },
        {
          path: 'detail/:id',
          name: 'StrategyDetail',
          component: () => import('@/views/strategy/detail.vue'),
          meta: {
            title: '策略详情',
            icon: 'View'
          }
        },
        {
          path: 'backtest/:id',
          name: 'StrategyBacktest',
          component: () => import('@/views/strategy/backtest.vue'),
          meta: {
            title: '策略回测',
            icon: 'DataAnalysis'
          }
        },
        {
          path: 'monitor/:id',
          name: 'StrategyMonitor',
          component: () => import('@/views/strategy/monitor.vue'),
          meta: {
            title: '策略监控',
            icon: 'Monitor'
          }
        },
        {
          path: 'adjust/:id',
          name: 'StrategyAdjust',
          component: () => import('@/views/strategy/adjust.vue'),
          meta: {
            title: '策略调整',
            icon: 'Setting'
          }
        }
      ]
    },
    {
      path: '/monitor',
      name: 'Monitor',
      redirect: '/monitor/overview',
      meta: {
        title: '策略监控',
        icon: 'Monitor'
      },
      children: [
        {
          path: 'overview',
          name: 'MonitorOverview',
          component: () => import('@/views/monitor/overview.vue'),
          meta: {
            title: '监控概览',
            icon: 'DataBoard'
          }
        },
        {
          path: 'alerts',
          name: 'MonitorAlerts',
          component: () => import('@/views/monitor/alerts.vue'),
          meta: {
            title: '预警管理',
            icon: 'Warning'
          }
        },
        {
          path: 'analysis',
          name: 'MonitorAnalysis',
          component: () => import('@/views/monitor/analysis.vue'),
          meta: {
            title: '绩效分析',
            icon: 'TrendCharts'
          }
        }
      ]
    },
    {
      path: '/trading',
      name: 'Trading',
      redirect: '/trading/positions',
      meta: {
        title: '交易管理',
        icon: 'Money'
      },
      children: [
        {
          path: 'positions',
          name: 'TradingPositions',
          component: () => import('@/views/trading/positions.vue'),
          meta: {
            title: '持仓管理',
            icon: 'TrendCharts'
          }
        },
        {
          path: 'rebalance',
          name: 'TradingRebalance',
          component: () => import('@/views/trading/rebalance.vue'),
          meta: {
            title: '再平衡',
            icon: 'Refresh'
          }
        }
      ]
    },
    {
      path: '/risk',
      name: 'Risk',
      meta: {
        title: '风险管理',
        icon: 'Warning'
      },
      children: [
        {
          path: 'overview',
          name: 'RiskOverview',
          component: () => import('@/views/risk/overview.vue'),
          meta: {
            title: '风险概览',
            icon: 'DataBoard'
          }
        }
      ]
    },
    {
      path: '/reports',
      name: 'Reports',
      meta: {
        title: '报告中心',
        icon: 'Document'
      },
      children: [
        {
          path: 'performance',
          name: 'ReportsPerformance',
          component: () => import('@/views/reports/performance.vue'),
          meta: {
            title: '绩效报告',
            icon: 'TrendCharts'
          }
        }
      ]
    },
    {
      path: '/settings',
      name: 'Settings',
      redirect: '/settings/profile',
      meta: {
        title: '系统设置',
        icon: 'Setting'
      },
      children: [
        {
          path: 'profile',
          name: 'SettingsProfile',
          component: () => import('@/views/settings/profile.vue'),
          meta: {
            title: '个人设置',
            icon: 'User'
          }
        },
        {
          path: 'system',
          name: 'SettingsSystem',
          component: () => import('@/views/settings/system.vue'),
          meta: {
            title: '系统配置',
            icon: 'Setting'
          }
        },
        {
          path: 'users',
          name: 'SettingsUsers',
          component: () => import('@/views/settings/users.vue'),
          meta: {
            title: '用户管理',
            icon: 'UserFilled'
          }
        }
      ]
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/login/index.vue'),
      meta: {
        title: '登录',
        hidden: true
      }
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: () => import('@/views/error/404.vue'),
      meta: {
        title: '页面不存在',
        hidden: true
      }
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - 策略管理系统`
  }
  
  // 检查登录状态
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    ElMessage.warning('请先登录')
    next('/login')
    return
  }
  
  next()
})

export default router