<template>
  <div class="performance-page">
    <el-card>
      <h2>基础绩效报告</h2>
      <el-row :gutter="24">
        <el-col :span="8">
          <div class="stat-block">
            <div class="stat-label">累计收益率</div>
            <div class="stat-value">{{ stats.totalReturn }}%</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="stat-block">
            <div class="stat-label">总盈亏</div>
            <div class="stat-value">{{ stats.totalProfit }}</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="stat-block">
            <div class="stat-label">最大回撤</div>
            <div class="stat-value">{{ stats.maxDrawdown }}%</div>
          </div>
        </el-col>
      </el-row>
      <el-divider />
      <h3>持仓分布</h3>
      <div ref="pieChart" style="width: 400px; height: 300px; margin: 0 auto;"></div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'

const stats = ref({
  totalReturn: 15.8,
  totalProfit: 36000,
  maxDrawdown: -8.2
})

const positions = ref([
  { name: '贵州茅台', value: 180000 },
  { name: '五粮液', value: 60000 },
  { name: '招商银行', value: 120000 }
])

const pieChart = ref(null)

onMounted(() => {
  const chart = echarts.init(pieChart.value)
  chart.setOption({
    title: { text: '持仓分布', left: 'center' },
    tooltip: { trigger: 'item' },
    legend: { bottom: 0 },
    series: [
      {
        name: '持仓市值',
        type: 'pie',
        radius: '60%',
        data: positions.value,
        label: { formatter: '{b}: {d}%' }
      }
    ]
  })
})
</script>

<style scoped>
.performance-page {
  padding: 32px;
}
.stat-block {
  background: #f5f7fa;
  border-radius: 8px;
  padding: 32px 0;
  text-align: center;
  margin-bottom: 24px;
}
.stat-label {
  font-size: 18px;
  color: #888;
  margin-bottom: 8px;
}
.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #409eff;
}
</style> 