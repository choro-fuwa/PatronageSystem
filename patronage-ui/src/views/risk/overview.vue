<template>
  <div class="risk-overview">
    <el-card class="risk-card">
      <h2>风险概览</h2>
      <el-row :gutter="24">
        <el-col :span="12">
          <div class="risk-metric">
            <div class="risk-label">最大回撤</div>
            <div class="risk-value">{{ maxDrawdown }}%</div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="risk-metric">
            <div class="risk-label">单只股票最大占比</div>
            <div class="risk-value">{{ maxStockWeight }}%</div>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const maxDrawdown = ref(0)
const maxStockWeight = ref(0)

// 模拟数据，实际可从后端接口获取
onMounted(() => {
  maxDrawdown.value = -8.2
  maxStockWeight.value = 23.5
})
</script>

<style scoped>
.risk-overview {
  padding: 32px;
}
.risk-card {
  max-width: 600px;
  margin: 0 auto;
}
.risk-metric {
  background: #f5f7fa;
  border-radius: 8px;
  padding: 32px 0;
  text-align: center;
  margin-bottom: 24px;
}
.risk-label {
  font-size: 18px;
  color: #888;
  margin-bottom: 8px;
}
.risk-value {
  font-size: 32px;
  font-weight: bold;
  color: #409eff;
}
</style> 