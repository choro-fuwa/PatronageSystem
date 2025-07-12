<template>
  <div class="layout">
    <!-- 侧边栏 -->
    <div class="sidebar">
      <div class="sidebar-header">
        <h2>智能投顾系统</h2>
      </div>
      <div class="sidebar-menu">
        <div 
          class="menu-item" 
          :class="{ active: currentPath === '/console' }"
          @click="handleMenuClick('/console')"
        >
          <el-icon><DataBoard /></el-icon>
          <span style="margin-left: 8px">控制台</span>
        </div>
        <template v-for="menu in menus" :key="menu.path">
          <div
            v-if="!menu.children"
            class="menu-item"
            :class="{ active: currentPath === menu.path }"
            @click="handleMenuClick(menu.path)"
          >
            <el-icon>
              <component :is="menu.icon" />
            </el-icon>
            <span style="margin-left: 8px">{{ menu.title }}</span>
          </div>
          <div v-else class="menu-item submenu">
            <div class="submenu-title">
              <el-icon>
                <component :is="menu.icon" />
              </el-icon>
              <span style="margin-left: 8px">{{ menu.title }}</span>
            </div>
            <div class="submenu-list">
              <div
                v-for="sub in menu.children"
                :key="sub.path"
                class="submenu-item"
                :class="{ active: currentPath === sub.path }"
                @click="handleMenuClick(sub.path)"
              >
                <el-icon>
                  <component :is="sub.icon" />
                </el-icon>
                <span style="margin-left: 8px">{{ sub.title }}</span>
              </div>
            </div>
          </div>
        </template>
      </div>
    </div>
    
    <!-- 主内容区 -->
    <div class="main-content">
      <!-- 顶部导航 -->
      <div class="header">
        <div class="breadcrumb">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item>首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentMenuTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        
        <div class="user-info">
          <el-dropdown @command="handleCommand">
            <span class="user-dropdown">
              <el-avatar :size="32" icon="UserFilled" />
              <span style="margin-left: 8px">{{ userInfo.realName || userInfo.username }}</span>
              <el-icon>
                <ArrowDown />
              </el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人信息</el-dropdown-item>
                <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
      
      <!-- 路由视图 -->
      <div class="app-container">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { 
  PieChart, 
  OfficeBuilding, 
  User, 
  ArrowDown,
  TrendCharts,
  Box,
  Wallet,
  Document,
  DataBoard,
  View,
  DataLine,
  Location
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

// 菜单配置
const menus = [
  {
    path: '/fund',
    title: '基金研究',
    icon: PieChart
  },
  {
    path: '/company',
    title: '基金公司',
    icon: OfficeBuilding
  },
  {
    path: '/manager',
    title: '基金经理',
    icon: User
  },
  {
    path: '/factor',
    title: '因子管理',
    icon: PieChart
  },
  {
    path: '/strategy',
    title: '策略管理',
    icon: TrendCharts
  },
  {
    path: '/backtest',
    title: '基金回测',
    icon: TrendCharts
  },
  {
    path: '/portfolio-product',
    title: '组合产品管理',
    icon: Box
  },
  {
    path: '/trade',
    title: '交易管理',
    icon: Wallet,
    children: [
      {
        path: '/trade/accounts',
        title: '交易账户管理',
        icon: Wallet
      },
      {
        path: '/trade/orders',
        title: '交易单管理',
        icon: Document
      },
      {
        path: '/trade/error-handling',
        title: '差错处理',
        icon: Document
      },
      {
        path: '/trade/execution',
        title: '交割单管理',
        icon: Document
      }
    ]
  },
  {
    path: '/rebalance-plan',
    title: '调仓计划管理',
    icon: Box
  },
  {
    path: '/account-rebalance',
    title: '账户调仓',
    icon: Wallet
  },
  {
    path: '/display',
    title: '展示模块',
    icon: View,
    children: [
      {
        path: '/display/realtime',
        title: '实时数据',
        icon: DataLine
      },
      {
        path: '/display/china',
        title: '中国地图',
        icon: Location
      }
    ]
  }
]

// 当前路径
const currentPath = computed(() => route.path)

// 当前菜单标题
const currentMenuTitle = computed(() => {
  let menu = menus.find(m => m.path === currentPath.value)
  if (menu) return menu.title
  for (const m of menus) {
    if (m.children) {
      const sub = m.children.find(s => s.path === currentPath.value)
      if (sub) return sub.title
    }
  }
  return ''
})

// 用户信息
const userInfo = ref({})

// 菜单点击处理
const handleMenuClick = (path) => {
  router.push(path)
}

// 用户下拉菜单处理
const handleCommand = async (command) => {
  if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      router.push('/login')
    } catch {
      // 用户取消
    }
  } else if (command === 'profile') {
    // 跳转到个人信息页面
    console.log('个人信息')
  }
}

// 初始化
onMounted(() => {
  const userInfoStr = localStorage.getItem('userInfo')
  if (userInfoStr) {
    userInfo.value = JSON.parse(userInfoStr)
  }
})
</script>

<style scoped>
html, body, #app, .layout {
  margin: 0;
  padding: 0;
  background: #2d3a4b;
}

.layout {
  display: flex;
}

.sidebar {
  width: 220px;
  background: #2d3a4b;
  color: #fff;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.sidebar-header {
  padding: 24px 0 16px 0;
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  letter-spacing: 2px;
  background: #263445;
}

.sidebar-menu {
  flex: 1;
  padding: 16px 0;
}
.menu-item {
  display: flex;
  align-items: center;
  padding: 12px 24px;
  cursor: pointer;
  transition: background 0.2s;
}
.menu-item.active, .submenu-item.active {
  background: #1e90ff;
  color: #fff;
}
.menu-item:hover, .submenu-item:hover {
  background: #3a4a5d;
}
.submenu {
  margin-bottom: 8px;
}
.submenu-title {
  display: flex;
  align-items: center;
  padding: 12px 24px 8px 24px;
  font-weight: bold;
}
.submenu-list {
  padding-left: 24px;
}
.submenu-item {
  display: flex;
  align-items: center;
  padding: 8px 0 8px 0;
  cursor: pointer;
  transition: background 0.2s;
}

.user-dropdown {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 8px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-dropdown:hover {
  background-color: #f5f7fa;
}

.breadcrumb {
  font-size: 14px;
}


.main-content {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  background: #f5f7fa;
}

.app-container {
  flex: 1;
  width: 100%;
  min-width: 0;
  box-sizing: border-box;
  padding: 24px;
}
</style>