<template>
  <div class="backtest-container">
    <el-card>
      <template #header>
        <div class="header-title">回测结果</div>
      </template>
      <div v-if="backtest">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="总收益率">{{ formatPercent(backtest.total_return) }}</el-descriptions-item>
          <el-descriptions-item label="年化收益率">{{ formatPercent(backtest.annual_return) }}</el-descriptions-item>
          <el-descriptions-item label="最大回撤">{{ formatPercent(backtest.max_drawdown) }}</el-descriptions-item>
          <el-descriptions-item label="夏普比率">{{ backtest.sharpe_ratio }}</el-descriptions-item>
          <el-descriptions-item label="胜率">{{ formatPercent(backtest.win_rate) }}</el-descriptions-item>
          <el-descriptions-item label="总交易次数">{{ backtest.total_trades }}</el-descriptions-item>
        </el-descriptions>
        <v-chart :option="chartOption" autoresize style="height: 400px; margin-top: 30px;" />
      </div>
    </el-card>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { backtestApi } from '@/api/strategy'
import { use } from 'echarts/core'
import VChart from 'vue-echarts'
import dayjs from 'dayjs'

const route = useRoute()
const backtest = ref(null)
const chartOption = ref({})

const getBacktestResult = async () => {
  const res = await backtestApi.getBacktestResultDetail(route.params.id)
  if (res.success) {
    backtest.value = res.data
    // 获取曲线
    const chartRes = await backtestApi.getBacktestChartData(route.params.id)
    if (chartRes.success) {
      chartOption.value = buildChartOption(chartRes.data)
    }
  }
}
const formatPercent = (val) => {
  if (!val) return '0.00%'
  return `${(val * 100).toFixed(2)}%`
}
const buildChartOption = (data) => {
  return {
    tooltip: { trigger: 'axis' },
    legend: { data: ['权益曲线', '回撤曲线'] },
    xAxis: { type: 'category', data: (data.equityCurve || []).map(i => i.date) },
    yAxis: { type: 'value' },
    series: [
      { name: '权益曲线', type: 'line', data: (data.equityCurve || []).map(i => i.equity) },
      { name: '回撤曲线', type: 'line', data: (data.drawdownCurve || []).map(i => i.value) }
    ]
  }
}
onMounted(() => { getBacktestResult() })
</script>
<style scoped>
.backtest-container { padding: 20px; }
.header-title { font-size: 18px; font-weight: bold; }
</style> 