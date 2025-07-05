<template>
  <div class="error-404">
    <div class="error-container">
      <div class="error-content">
        <div class="error-icon">
          <el-icon><Warning /></el-icon>
        </div>
        <h1 class="error-code">404</h1>
        <h2 class="error-title">页面未找到</h2>
        <p class="error-description">
          抱歉，您访问的页面不存在或已被移除。
        </p>
        <div class="error-actions">
          <el-button type="primary" @click="goHome">返回首页</el-button>
          <el-button @click="goBack">返回上一页</el-button>
        </div>
        <div class="error-suggestions">
          <h3>您可以尝试：</h3>
          <ul>
            <li>检查URL是否正确</li>
            <li>使用导航菜单访问其他页面</li>
            <li>联系管理员获取帮助</li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useRouter } from 'vue-router'
import { Warning } from '@element-plus/icons-vue'

export default {
  name: 'Error404',
  setup() {
    const router = useRouter()

    const goHome = () => {
      router.push('/dashboard')
    }

    const goBack = () => {
      router.back()
    }

    return {
      goHome,
      goBack
    }
  }
}
</script>

<style scoped>
.error-404 {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.error-container {
  text-align: center;
  color: white;
  max-width: 600px;
  padding: 40px;
}

.error-icon {
  font-size: 80px;
  margin-bottom: 20px;
  opacity: 0.8;
}

.error-code {
  font-size: 120px;
  font-weight: bold;
  margin: 0;
  line-height: 1;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}

.error-title {
  font-size: 32px;
  margin: 20px 0;
  font-weight: 500;
}

.error-description {
  font-size: 18px;
  margin: 20px 0 40px 0;
  opacity: 0.9;
  line-height: 1.6;
}

.error-actions {
  margin: 40px 0;
}

.error-actions .el-button {
  margin: 0 10px;
  padding: 12px 24px;
  font-size: 16px;
}

.error-suggestions {
  margin-top: 40px;
  text-align: left;
  background: rgba(255, 255, 255, 0.1);
  padding: 20px;
  border-radius: 8px;
  backdrop-filter: blur(10px);
}

.error-suggestions h3 {
  margin: 0 0 15px 0;
  font-size: 18px;
  font-weight: 500;
}

.error-suggestions ul {
  margin: 0;
  padding-left: 20px;
  list-style-type: none;
}

.error-suggestions li {
  margin: 8px 0;
  position: relative;
  padding-left: 20px;
}

.error-suggestions li::before {
  content: '•';
  position: absolute;
  left: 0;
  color: #52c41a;
  font-weight: bold;
}
</style> 