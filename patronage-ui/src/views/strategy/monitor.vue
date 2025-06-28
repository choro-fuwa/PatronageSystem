<template>
  <div class="monitor-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="header-left">
          <h2>策略监控</h2>
        <p class="subtitle">实时监控策略运行状态和绩效表现</p>
      </div>
      <div class="header-right">
        <el-button @click="handleRefresh" :icon="Refresh">刷新</el-button>
        <el-button type="primary" @click="handleExport" :icon="Download">导出报告</el-button>
      </div>
        </div>

    <!-- 策略选择 -->
    <el-card class="strategy-selector">
      <el-form :model="strategyForm" inline>
        <el-form-item label="选择策略">
          <el-select v-model="strategyForm.strategyId" placeholder="请选择策略" @change="handleStrategyChange">
            <el-option
              v-for="strategy in strategyList"
              :key="strategy.id"
              :label="strategy.name"
              :value="strategy.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="strategyForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="handleDateChange"
          />
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 关键指标卡片 -->
    <div class="metrics-cards" v-if="currentStrategy">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="metric-card">
            <div class="metric-content">
              <div class="metric-icon total-value">
                <el-icon><Money /></el-icon>
              </div>
              <div class="metric-info">
                <div class="metric-label">总资产</div>
                <div class="metric-value">{{ formatCurrency(metrics.totalValue) }}</div>
                <div class="metric-change" :class="metrics.valueChange >= 0 ? 'positive' : 'negative'">
                  {{ metrics.valueChange >= 0 ? '+' : '' }}{{ metrics.valueChange }}%
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="metric-card">
            <div class="metric-content">
              <div class="metric-icon total-return">
                <el-icon><TrendCharts /></el-icon>
              </div>
              <div class="metric-info">
                <div class="metric-label">总收益率</div>
                <div class="metric-value">{{ metrics.totalReturn }}%</div>
                <div class="metric-change" :class="metrics.returnChange >= 0 ? 'positive' : 'negative'">
                  {{ metrics.returnChange >= 0 ? '+' : '' }}{{ metrics.returnChange }}%
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="metric-card">
            <div class="metric-content">
              <div class="metric-icon max-drawdown">
                <el-icon><Warning /></el-icon>
              </div>
              <div class="metric-info">
                <div class="metric-label">最大回撤</div>
                <div class="metric-value">{{ metrics.maxDrawdown }}%</div>
                <div class="metric-change">{{ metrics.drawdownPeriod }}天</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="metric-card">
            <div class="metric-content">
              <div class="metric-icon sharpe-ratio">
                <el-icon><DataAnalysis /></el-icon>
              </div>
              <div class="metric-info">
                <div class="metric-label">夏普比率</div>
                <div class="metric-value">{{ metrics.sharpeRatio }}</div>
                <div class="metric-change">{{ metrics.volatility }}%</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 图表区域 -->
    <div class="charts-section" v-if="currentStrategy">
      <el-row :gutter="20">
        <!-- 净值曲线 -->
        <el-col :span="16">
          <el-card class="chart-card">
            <template #header>
              <div class="chart-header">
                <span>净值曲线</span>
                <div class="chart-actions">
                  <el-radio-group v-model="navChartType" size="small">
                    <el-radio-button label="nav">净值</el-radio-button>
                    <el-radio-button label="return">收益率</el-radio-button>
                    <el-radio-button label="drawdown">回撤</el-radio-button>
                  </el-radio-group>
                </div>
              </div>
            </template>
            <div class="chart-container" ref="navChartRef"></div>
          </el-card>
        </el-col>
        
        <!-- 持仓分布 -->
        <el-col :span="8">
          <el-card class="chart-card">
            <template #header>
              <span>持仓分布</span>
            </template>
            <div class="chart-container" ref="positionChartRef"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 详细数据 -->
    <div class="detail-section" v-if="currentStrategy">
      <el-row :gutter="20">
        <!-- 持仓明细 -->
        <el-col :span="12">
          <el-card class="detail-card">
            <template #header>
              <span>持仓明细</span>
            </template>
            <el-table :data="positions" style="width: 100%" size="small">
              <el-table-column prop="assetName" label="资产名称" />
              <el-table-column prop="weight" label="权重" width="80">
                <template #default="{ row }">
                  {{ row.weight }}%
                </template>
              </el-table-column>
              <el-table-column prop="marketValue" label="市值" width="100">
                <template #default="{ row }">
                  {{ formatCurrency(row.marketValue) }}
                </template>
              </el-table-column>
              <el-table-column prop="unrealizedPnL" label="盈亏" width="100">
                <template #default="{ row }">
                  <span :class="row.unrealizedPnL >= 0 ? 'positive' : 'negative'">
                    {{ formatCurrency(row.unrealizedPnL) }}
                  </span>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
        
        <!-- 风险指标 -->
        <el-col :span="12">
          <el-card class="detail-card">
            <template #header>
              <span>风险指标</span>
            </template>
            <div class="risk-metrics">
              <div class="risk-item">
                <span class="risk-label">波动率</span>
                <span class="risk-value">{{ riskMetrics.volatility }}%</span>
              </div>
              <div class="risk-item">
                <span class="risk-label">VaR(95%)</span>
                <span class="risk-value">{{ riskMetrics.var95 }}%</span>
              </div>
              <div class="risk-item">
                <span class="risk-label">贝塔系数</span>
                <span class="risk-value">{{ riskMetrics.beta }}</span>
              </div>
              <div class="risk-item">
                <span class="risk-label">阿尔法系数</span>
                <span class="risk-value">{{ riskMetrics.alpha }}%</span>
              </div>
              <div class="risk-item">
                <span class="risk-label">信息比率</span>
                <span class="risk-value">{{ riskMetrics.informationRatio }}</span>
              </div>
              <div class="risk-item">
                <span class="risk-label">跟踪误差</span>
                <span class="risk-value">{{ riskMetrics.trackingError }}%</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      </div>

    <!-- 预警信息 -->
    <el-card class="alert-section" v-if="alerts.length > 0">
      <template #header>
        <span>预警信息</span>
      </template>
      <el-alert
        v-for="alert in alerts"
        :key="alert.id"
        :title="alert.title"
        :description="alert.description"
        :type="alert.type"
        :closable="false"
        show-icon
        style="margin-bottom: 10px"
      />
    </el-card>

    <!-- 监控看板 -->
    <el-row :gutter="20" class="dashboard-row">
      <el-col :span="6">
        <el-card class="dashboard-card">
          <div class="dashboard-item">
            <div class="dashboard-icon">
              <el-icon size="24" color="#409EFF"><TrendCharts /></el-icon>
            </div>
            <div class="dashboard-content">
              <div class="dashboard-value">{{ dashboardData.totalStrategies || 0 }}</div>
              <div class="dashboard-label">总策略数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="dashboard-card">
          <div class="dashboard-item">
            <div class="dashboard-icon">
              <el-icon size="24" color="#67C23A"><VideoPlay /></el-icon>
            </div>
            <div class="dashboard-content">
              <div class="dashboard-value">{{ dashboardData.runningStrategies || 0 }}</div>
              <div class="dashboard-label">运行中策略</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="dashboard-card">
          <div class="dashboard-item">
            <div class="dashboard-icon">
              <el-icon size="24" color="#E6A23C"><Money /></el-icon>
            </div>
            <div class="dashboard-content">
              <div class="dashboard-value">{{ formatCurrency(dashboardData.totalValue) }}</div>
              <div class="dashboard-label">总资产</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="dashboard-card">
          <div class="dashboard-item">
            <div class="dashboard-icon">
              <el-icon size="24" color="#F56C6C"><Warning /></el-icon>
            </div>
            <div class="dashboard-content">
              <div class="dashboard-value">{{ dashboardData.alertCount || 0 }}</div>
              <div class="dashboard-label">预警数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 策略监控列表 -->
    <el-card class="monitor-card">
      <template #header>
        <div class="card-header">
          <span>策略监控</span>
          <el-button type="primary" size="small" @click="handleRefresh">刷新</el-button>
        </div>
      </template>
      
      <el-table
        v-loading="loading"
        :data="strategyList"
        style="width: 100%"
      >
        <el-table-column prop="strategyName" label="策略名称" min-width="150">
          <template #default="{ row }">
            <el-link type="primary" @click="handleViewStrategy(row)">{{ row.strategyName }}</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="strategyType" label="策略类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getStrategyTypeTag(row.strategyType)">
              {{ getStrategyTypeName(row.strategyType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.status)">
              {{ getStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="当前收益率" width="120">
          <template #default="{ row }">
            <span :class="getReturnClass(row.currentReturn)">
              {{ formatPercent(row.currentReturn) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="当前回撤" width="120">
          <template #default="{ row }">
            <span class="loss">{{ formatPercent(row.currentDrawdown) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="持仓数量" width="100">
          <template #default="{ row }">
            {{ row.positionCount || 0 }}
          </template>
        </el-table-column>
        <el-table-column label="总市值" width="120">
          <template #default="{ row }">
            {{ formatCurrency(row.totalValue) }}
          </template>
        </el-table-column>
        <el-table-column label="最后更新" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.lastUpdateTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button-group>
              <el-button size="small" @click="handleViewStrategy(row)">详情</el-button>
              <el-button size="small" type="warning" @click="handleSetAlert(row)">预警</el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 预警管理 -->
    <el-card class="alert-card">
      <template #header>
        <div class="card-header">
          <span>预警管理</span>
          <el-button type="primary" size="small" @click="handleCreateAlert">新建预警</el-button>
        </div>
      </template>
      
      <el-table
        v-loading="alertLoading"
        :data="alertList"
        style="width: 100%"
      >
        <el-table-column prop="alertName" label="预警名称" min-width="150" />
        <el-table-column prop="alertType" label="预警类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getAlertTypeTag(row.alertType)">
              {{ getAlertTypeName(row.alertType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="alertLevel" label="预警级别" width="100">
          <template #default="{ row }">
            <el-tag :type="getAlertLevelTag(row.alertLevel)">
              {{ getAlertLevelName(row.alertLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="threshold" label="阈值" width="100">
          <template #default="{ row }">
            {{ formatThreshold(row.alertType, row.threshold) }}
          </template>
        </el-table-column>
        <el-table-column prop="enabled" label="状态" width="80">
          <template #default="{ row }">
            <el-switch
              v-model="row.enabled"
              :active-value="1"
              :inactive-value="0"
              @change="handleToggleAlert(row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button-group>
              <el-button size="small" @click="handleEditAlert(row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDeleteAlert(row)">删除</el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 预警历史 -->
    <el-card class="history-card">
      <template #header>
        <div class="card-header">
          <span>预警历史</span>
          <el-button type="primary" size="small" @click="handleRefreshHistory">刷新</el-button>
        </div>
      </template>
      
      <el-table
        v-loading="historyLoading"
        :data="alertHistoryList"
        style="width: 100%"
      >
        <el-table-column prop="alertName" label="预警名称" min-width="150" />
        <el-table-column prop="alertType" label="预警类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getAlertTypeTag(row.alertType)">
              {{ getAlertTypeName(row.alertType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="alertLevel" label="预警级别" width="100">
          <template #default="{ row }">
            <el-tag :type="getAlertLevelTag(row.alertLevel)">
              {{ getAlertLevelName(row.alertLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="triggerValue" label="触发值" width="100">
          <template #default="{ row }">
            {{ formatThreshold(row.alertType, row.triggerValue) }}
          </template>
        </el-table-column>
        <el-table-column prop="processStatus" label="处理状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getProcessStatusTag(row.processStatus)">
              {{ getProcessStatusName(row.processStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="触发时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.processStatus === 0"
              size="small"
              type="primary"
              @click="handleProcessAlert(row)"
            >
              处理
            </el-button>
            <el-button size="small" @click="handleViewHistory(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 预警设置对话框 -->
    <el-dialog
      v-model="alertDialogVisible"
      :title="alertDialogTitle"
      width="500px"
    >
      <el-form
        ref="alertFormRef"
        :model="alertForm"
        :rules="alertRules"
        label-width="100px"
      >
        <el-form-item label="预警名称" prop="alertName">
          <el-input v-model="alertForm.alertName" placeholder="请输入预警名称" />
        </el-form-item>
        <el-form-item label="预警类型" prop="alertType">
          <el-select v-model="alertForm.alertType" placeholder="请选择预警类型" style="width: 100%">
            <el-option label="收益率预警" :value="1" />
            <el-option label="回撤预警" :value="2" />
            <el-option label="持仓预警" :value="3" />
            <el-option label="资金预警" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="预警级别" prop="alertLevel">
          <el-select v-model="alertForm.alertLevel" placeholder="请选择预警级别" style="width: 100%">
            <el-option label="低" :value="1" />
            <el-option label="中" :value="2" />
            <el-option label="高" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="预警阈值" prop="threshold">
          <el-input-number
            v-model="alertForm.threshold"
            :precision="4"
            style="width: 100%"
            placeholder="请输入预警阈值"
          />
        </el-form-item>
        <el-form-item label="通知方式" prop="notificationType">
          <el-select v-model="alertForm.notificationType" placeholder="请选择通知方式" style="width: 100%">
            <el-option label="邮件" :value="1" />
            <el-option label="短信" :value="2" />
            <el-option label="WebSocket" :value="3" />
            <el-option label="全部" :value="4" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="alertDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmitAlert">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Download, Money, TrendCharts, Warning, DataAnalysis, VideoPlay } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { getStrategyMonitorData, getStrategyRiskMetrics, strategyApi, monitorApi } from '@/api/strategy'
import dayjs from 'dayjs'

const route = useRoute()
const navChartRef = ref(null)
const positionChartRef = ref(null)
let navChart = null
let positionChart = null

// 策略选择表单
const strategyForm = reactive({
  strategyId: '',
  dateRange: []
})

// 策略列表
const strategyList = ref([])
const currentStrategy = ref(null)

// 监控指标
const metrics = reactive({
  totalValue: 0,
  valueChange: 0,
  totalReturn: 0,
  returnChange: 0,
  maxDrawdown: 0,
  drawdownPeriod: 0,
  sharpeRatio: 0,
  volatility: 0
})

// 风险指标
const riskMetrics = reactive({
  volatility: 0,
  var95: 0,
  beta: 0,
  alpha: 0,
  informationRatio: 0,
  trackingError: 0
})

// 持仓数据
const positions = ref([])

// 预警信息
const alerts = ref([])

// 图表类型
const navChartType = ref('nav')

// 响应式数据
const loading = ref(false)
const alertLoading = ref(false)
const historyLoading = ref(false)
const dashboardData = ref({})
const alertDialogVisible = ref(false)
const alertDialogTitle = ref('')
const alertFormRef = ref()

// 预警表单
const alertForm = reactive({
  id: null,
  strategyId: null,
  alertName: '',
  alertType: null,
  alertLevel: null,
  threshold: null,
  notificationType: null
})

// 预警表单验证规则
const alertRules = {
  alertName: [
    { required: true, message: '请输入预警名称', trigger: 'blur' }
  ],
  alertType: [
    { required: true, message: '请选择预警类型', trigger: 'change' }
  ],
  alertLevel: [
    { required: true, message: '请选择预警级别', trigger: 'change' }
  ],
  threshold: [
    { required: true, message: '请输入预警阈值', trigger: 'blur' }
  ],
  notificationType: [
    { required: true, message: '请选择通知方式', trigger: 'change' }
  ]
}

// 获取策略监控数据
const fetchMonitorData = async () => {
  if (!strategyForm.strategyId) return
  
  try {
    const response = await getStrategyMonitorData(strategyForm.strategyId)
    const data = response.data
    
    // 更新指标数据
    Object.assign(metrics, data.metrics)
    Object.assign(riskMetrics, data.riskMetrics)
    positions.value = data.positions
    alerts.value = data.alerts
    
    // 更新图表
    nextTick(() => {
      updateCharts(data)
    })
  } catch (error) {
    ElMessage.error('获取监控数据失败')
  }
}

// 更新图表
const updateCharts = (data) => {
  updateNavChart(data.navData)
  updatePositionChart(data.positionData)
}

// 更新净值图表
const updateNavChart = (navData) => {
  if (!navChartRef.value) return
  
  if (!navChart) {
    navChart = echarts.init(navChartRef.value)
  }
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross'
      }
    },
    legend: {
      data: ['策略净值', '基准净值']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: navData.dates
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '策略净值',
        type: 'line',
        data: navData.strategyNav,
        smooth: true,
        lineStyle: {
          color: '#1890ff'
        }
      },
      {
        name: '基准净值',
        type: 'line',
        data: navData.benchmarkNav,
        smooth: true,
        lineStyle: {
          color: '#d9d9d9'
        }
      }
    ]
  }
  
  navChart.setOption(option)
}

// 更新持仓图表
const updatePositionChart = (positionData) => {
  if (!positionChartRef.value) return
  
  if (!positionChart) {
    positionChart = echarts.init(positionChartRef.value)
  }
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    series: [
      {
        name: '持仓分布',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '18',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: positionData.map(item => ({
          name: item.assetName,
          value: item.weight
        }))
      }
    ]
  }
  
  positionChart.setOption(option)
}

// 策略变化处理
const handleStrategyChange = () => {
  fetchMonitorData()
}

// 日期变化处理
const handleDateChange = () => {
  fetchMonitorData()
}

// 刷新数据
const handleRefresh = () => {
  fetchMonitorData()
  getDashboardData()
  getStrategyList()
}

// 导出报告
const handleExport = () => {
  ElMessage.info('导出功能开发中')
}

// 获取监控看板数据
const getDashboardData = async () => {
  try {
    const res = await monitorApi.getMonitorDashboardData()
    if (res.success) {
      dashboardData.value = res.data
    }
  } catch (error) {
    console.error('获取监控看板数据失败:', error)
  }
}

// 获取策略列表
const getStrategyList = async () => {
  loading.value = true
  try {
    const res = await strategyApi.getStrategyList({ pageNum: 1, pageSize: 100 })
    if (res.success) {
      strategyList.value = res.data.records
      // 获取每个策略的监控数据
      for (const strategy of strategyList.value) {
        try {
          const monitorRes = await monitorApi.getStrategyMonitorData(strategy.id)
          if (monitorRes.success) {
            Object.assign(strategy, monitorRes.data)
          }
        } catch (error) {
          console.error(`获取策略${strategy.id}监控数据失败:`, error)
        }
      }
    }
  } catch (error) {
    console.error('获取策略列表失败:', error)
    ElMessage.error('获取策略列表失败')
  } finally {
    loading.value = false
  }
}

// 获取预警列表
const getAlertList = async () => {
  alertLoading.value = true
  try {
    const res = await monitorApi.getAlertList({ pageNum: 1, pageSize: 100 })
    if (res.success) {
      alertList.value = res.data.records
    }
  } catch (error) {
    console.error('获取预警列表失败:', error)
    ElMessage.error('获取预警列表失败')
  } finally {
    alertLoading.value = false
  }
}

// 获取预警历史
const getAlertHistory = async () => {
  historyLoading.value = true
  try {
    const res = await monitorApi.getAlertHistoryList({ pageNum: 1, pageSize: 100 })
    if (res.success) {
      alertHistoryList.value = res.data.records
    }
  } catch (error) {
    console.error('获取预警历史失败:', error)
    ElMessage.error('获取预警历史失败')
  } finally {
    historyLoading.value = false
  }
}

// 查看策略
const handleViewStrategy = (row) => {
  // TODO: 跳转到策略详情页
  console.log('查看策略:', row)
}

// 设置预警
const handleSetAlert = (row) => {
  alertDialogTitle.value = '设置预警'
  alertDialogVisible.value = true
  alertForm.strategyId = row.id
  resetAlertForm()
}

// 创建预警
const handleCreateAlert = () => {
  alertDialogTitle.value = '新建预警'
  alertDialogVisible.value = true
  resetAlertForm()
}

// 编辑预警
const handleEditAlert = (row) => {
  alertDialogTitle.value = '编辑预警'
  alertDialogVisible.value = true
  Object.assign(alertForm, row)
}

// 删除预警
const handleDeleteAlert = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该预警规则吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await monitorApi.deleteAlert(row.id)
    if (res.success) {
      ElMessage.success('预警规则删除成功')
      getAlertList()
    } else {
      ElMessage.error(res.message || '预警规则删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除预警规则失败:', error)
      ElMessage.error('预警规则删除失败')
    }
  }
}

// 启用/禁用预警
const handleToggleAlert = async (row) => {
  try {
    const res = await monitorApi.toggleAlert(row.id, row.enabled === 1)
    if (!res.success) {
      ElMessage.error(res.message || '操作失败')
      // 恢复原状态
      row.enabled = row.enabled === 1 ? 0 : 1
    }
  } catch (error) {
    console.error('切换预警状态失败:', error)
    ElMessage.error('操作失败')
    // 恢复原状态
    row.enabled = row.enabled === 1 ? 0 : 1
  }
}

// 处理预警
const handleProcessAlert = async (row) => {
  try {
    const res = await monitorApi.processAlert(row.id, 'admin', '已处理')
    if (res.success) {
      ElMessage.success('预警处理成功')
      getAlertHistory()
    } else {
      ElMessage.error(res.message || '预警处理失败')
    }
  } catch (error) {
    console.error('处理预警失败:', error)
    ElMessage.error('预警处理失败')
  }
}

// 查看预警历史详情
const handleViewHistory = (row) => {
  // TODO: 显示预警历史详情
  console.log('查看预警历史:', row)
}

// 提交预警表单
const handleSubmitAlert = async () => {
  try {
    await alertFormRef.value.validate()
    
    const api = alertForm.id ? monitorApi.updateAlert : monitorApi.createAlert
    const res = await api(alertForm)
    
    if (res.success) {
      ElMessage.success(alertForm.id ? '预警规则更新成功' : '预警规则创建成功')
      alertDialogVisible.value = false
      getAlertList()
    } else {
      ElMessage.error(res.message || (alertForm.id ? '预警规则更新失败' : '预警规则创建失败'))
    }
  } catch (error) {
    console.error('提交预警表单失败:', error)
    ElMessage.error('提交失败')
  }
}

// 重置预警表单
const resetAlertForm = () => {
  Object.assign(alertForm, {
    id: null,
    strategyId: null,
    alertName: '',
    alertType: null,
    alertLevel: null,
    threshold: null,
    notificationType: null
  })
  alertFormRef.value?.resetFields()
}

// 工具函数
const getStrategyTypeName = (type) => {
  const typeMap = {
    1: '趋势策略',
    2: '均值回归',
    3: '套利策略',
    4: '量化策略'
  }
  return typeMap[type] || '未知'
}

const getStrategyTypeTag = (type) => {
  const tagMap = {
    1: 'primary',
    2: 'success',
    3: 'warning',
    4: 'info'
  }
  return tagMap[type] || ''
}

const getStatusName = (status) => {
  const statusMap = {
    0: '草稿',
    1: '运行中',
    2: '暂停',
    3: '已停止'
  }
  return statusMap[status] || '未知'
}

const getStatusTag = (status) => {
  const tagMap = {
    0: 'info',
    1: 'success',
    2: 'warning',
    3: 'danger'
  }
  return tagMap[status] || ''
}

const getAlertTypeName = (type) => {
  const typeMap = {
    1: '收益率预警',
    2: '回撤预警',
    3: '持仓预警',
    4: '资金预警'
  }
  return typeMap[type] || '未知'
}

const getAlertTypeTag = (type) => {
  const tagMap = {
    1: 'success',
    2: 'danger',
    3: 'warning',
    4: 'info'
  }
  return tagMap[type] || ''
}

const getAlertLevelName = (level) => {
  const levelMap = {
    1: '低',
    2: '中',
    3: '高'
  }
  return levelMap[level] || '未知'
}

const getAlertLevelTag = (level) => {
  const tagMap = {
    1: 'info',
    2: 'warning',
    3: 'danger'
  }
  return tagMap[level] || ''
}

const getProcessStatusName = (status) => {
  const statusMap = {
    0: '未处理',
    1: '已处理',
    2: '已忽略'
  }
  return statusMap[status] || '未知'
}

const getProcessStatusTag = (status) => {
  const tagMap = {
    0: 'warning',
    1: 'success',
    2: 'info'
  }
  return tagMap[status] || ''
}

const formatCurrency = (value) => {
  if (!value) return '¥0.00'
  return new Intl.NumberFormat('zh-CN', {
    style: 'currency',
    currency: 'CNY'
  }).format(value)
}

const formatPercent = (value) => {
  if (!value) return '0.00%'
  return `${(value * 100).toFixed(2)}%`
}

const formatThreshold = (type, value) => {
  if (!value) return '0.00'
  if (type === 1 || type === 2) {
    return `${(value * 100).toFixed(2)}%`
  }
  return value.toString()
}

const formatDateTime = (value) => {
  if (!value) return '-'
  return dayjs(value).format('YYYY-MM-DD HH:mm:ss')
}

const getReturnClass = (value) => {
  if (!value) return ''
  return value >= 0 ? 'profit' : 'loss'
}

onMounted(() => {
  // 初始化策略列表
  strategyList.value = [
    { id: 1, name: '稳健股债轮动-2023' },
    { id: 2, name: 'FOF组合管理策略' },
    { id: 3, name: '基金指数组合策略' }
  ]
  
  // 如果有路由参数，设置默认策略
  if (route.params.id) {
    strategyForm.strategyId = parseInt(route.params.id)
    fetchMonitorData()
  }
  
  // 初始化预警列表
  alertList.value = [
    { id: 1, name: '收益率预警规则', type: '收益率预警', level: '中', threshold: 0.05, enabled: 1 },
    { id: 2, name: '回撤预警规则', type: '回撤预警', level: '高', threshold: 0.10, enabled: 1 },
    { id: 3, name: '持仓预警规则', type: '持仓预警', level: '高', threshold: 0.20, enabled: 1 },
    { id: 4, name: '资金预警规则', type: '资金预警', level: '高', threshold: 1000000, enabled: 1 }
  ]
  
  // 初始化预警历史
  alertHistoryList.value = [
    { id: 1, name: '收益率预警规则', type: '收益率预警', level: '中', triggerValue: 0.05, processStatus: 1 },
    { id: 2, name: '回撤预警规则', type: '回撤预警', level: '高', triggerValue: 0.10, processStatus: 1 },
    { id: 3, name: '持仓预警规则', type: '持仓预警', level: '高', triggerValue: 0.20, processStatus: 1 },
    { id: 4, name: '资金预警规则', type: '资金预警', level: '高', triggerValue: 1000000, processStatus: 1 }
  ]
  
  // 初始化监控看板数据
  dashboardData.value = {
    totalStrategies: 4,
    runningStrategies: 3,
    totalValue: 1000000,
    alertCount: 4
  }
})
</script>

<style scoped lang="scss">
.monitor-container {
  .page-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    margin-bottom: 24px;
    
    .header-left {
      h2 {
        margin: 0 0 8px 0;
        font-size: 24px;
        font-weight: 600;
        color: #1f2937;
      }
      
      .subtitle {
        margin: 0;
        color: #6b7280;
        font-size: 14px;
      }
    }
  }
  
  .strategy-selector {
    margin-bottom: 24px;
  }
  
  .metrics-cards {
    margin-bottom: 24px;

  .metric-card {
      .metric-content {
        display: flex;
        align-items: center;
        
        .metric-icon {
          width: 48px;
          height: 48px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          
          &.total-value {
            background-color: #e6f7ff;
            color: #1890ff;
          }
          
          &.total-return {
            background-color: #f6ffed;
            color: #52c41a;
          }
          
          &.max-drawdown {
            background-color: #fff2f0;
            color: #ff4d4f;
          }
          
          &.sharpe-ratio {
            background-color: #fff7e6;
            color: #fa8c16;
          }
        }
        
        .metric-info {
          flex: 1;
          
          .metric-label {
            font-size: 14px;
            color: #6b7280;
            margin-bottom: 4px;
          }
    
    .metric-value {
      font-size: 24px;
            font-weight: 600;
            color: #1f2937;
            line-height: 1;
          }
          
          .metric-change {
            font-size: 12px;
            margin-top: 4px;
      
      &.positive {
              color: #52c41a;
      }
      
      &.negative {
              color: #ff4d4f;
            }
          }
        }
      }
    }
  }
  
  .charts-section {
    margin-bottom: 24px;
    
    .chart-card {
      .chart-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
      
      .chart-container {
      height: 400px;
    }
    }
  }
  
  .detail-section {
    margin-bottom: 24px;
    
    .detail-card {
      .risk-metrics {
        .risk-item {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding: 12px 0;
          border-bottom: 1px solid #f0f0f0;
          
          &:last-child {
            border-bottom: none;
          }
          
          .risk-label {
            color: #6b7280;
            font-size: 14px;
          }
          
          .risk-value {
            font-weight: 500;
            color: #1f2937;
          }
        }
      }
    }
  }
  
  .alert-section {
    margin-bottom: 24px;
  }
  
  .positive {
    color: #52c41a;
  }
  
  .negative {
    color: #ff4d4f;
  }

  .dashboard-row {
    margin-bottom: 20px;
  }

  .dashboard-card {
    .dashboard-item {
      display: flex;
      align-items: center;
      gap: 16px;
    }

    .dashboard-icon {
      width: 48px;
      height: 48px;
      border-radius: 8px;
      background: #f0f9ff;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .dashboard-content {
      flex: 1;
    }

    .dashboard-value {
      font-size: 24px;
      font-weight: 600;
      color: #1f2937;
      line-height: 1;
    }

    .dashboard-label {
      font-size: 14px;
      color: #6b7280;
      margin-top: 4px;
    }
  }

  .monitor-card,
  .alert-card,
  .history-card {
    margin-bottom: 20px;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .profit {
    color: #67c23a;
    font-weight: 500;
  }

  .loss {
    color: #f56c6c;
    font-weight: 500;
  }

  .dialog-footer {
    text-align: right;
  }
}
</style>