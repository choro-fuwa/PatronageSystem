<template>
  <div class="console-container">
    <h1 class="page-title">系统控制台</h1>
    
    <!-- 数据概览卡片 -->
    <div class="data-cards">
      <el-card class="card-item">
        <div class="card-title">基金总数</div>
        <div class="card-value">1,234 只</div>
      </el-card>
      <el-card class="card-item">
        <div class="card-title">活跃策略</div>
        <div class="card-value">24 个</div>
      </el-card>
      <el-card class="card-item">
        <div class="card-title">今日交易</div>
        <div class="card-value">1,286 笔</div>
      </el-card>
      <el-card class="card-item">
        <div class="card-title">用户数量</div>
        <div class="card-value">5,678 人</div>
      </el-card>
    </div>
    
    <!-- 引入新闻组件 -->
    <NewsBox />
  </div>
</template>

<script setup>
import NewsBox from '@/components/NewsBox.vue'
</script>

<style scoped>
.console-container {
  padding: 20px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 20px;
}

.data-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 30px;
}

.card-item {
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.card-title {
  font-size: 16px;
  color: #606266;
  margin-bottom: 8px;
}

.card-value {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}
</style>