<template>
  <div class="strategy-detail-container">
    <el-card>
      <template #header>
        <div class="header-title">策略详情</div>
      </template>
      <el-descriptions :title="strategy.strategyName" :column="2" border>
        <el-descriptions-item label="策略类型">{{ getStrategyTypeName(strategy.strategyType) }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ getStatusName(strategy.status) }}</el-descriptions-item>
        <el-descriptions-item label="初始资金">{{ formatCurrency(strategy.initialCapital) }}</el-descriptions-item>
        <el-descriptions-item label="当前资金">{{ formatCurrency(strategy.currentCapital) }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatDateTime(strategy.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ formatDateTime(strategy.updateTime) }}</el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ strategy.description }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
    <!-- 这里可继续补充持仓、回测、预警等Tab页 -->
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { strategyApi } from '@/api/strategy'
import dayjs from 'dayjs'

const route = useRoute()
const strategy = ref({})

const getStrategyDetail = async () => {
  const res = await strategyApi.getStrategyDetail(route.params.id)
  if (res.success) {
    strategy.value = res.data
  }
}

const getStrategyTypeName = (type) => {
  const typeMap = { 1: '趋势策略', 2: '均值回归', 3: '套利策略', 4: '量化策略' }
  return typeMap[type] || '未知'
}
const getStatusName = (status) => {
  const statusMap = { 0: '草稿', 1: '运行中', 2: '暂停', 3: '已停止' }
  return statusMap[status] || '未知'
}
const formatCurrency = (value) => {
  return new Intl.NumberFormat('zh-CN', { style: 'currency', currency: 'CNY' }).format(value)
}
const formatDateTime = (value) => {
  return dayjs(value).format('YYYY-MM-DD HH:mm:ss')
}
onMounted(() => {
  getStrategyDetail()
})
</script>
<style scoped>
.strategy-detail-container { padding: 20px; }
.header-title { font-size: 18px; font-weight: bold; }
</style> 