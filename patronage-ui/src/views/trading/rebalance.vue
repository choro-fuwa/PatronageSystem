<template>
  <div class="rebalance-page">
    <el-card>
      <h2>手动再平衡</h2>
      <el-form :model="form" label-width="120px" style="max-width: 500px; margin: 0 auto;">
        <el-form-item label="股票代码">
          <el-input v-model="form.symbol" placeholder="如 600519" />
        </el-form-item>
        <el-form-item label="目标占比(%)">
          <el-input-number v-model="form.targetWeight" :min="0" :max="100" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRebalance">调整</el-button>
        </el-form-item>
      </el-form>
      <el-divider />
      <el-table :data="positions" style="width: 100%">
        <el-table-column prop="symbol" label="股票代码" width="120" />
        <el-table-column prop="name" label="股票名称" width="180" />
        <el-table-column prop="weight" label="当前占比(%)" width="120" />
        <el-table-column prop="targetWeight" label="目标占比(%)" width="120" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const form = ref({ symbol: '', targetWeight: null })
const positions = ref([
  { symbol: '600519', name: '贵州茅台', weight: 23.5, targetWeight: 23.5 },
  { symbol: '000858', name: '五粮液', weight: 7.8, targetWeight: 7.8 },
  { symbol: '600036', name: '招商银行', weight: 15.6, targetWeight: 15.6 }
])

function handleRebalance() {
  const idx = positions.value.findIndex(p => p.symbol === form.value.symbol)
  if (idx === -1) {
    ElMessage.error('未找到该股票代码')
    return
  }
  positions.value[idx].targetWeight = form.value.targetWeight
  ElMessage.success('目标占比已调整')
}
</script>

<style scoped>
.rebalance-page {
  padding: 32px;
}
</style> 