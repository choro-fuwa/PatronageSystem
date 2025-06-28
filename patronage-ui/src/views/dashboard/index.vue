<template>
  <div class="dashboard-container">
    <!-- 欢迎信息 -->
    <div class="welcome-section">
      <div class="welcome-content">
        <h1>欢迎使用策略管理系统</h1>
        <p>今天是 {{ currentDate }}，祝您投资顺利！</p>
      </div>
      <div class="weather-info">
        <el-icon><Sunny /></el-icon>
        <span>晴 25°C</span>
      </div>
    </div>

    <!-- 关键指标卡片 -->
    <div class="metrics-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="metric-card">
            <div class="metric-content">
              <div class="metric-icon total-strategies">
                <el-icon><TrendCharts /></el-icon>
              </div>
              <div class="metric-info">
                <div class="metric-number">{{ metrics.totalStrategies }}</div>
                <div class="metric-label">总策略数</div>
                <div class="metric-change positive">
                  +{{ metrics.newStrategies }} 本月新增
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="metric-card">
            <div class="metric-content">
              <div class="metric-icon running-strategies">
                <el-icon><VideoPlay /></el-icon>
              </div>
              <div class="metric-info">
                <div class="metric-number">{{ metrics.runningStrategies }}</div>
                <div class="metric-label">运行中策略</div>
                <div class="metric-change">
                  运行率 {{ metrics.runningRate }}%
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="metric-card">
            <div class="metric-content">
              <div class="metric-icon total-assets">
                <el-icon><Money /></el-icon>
              </div>
              <div class="metric-info">
                <div class="metric-number">{{ formatCurrency(metrics.totalAssets) }}</div>
                <div class="metric-label">总资产规模</div>
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
              <div class="metric-icon total-return">
                <el-icon><TrendingUp /></el-icon>
              </div>
              <div class="metric-info">
                <div class="metric-number">{{ metrics.totalReturn }}%</div>
                <div class="metric-label">总收益率</div>
                <div class="metric-change" :class="metrics.returnChange >= 0 ? 'positive' : 'negative'">
                  {{ metrics.returnChange >= 0 ? '+' : '' }}{{ metrics.returnChange }}%
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 图表区域 -->
    <div class="charts-section">
      <el-row :gutter="20">
        <!-- 收益趋势图 -->
        <el-col :span="16">
          <el-card class="chart-card">
            <template #header>
              <div class="chart-header">
                <span>收益趋势</span>
                <div class="chart-actions">
                  <el-radio-group v-model="returnChartPeriod" size="small">
                    <el-radio-button label="7d">7天</el-radio-button>
                    <el-radio-button label="30d">30天</el-radio-button>
                    <el-radio-button label="90d">90天</el-radio-button>
                    <el-radio-button label="1y">1年</el-radio-button>
                  </el-radio-group>
                </div>
              </div>
            </template>
            <div class="chart-container" ref="returnChartRef"></div>
          </el-card>
        </el-col>
        
        <!-- 策略分布 -->
        <el-col :span="8">
          <el-card class="chart-card">
            <template #header>
              <span>策略类型分布</span>
            </template>
            <div class="chart-container" ref="strategyChartRef"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 快速操作 -->
    <div class="quick-actions-section">
      <el-card>
        <template #header>
          <span>快速操作</span>
        </template>
        <div class="quick-actions">
          <div class="action-item" @click="handleQuickAction('create')">
            <div class="action-icon">
              <el-icon><Plus /></el-icon>
            </div>
            <div class="action-info">
              <div class="action-title">创建策略</div>
              <div class="action-desc">快速创建新的投资策略</div>
            </div>
          </div>
          <div class="action-item" @click="handleQuickAction('backtest')">
            <div class="action-icon">
              <el-icon><DataAnalysis /></el-icon>
            </div>
            <div class="action-info">
              <div class="action-title">策略回测</div>
              <div class="action-desc">对策略进行历史回测</div>
            </div>
          </div>
          <div class="action-item" @click="handleQuickAction('monitor')">
            <div class="action-icon">
              <el-icon><Monitor /></el-icon>
            </div>
            <div class="action-info">
              <div class="action-title">策略监控</div>
              <div class="action-desc">实时监控策略运行状态</div>
            </div>
          </div>
          <div class="action-item" @click="handleQuickAction('report')">
            <div class="action-icon">
              <el-icon><Document /></el-icon>
            </div>
            <div class="action-info">
              <div class="action-title">生成报告</div>
              <div class="action-desc">生成策略绩效报告</div>
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  Sunny,
  TrendCharts,
  VideoPlay,
  Money,
  TrendingUp,
  Plus,
  DataAnalysis,
  Monitor,
  Document,
  Warning,
  Success,
  Info
} from '@element-plus/icons-vue'
import * as echarts from 'echarts'

const router = useRouter()
const returnChartRef = ref(null)
const strategyChartRef = ref(null)
let returnChart = null
let strategyChart = null

// 当前日期
const currentDate = ref(new Date().toLocaleDateString('zh-CN', {
  year: 'numeric',
  month: 'long',
  day: 'numeric',
  weekday: 'long'
}))

// 关键指标
const metrics = reactive({
  totalStrategies: 45,
  newStrategies: 3,
  runningStrategies: 12,
  runningRate: 26.7,
  totalAssets: 125000000,
  assetChange: 2.5,
  totalReturn: 15.8,
  returnChange: 1.2
})

// 图表周期
const returnChartPeriod = ref('30d')

// 通知列表
const notifications = ref([
  {
    id: 1,
    type: 'success',
    title: '策略审核通过',
    description: '稳健股债轮动策略已通过风控审核',
    time: new Date(Date.now() - 2 * 60 * 60 * 1000)
  },
  {
    id: 2,
    type: 'warning',
    title: '预警提醒',
    description: 'FOF组合管理策略触发回撤预警',
    time: new Date(Date.now() - 4 * 60 * 60 * 1000)
  },
  {
    id: 3,
    type: 'info',
    title: '系统更新',
    description: '系统将于今晚22:00进行维护更新',
    time: new Date(Date.now() - 6 * 60 * 60 * 1000)
  },
  {
    id: 4,
    type: 'success',
    title: '回测完成',
    description: '基金指数组合策略回测已完成',
    time: new Date(Date.now() - 8 * 60 * 60 * 1000)
  }
])

// 初始化图表
const initCharts = () => {
  nextTick(() => {
    initReturnChart()
    initStrategyChart()
  })
}

// 初始化收益趋势图
const initReturnChart = () => {
  if (!returnChartRef.value) return
  
  returnChart = echarts.init(returnChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross'
      }
    },
    legend: {
      data: ['策略收益', '基准收益']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: ['1月', '2月', '3月', '4月', '5月', '6月']
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: '{value}%'
      }
    },
    series: [
      {
        name: '策略收益',
        type: 'line',
        data: [2.1, 3.2, 1.8, 4.5, 2.9, 3.8],
        smooth: true,
        lineStyle: {
          color: '#1890ff'
        },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(24, 144, 255, 0.3)' },
              { offset: 1, color: 'rgba(24, 144, 255, 0.1)' }
            ]
          }
        }
      },
      {
        name: '基准收益',
        type: 'line',
        data: [1.5, 2.1, 1.2, 3.2, 2.1, 2.8],
        smooth: true,
        lineStyle: {
          color: '#d9d9d9'
        }
      }
    ]
  }
  
  returnChart.setOption(option)
}

// 初始化策略分布图
const initStrategyChart = () => {
  if (!strategyChartRef.value) return
  
  strategyChart = echarts.init(strategyChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    series: [
      {
        name: '策略类型',
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
            fontSize: '16',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          { value: 20, name: '大类资产配置' },
          { value: 15, name: 'FOF组合管理' },
          { value: 8, name: '基金指数组合' },
          { value: 2, name: '择时组合' }
        ]
      }
    ]
  }
  
  strategyChart.setOption(option)
}

// 快速操作处理
const handleQuickAction = (action) => {
  switch (action) {
    case 'create':
      router.push('/strategy/create')
      break
    case 'backtest':
      router.push('/strategy/backtest')
      break
    case 'monitor':
      router.push('/monitor/overview')
      break
    case 'report':
      router.push('/reports/performance')
      break
  }
}

// 获取通知图标
const getNotificationIcon = (type) => {
  const iconMap = {
    success: Success,
    warning: Warning,
    info: Info
  }
  return iconMap[type] || Info
}

// 格式化时间
const formatTime = (time) => {
  const now = new Date()
  const diff = now - time
  const hours = Math.floor(diff / (1000 * 60 * 60))
  
  if (hours < 1) {
    return '刚刚'
  } else if (hours < 24) {
    return `${hours}小时前`
  } else {
    return time.toLocaleDateString('zh-CN')
  }
}

// 格式化货币
const formatCurrency = (value) => {
  return new Intl.NumberFormat('zh-CN', {
    style: 'currency',
    currency: 'CNY',
    minimumFractionDigits: 0,
    maximumFractionDigits: 0
  }).format(value)
}

onMounted(() => {
  initCharts()
})
</script>

<style scoped lang="scss">
.dashboard-container {
  .welcome-section {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
    padding: 24px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 12px;
    color: white;
    
    .welcome-content {
      h1 {
        margin: 0 0 8px 0;
        font-size: 28px;
        font-weight: 600;
      }
      
      p {
        margin: 0;
        font-size: 16px;
        opacity: 0.9;
      }
    }
    
    .weather-info {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 16px;
    }
  }
  
  .metrics-section {
    margin-bottom: 24px;
    
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
          
          &.total-strategies {
            background-color: #e6f7ff;
            color: #1890ff;
          }
          
          &.running-strategies {
            background-color: #f6ffed;
            color: #52c41a;
          }
          
          &.total-assets {
            background-color: #fff7e6;
            color: #fa8c16;
          }
          
          &.total-return {
            background-color: #f6ffed;
            color: #52c41a;
          }
        }
        
        .metric-info {
          flex: 1;
          
          .metric-number {
            font-size: 28px;
            font-weight: 600;
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
  
  .quick-actions-section {
    .quick-actions {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 16px;
      
      .action-item {
        display: flex;
        align-items: center;
        padding: 16px;
        border-radius: 8px;
        cursor: pointer;
        transition: all 0.3s;
        border: 1px solid #f0f0f0;
        
        &:hover {
          background-color: #f5f5f5;
          border-color: #d9d9d9;
        }
        
        .action-icon {
          width: 48px;
          height: 48px;
          border-radius: 8px;
          background-color: #e6f7ff;
          color: #1890ff;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          font-size: 20px;
        }
        
        .action-info {
          flex: 1;
          
          .action-title {
            font-size: 16px;
            font-weight: 500;
            color: #1f2937;
            margin-bottom: 4px;
          }
          
          .action-desc {
            font-size: 14px;
            color: #6b7280;
          }
        }
      }
    }
  }
}
</style> 