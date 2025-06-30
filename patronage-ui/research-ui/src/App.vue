<!-- src/App.vue -->
<template>
  <div id="app">
    <!-- 顶部导航栏 - 采用Element Plus Menu组件 -->
    <el-menu
        :default-active="activeMenu"
        mode="horizontal"
        background-color="#2578e6"
        text-color="#fff"
        active-text-color="#ffd04b"
        @select="handleMenuSelect"
        class="main-menu"
    >
      <!-- 系统名称 -->
      <el-menu-item index="system" disabled class="system-name">
        <span>基金研究子系统</span>
      </el-menu-item>

      <!-- 导航菜单项 -->
      <el-menu-item index="/publicFunds">
        <i class="el-icon-menu"></i>
        <span>全部公募基金</span>
      </el-menu-item>

      <el-menu-item index="/fundCompanies">
        <i class="el-icon-office-building"></i>
        <span>基金公司</span>
      </el-menu-item>

      <el-menu-item index="/fundManagers">
        <i class="el-icon-user"></i>
        <span>基金经理</span>
      </el-menu-item>

      <el-menu-item index="/portfolio">
        <i class="el-icon-suitcase"></i>
        <span>我的组合</span>
      </el-menu-item>

      <!-- 管理员菜单 - 右侧显示 -->
      <el-submenu index="admin" class="admin-menu">
        <template #title>
          <i class="el-icon-s-custom"></i>
          <span>管理员</span>
        </template>
        <el-menu-item index="userManagement">用户管理</el-menu-item>
        <el-menu-item index="dataImport">数据导入</el-menu-item>
      </el-submenu>
    </el-menu>

    <!-- 页面内容区域 -->
    <div class="content-container">
      <router-view />
    </div>

    <!-- 底部版权信息 -->
    <footer class="app-footer">
      © 2023 基金研究子系统
    </footer>
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      activeMenu: this.$route.path || '/publicFunds'
    }
  },
  watch: {
    $route(to) {
      this.activeMenu = to.path
    }
  },
  methods: {
    handleMenuSelect(index) {
      if (index === '/publicFunds' ||
          index === '/fundCompanies' ||
          index === '/fundManagers' ||
          index === '/portfolio') {
        this.$router.push(index)
      }
    }
  }
}
</script>

<style scoped>
#app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", Arial, sans-serif;
}

/* 导航菜单样式 */
.main-menu {
  display: flex;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 0 20px;
  position: relative;
}

.system-name {
  font-size: 1.5rem;
  font-weight: bold;
  cursor: default !important;
}

.system-name span {
  color: #fff !important;
}

.el-menu-item,
.el-submenu__title {
  font-size: 1rem;
  transition: all 0.3s ease;
}

/* 管理员菜单靠右显示 */
.admin-menu {
  margin-left: auto;
}

/* 内容区域样式 */
.content-container {
  flex: 1;
  padding: 20px;
  background-color: #f5f7fa;
}

/* 页脚样式 */
.app-footer {
  background-color: #eef1f5;
  color: #909399;
  text-align: center;
  padding: 15px 0;
  border-top: 1px solid #dcdfe6;
}
</style>