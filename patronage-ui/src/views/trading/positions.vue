<template>
  <div class="positions-page">
    <el-card>
      <h2>当前持仓</h2>
      <el-table :data="positions" style="width: 100%">
        <el-table-column prop="symbol" label="股票代码" width="120" />
        <el-table-column prop="name" label="股票名称" width="180" />
        <el-table-column prop="quantity" label="持仓数量" width="120" />
        <el-table-column prop="marketValue" label="市值" width="120" />
        <el-table-column prop="weight" label="占比(%)" width="120" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const positions = ref([])

// 模拟数据，实际可从后端接口获取
onMounted(() => {
  positions.value = [
    { symbol: '600519', name: '贵州茅台', quantity: 100, marketValue: 180000, weight: 23.5 },
    { symbol: '000858', name: '五粮液', quantity: 200, marketValue: 60000, weight: 7.8 },
    { symbol: '600036', name: '招商银行', quantity: 300, marketValue: 120000, weight: 15.6 }
  ]
})
</script>

<style scoped>
.positions-page {
  padding: 32px;
}
</style> 