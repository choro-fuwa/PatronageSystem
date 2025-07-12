<template>
  <div class="backtest-view">
    <div class="page-header">
      <h2>基金回测</h2>
    </div>

    <!-- 参数设置表单 -->
    <el-card class="parameter-form">
      <template #header>
        <div class="card-header">
          <span>回测参数设置</span>
        </div>
      </template>

      <el-form :model="backtestForm" label-width="120px">
        <el-form-item label="初始资金">
          <el-input-number
            v-model="backtestForm.initial_capital"
            :min="1000"
            :step="1000"
            :precision="0"
          />
        </el-form-item>

        <el-form-item label="再平衡比例">
          <el-input-number
            v-model="backtestForm.rebalance_rate"
            :min="0"
            :max="1"
            :step="0.01"
            :precision="2"
          />
        </el-form-item>

        <el-form-item label="回测时间范围">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            @change="handleDateRangeChange"
          />
        </el-form-item>

        <!-- 基金列表 -->
        <el-form-item label="基金配置">
          <div class="fund-list">
            <div v-for="(fund, index) in backtestForm.funds_list" :key="index" class="fund-item">
              <el-input v-model="fund.code" placeholder="基金代码" style="width: 120px" />
              <el-input v-model="fund.name" placeholder="基金名称" style="width: 200px" />
              <el-input-number
                v-model="fund.rate"
                :min="0"
                :max="1"
                :step="0.01"
                :precision="2"
                placeholder="配置比例"
              />
              <el-input-number
                v-model="fund.purchaseFee"
                :min="0"
                :max="0.02"
                :step="0.001"
                :precision="3"
                placeholder="申购费率"
              />
              <el-input-number
                v-model="fund.sellFee"
                :min="0"
                :max="0.02"
                :step="0.001"
                :precision="3"
                placeholder="赎回费率"
              />
              <el-button type="danger" @click="removeFund(index)">删除</el-button>
            </div>
            <el-button type="primary" @click="addFund">添加基金</el-button>
          </div>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="runBacktest" :loading="loading">开始回测</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 回测结果 -->
    <el-card v-if="backtestResult" class="result-section">
      <template #header>
        <div class="card-header">
          <span>回测结果</span>
        </div>
      </template>

      <div class="charts-section">
        <h3>净值走势图</h3>
        <img src="/doc/fund_performance.png" class="chart-image" alt="净值走势图" />
      </div>

      <div class="funds-detail">
        <h3>基金明细</h3>
        <el-table :data="csvTableData" border style="width: 100%">
          <el-table-column prop="基金名字" label="基金名称" />
          <el-table-column prop="最早数据时间" label="开始日期" width="100" />
          <el-table-column prop="最晚数据时间" label="结束日期" width="100" />
          <el-table-column label="总投入资金" width="120">
            <template #default="{row}">
              {{ formatMoney(parseFloat(row['总投入资金']) || 0) }}
            </template>
          </el-table-column>
          <el-table-column label="账户资金" width="120">
            <template #default="{row}">
              {{ formatMoney(parseFloat(row['账户资金']) || 0) }}
            </template>
          </el-table-column>
          <el-table-column label="盈亏资金" width="120">
            <template #default="{row}">
              {{ formatMoney(parseFloat(row['盈亏资金']) || 0) }}
            </template>
          </el-table-column>
          <el-table-column label="盈亏率" width="100">
            <template #default="{row}">
              {{ formatPercent(parseFloat(row['盈亏率']) || 0) }}
            </template>
          </el-table-column>
          <el-table-column label="最大回撤" width="100">
            <template #default="{row}">
              {{ formatPercent(parseFloat(row['最大回撤']) || 0) }}
            </template>
          </el-table-column>
          <el-table-column label="买入手续费合计" width="120">
            <template #default="{row}">
              {{ formatMoney(parseFloat(row['买入手续费合计']) || 0) }}
            </template>
          </el-table-column>
          <el-table-column label="卖出手续费合计" width="120">
            <template #default="{row}">
              {{ formatMoney(parseFloat(row['卖出手续费合计']) || 0) }}
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { backtestApi } from '@/api/backtest'
import { ElMessage } from 'element-plus'

// 回测表单数据
const backtestForm = reactive({
  initial_capital: 10000,
  rebalance_rate: 0.05,
  start_date: '',
  end_date: '',
  funds_list: []
})

// 日期选择器数据
const dateRange = ref([])

// 加载状态
const loading = ref(false)

// 回测结果
const backtestResult = ref(false) // 只做“是否展示”标记

// CSV表格数据
const csvTableData = ref([])

// 处理日期范围变化
const handleDateRangeChange = (dates) => {
  if (dates) {
    backtestForm.start_date = dates[0]
    backtestForm.end_date = dates[1]
  } else {
    backtestForm.start_date = ''
    backtestForm.end_date = ''
  }
}

// 添加基金
const addFund = () => {
  backtestForm.funds_list.push({
    code: '',
    name: '',
    rate: 0,
    purchaseFee: 0.015,
    sellFee: 0.005
  })
}

// 删除基金
const removeFund = (index) => {
  backtestForm.funds_list.splice(index, 1)
}

// 读取CSV文件并解析
const loadCsvData = async () => {
  try {
    const response = await fetch('/doc/result.csv')
    if (!response.ok) throw new Error('无法读取CSV文件')
    const csvText = await response.text()
    csvTableData.value = parseCsvData(csvText)
  } catch (error) {
    csvTableData.value = []
    ElMessage.error('读取CSV文件失败: ' + error.message)
  }
}

// 解析CSV文本为对象数组
const parseCsvData = (csvText) => {
  const lines = csvText.split('\n').filter(line => line.trim())
  if (lines.length === 0) return []
  const headers = lines[0].split(',').map(h => h.trim())
  return lines.slice(1).map(line => {
    const values = line.split(',')
    const row = {}
    headers.forEach((h, i) => row[h] = values[i])
    return row
  })
}

// 执行回测
const runBacktest = async () => {
  // 验证表单
  if (!backtestForm.start_date || !backtestForm.end_date) {
    ElMessage.warning('请选择回测时间范围')
    return
  }
  if (backtestForm.funds_list.length === 0) {
    ElMessage.warning('请至少添加一个基金')
    return
  }

  // 验证基金配置
  for (const fund of backtestForm.funds_list) {
    if (!fund.code || !fund.name || !fund.rate) {
      ElMessage.warning('请完善基金配置信息')
      return
    }
  }

  try {
    loading.value = true
    await backtestApi.runBacktest(backtestForm) // 只关心成功
    backtestResult.value = true
    await loadCsvData()
    ElMessage.success('回测完成')
  } catch (error) {
    ElMessage.error('回测失败：' + (error.response?.data?.message || error.message))
  } finally {
    loading.value = false
  }
}

// 重置表单
const resetForm = () => {
  backtestForm.initial_capital = 10000
  backtestForm.rebalance_rate = 0.05
  dateRange.value = []
  backtestForm.start_date = ''
  backtestForm.end_date = ''
  backtestForm.funds_list = []
  backtestResult.value = false
  csvTableData.value = []
}

// 格式化金额
const formatMoney = (value) => {
  return new Intl.NumberFormat('zh-CN', {
    style: 'currency',
    currency: 'CNY'
  }).format(value)
}

// 格式化百分比
const formatPercent = (value) => {
  return new Intl.NumberFormat('zh-CN', {
    style: 'percent',
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  }).format(value / 100)
}
</script>

<style scoped>
.backtest-view {
  background: #fff;
  border-radius: 4px;
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #303133;
}

.parameter-form {
  margin-bottom: 20px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.fund-list {
  margin-top: 10px;
}

.fund-item {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
  align-items: center;
}

.result-section {
  margin-top: 20px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.summary-section,
.charts-section,
.funds-detail {
  margin-bottom: 30px;
}

.chart-image {
  width: 100%;
  max-width: 1000px;
  margin: 20px 0;
}

h3 {
  margin: 20px 0;
  color: #303133;
  font-weight: 500;
}
</style>