<template>
  <div class="monitor-overview">
    <div class="page-header">
      <h1>监控概览</h1>
      <div class="header-actions">
        <el-button @click="refreshData">刷新数据</el-button>
        <el-button type="primary" @click="exportReport">导出报告</el-button>
      </div>
    </div>

    <div class="overview-content">
      <!-- 关键指标 -->
      <el-row :gutter="20" class="metrics-row">
        <el-col :span="6">
          <el-card class="metric-card">
            <div class="metric-content">
              <div class="metric-icon total">
                <el-icon><DataAnalysis /></el-icon>
              </div>
              <div class="metric-info">
                <div class="metric-number">{{ metrics.totalStrategies }}</div>
                <div class="metric-label">总策略数</div>
                <div class="metric-change positive">+{{ metrics.newStrategies }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="metric-card">
            <div class="metric-content">
              <div class="metric-icon running">
                <el-icon><VideoPlay /></el-icon>
              </div>
              <div class="metric-info">
                <div class="metric-number">{{ metrics.runningStrategies }}</div>
                <div class="metric-label">运行中策略</div>
                <div class="metric-change">{{ metrics.runningRate }}%</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="metric-card">
            <div class="metric-content">
              <div class="metric-icon assets">
                <el-icon><Money /></el-icon>
              </div>
              <div class="metric-info">
                <div class="metric-number">¥{{ formatCurrency(metrics.totalAssets) }}</div>
                <div class="metric-label">总资产</div>
                <div class="metric-change" :class="metrics.assetChange >= 0 ? 'positive' : 'negative'">
                  {{ metrics.assetChange >= 0 ? '+' : '' }}{{ metrics.assetChange }}%
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="metric-card">
            <div class="metric-content">
              <div class="metric-icon alerts">
                <el-icon><Warning /></el-icon>
              </div>
              <div class="metric-info">
                <div class="metric-number">{{ metrics.activeAlerts }}</div>
                <div class="metric-label">活跃预警</div>
                <div class="metric-change negative">+{{ metrics.newAlerts }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 策略状态分布 -->
      <el-row :gutter="20" class="charts-row">
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <span>策略状态分布</span>
            </template>
            <div class="chart-container" ref="statusChartRef"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <span>策略类型分布</span>
            </template>
            <div class="chart-container" ref="typeChartRef"></div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 实时监控 -->
      <el-card class="realtime-card">
        <template #header>
          <span>实时监控</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="toggleAutoRefresh">
            {{ autoRefresh ? '停止自动刷新' : '开启自动刷新' }}
          </el-button>
        </template>
        
        <el-table :data="realtimeData" style="width: 100%">
          <el-table-column prop="strategyName" label="策略名称" width="200" />
          <el-table-column prop="type" label="类型" width="120">
            <template #default="scope">
              <el-tag :type="getTypeTag(scope.row.type)">
                {{ getTypeText(scope.row.type) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="getStatusTag(scope.row.status)">
                {{ getStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="currentCapital" label="当前资金" width="150">
            <template #default="scope">
              ¥{{ formatCurrency(scope.row.currentCapital) }}
            </template>
          </el-table-column>
          <el-table-column prop="totalReturn" label="累计收益" width="120">
            <template #default="scope">
              <span :class="scope.row.totalReturn >= 0 ? 'positive' : 'negative'">
                {{ scope.row.totalReturn >= 0 ? '+' : '' }}{{ scope.row.totalReturn }}%
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="maxDrawdown" label="最大回撤" width="120">
            <template #default="scope">
              <span class="negative">{{ scope.row.maxDrawdown }}%</span>
            </template>
          </el-table-column>
          <el-table-column prop="lastUpdate" label="最后更新" width="150" />
          <el-table-column label="操作" width="150">
            <template #default="scope">
              <el-button type="text" @click="viewDetail(scope.row)">详情</el-button>
              <el-button type="text" @click="viewAlerts(scope.row)">预警</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 最新预警 -->
      <el-card class="alerts-card">
        <template #header>
          <span>最新预警</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="viewAllAlerts">
            查看全部
          </el-button>
        </template>
        
        <div class="alerts-list">
          <div 
            v-for="alert in recentAlerts" 
            :key="alert.id" 
            class="alert-item"
            :class="alert.level"
          >
            <div class="alert-icon">
              <el-icon v-if="alert.level === 'high'"><Warning /></el-icon>
              <el-icon v-else-if="alert.level === 'medium'"><InfoFilled /></el-icon>
              <el-icon v-else><CircleCheck /></el-icon>
            </div>
            <div class="alert-content">
              <div class="alert-title">{{ alert.title }}</div>
              <div class="alert-description">{{ alert.description }}</div>
              <div class="alert-meta">
                <span>{{ alert.strategyName }}</span>
                <span>{{ formatTime(alert.createTime) }}</span>
              </div>
            </div>
            <div class="alert-actions">
              <el-button type="text" size="small" @click="handleAlert(alert)">处理</el-button>
              <el-button type="text" size="small" @click="ignoreAlert(alert)">忽略</el-button>
            </div>
          </div>
        </div>
      </el-card>

      <!-- 系统状态 -->
      <el-card class="system-card">
        <template #header>
          <span>系统状态</span>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="system-item">
              <div class="system-label">数据库连接</div>
              <div class="system-status">
                <el-tag type="success">正常</el-tag>
                <span class="system-time">响应时间: 15ms</span>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="system-item">
              <div class="system-label">数据源连接</div>
              <div class="system-status">
                <el-tag type="success">正常</el-tag>
                <span class="system-time">响应时间: 25ms</span>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="system-item">
              <div class="system-label">回测引擎</div>
              <div class="system-status">
                <el-tag type="success">正常</el-tag>
                <span class="system-time">运行中: 2个任务</span>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { 
  DataAnalysis, 
  VideoPlay, 
  Money, 
  Warning, 
  InfoFilled, 
  CircleCheck 
} from '@element-plus/icons-vue'
import * as echarts from 'echarts'

export default {
  name: 'MonitorOverview',
  setup() {
    const router = useRouter()

    const metrics = reactive({
      totalStrategies: 45,
      newStrategies: 3,
      runningStrategies: 12,
      runningRate: 26.7,
      totalAssets: 125000000,
      assetChange: 2.5,
      activeAlerts: 8,
      newAlerts: 2
    })

    const autoRefresh = ref(false)
    const refreshTimer = ref(null)

    const statusChartRef = ref(null)
    const typeChartRef = ref(null)
    let statusChart = null
    let typeChart = null

    const realtimeData = ref([
      {
        id: 1,
        strategyName: '稳健股债轮动策略',
        type: 'ASSET_ALLOCATION',
        status: 'ACTIVE',
        currentCapital: 1150000,
        totalReturn: 15.6,
        maxDrawdown: -8.3,
        lastUpdate: '2024-01-15 14:30:00'
      },
      {
        id: 2,
        strategyName: 'FOF组合管理策略',
        type: 'FOF',
        status: 'ACTIVE',
        currentCapital: 850000,
        totalReturn: 12.3,
        maxDrawdown: -6.8,
        lastUpdate: '2024-01-15 14:29:00'
      },
      {
        id: 3,
        strategyName: '基金指数组合策略',
        type: 'INDEX_COPY',
        status: 'PAUSED',
        currentCapital: 920000,
        totalReturn: 8.9,
        maxDrawdown: -4.2,
        lastUpdate: '2024-01-15 14:28:00'
      }
    ])

    const recentAlerts = ref([
      {
        id: 1,
        level: 'high',
        title: '回撤预警',
        description: 'FOF组合管理策略触发回撤预警，当前回撤-6.8%',
        strategyName: 'FOF组合管理策略',
        createTime: new Date(Date.now() - 30 * 60 * 1000)
      },
      {
        id: 2,
        level: 'medium',
        title: '权重偏离',
        description: '稳健股债轮动策略权重偏离超过阈值',
        strategyName: '稳健股债轮动策略',
        createTime: new Date(Date.now() - 2 * 60 * 60 * 1000)
      },
      {
        id: 3,
        level: 'low',
        title: '波动率预警',
        description: '基金指数组合策略波动率超过设定阈值',
        strategyName: '基金指数组合策略',
        createTime: new Date(Date.now() - 4 * 60 * 60 * 1000)
      }
    ])

    const getTypeTag = (type) => {
      const typeMap = {
        'ASSET_ALLOCATION': 'primary',
        'FOF': 'success',
        'INDEX_COPY': 'warning',
        'TIMING': 'danger'
      }
      return typeMap[type] || 'default'
    }

    const getTypeText = (type) => {
      const typeMap = {
        'ASSET_ALLOCATION': '大类资产配置',
        'FOF': 'FOF组合',
        'INDEX_COPY': '基金指数组合',
        'TIMING': '择时策略'
      }
      return typeMap[type] || type
    }

    const getStatusTag = (status) => {
      const statusMap = {
        'ACTIVE': 'success',
        'PAUSED': 'warning',
        'STOPPED': 'danger'
      }
      return statusMap[status] || 'default'
    }

    const getStatusText = (status) => {
      const statusMap = {
        'ACTIVE': '运行中',
        'PAUSED': '暂停',
        'STOPPED': '停止'
      }
      return statusMap[status] || status
    }

    const formatCurrency = (value) => {
      return new Intl.NumberFormat('zh-CN', {
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
      }).format(value)
    }

    const formatTime = (time) => {
      const now = new Date()
      const diff = now - time
      const minutes = Math.floor(diff / (1000 * 60))
      
      if (minutes < 1) {
        return '刚刚'
      } else if (minutes < 60) {
        return `${minutes}分钟前`
      } else {
        const hours = Math.floor(minutes / 60)
        return `${hours}小时前`
      }
    }

    const refreshData = () => {
      ElMessage.success('数据已刷新')
    }

    const exportReport = () => {
      ElMessage.success('报告导出功能开发中')
    }

    const toggleAutoRefresh = () => {
      autoRefresh.value = !autoRefresh.value
      if (autoRefresh.value) {
        refreshTimer.value = setInterval(() => {
          refreshData()
        }, 30000) // 30秒刷新一次
        ElMessage.success('已开启自动刷新')
      } else {
        if (refreshTimer.value) {
          clearInterval(refreshTimer.value)
          refreshTimer.value = null
        }
        ElMessage.info('已停止自动刷新')
      }
    }

    const viewDetail = (strategy) => {
      router.push(`/strategy/detail/${strategy.id}`)
    }

    const viewAlerts = (strategy) => {
      router.push(`/monitor/alerts?strategyId=${strategy.id}`)
    }

    const viewAllAlerts = () => {
      router.push('/monitor/alerts')
    }

    const handleAlert = (alert) => {
      ElMessage.success(`已处理预警: ${alert.title}`)
    }

    const ignoreAlert = (alert) => {
      ElMessage.info(`已忽略预警: ${alert.title}`)
    }

    const initCharts = () => {
      // 初始化策略状态分布图
      if (statusChartRef.value) {
        statusChart = echarts.init(statusChartRef.value)
        const statusOption = {
          tooltip: { trigger: 'item' },
          series: [{
            name: '策略状态',
            type: 'pie',
            radius: '60%',
            data: [
              { value: 12, name: '运行中', itemStyle: { color: '#67c23a' } },
              { value: 8, name: '暂停', itemStyle: { color: '#e6a23c' } },
              { value: 5, name: '停止', itemStyle: { color: '#f56c6c' } },
              { value: 20, name: '草稿', itemStyle: { color: '#909399' } }
            ]
          }]
        }
        statusChart.setOption(statusOption)
      }

      // 初始化策略类型分布图
      if (typeChartRef.value) {
        typeChart = echarts.init(typeChartRef.value)
        const typeOption = {
          tooltip: { trigger: 'item' },
          series: [{
            name: '策略类型',
            type: 'pie',
            radius: '60%',
            data: [
              { value: 20, name: '大类资产配置', itemStyle: { color: '#409eff' } },
              { value: 15, name: 'FOF组合管理', itemStyle: { color: '#67c23a' } },
              { value: 8, name: '基金指数组合', itemStyle: { color: '#e6a23c' } },
              { value: 2, name: '择时组合', itemStyle: { color: '#f56c6c' } }
            ]
          }]
        }
        typeChart.setOption(typeOption)
      }
    }

    onMounted(() => {
      setTimeout(() => {
        initCharts()
      }, 100)
    })

    onUnmounted(() => {
      if (refreshTimer.value) {
        clearInterval(refreshTimer.value)
      }
    })

    return {
      metrics,
      autoRefresh,
      statusChartRef,
      typeChartRef,
      realtimeData,
      recentAlerts,
      getTypeTag,
      getTypeText,
      getStatusTag,
      getStatusText,
      formatCurrency,
      formatTime,
      refreshData,
      exportReport,
      toggleAutoRefresh,
      viewDetail,
      viewAlerts,
      viewAllAlerts,
      handleAlert,
      ignoreAlert
    }
  }
}
</script>

<style scoped>
.monitor-overview {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.page-header h1 {
  margin: 0;
  color: #303133;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.overview-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.metrics-row {
  margin-bottom: 20px;
}

.metric-card {
  .metric-content {
    display: flex;
    align-items: center;
    
    .metric-icon {
      width: 56px;
      height: 56px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;
      font-size: 24px;
      
      &.total {
        background-color: #e6f7ff;
        color: #1890ff;
      }
      
      &.running {
        background-color: #f6ffed;
        color: #52c41a;
      }
      
      &.assets {
        background-color: #fff7e6;
        color: #fa8c16;
      }
      
      &.alerts {
        background-color: #fff2e8;
        color: #fa8c16;
      }
    }
    
    .metric-info {
      flex: 1;
      
      .metric-number {
        font-size: 28px;
        font-weight: bold;
        color: #1f2937;
        line-height: 1;
      }
      
      .metric-label {
        font-size: 14px;
        color: #6b7280;
        margin: 4px 0;
      }
      
      .metric-change {
        font-size: 12px;
        
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

.charts-row {
  margin-bottom: 20px;
}

.chart-card {
  .chart-container {
    height: 300px;
  }
}

.realtime-card,
.alerts-card,
.system-card {
  margin-bottom: 20px;
}

.alerts-list {
  .alert-item {
    display: flex;
    align-items: center;
    padding: 16px;
    border-radius: 8px;
    margin-bottom: 12px;
    border-left: 4px solid;
    
    &.high {
      background-color: #fff2f0;
      border-left-color: #ff4d4f;
    }
    
    &.medium {
      background-color: #fff7e6;
      border-left-color: #fa8c16;
    }
    
    &.low {
      background-color: #f6ffed;
      border-left-color: #52c41a;
    }
    
    .alert-icon {
      margin-right: 12px;
      font-size: 20px;
      
      .high & {
        color: #ff4d4f;
      }
      
      .medium & {
        color: #fa8c16;
      }
      
      .low & {
        color: #52c41a;
      }
    }
    
    .alert-content {
      flex: 1;
      
      .alert-title {
        font-weight: 500;
        margin-bottom: 4px;
      }
      
      .alert-description {
        color: #606266;
        font-size: 14px;
        margin-bottom: 8px;
      }
      
      .alert-meta {
        display: flex;
        gap: 16px;
        font-size: 12px;
        color: #909399;
      }
    }
    
    .alert-actions {
      display: flex;
      gap: 8px;
    }
  }
}

.system-item {
  padding: 16px;
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  
  .system-label {
    font-weight: 500;
    margin-bottom: 8px;
  }
  
  .system-status {
    display: flex;
    align-items: center;
    gap: 12px;
    
    .system-time {
      font-size: 12px;
      color: #909399;
    }
  }
}

.positive {
  color: #67c23a;
}

.negative {
  color: #f56c6c;
}
</style> 