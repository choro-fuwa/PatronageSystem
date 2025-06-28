<template>
  <div class="strategy-detail">
    <div class="page-header">
      <div class="header-content">
        <div class="back-button" @click="goBack">
          <el-icon><ArrowLeft /></el-icon>
          返回
        </div>
        <div class="strategy-info">
          <h1>{{ strategy.name }}</h1>
          <div class="strategy-meta">
            <el-tag :type="getTypeTag(strategy.type)">{{ getTypeText(strategy.type) }}</el-tag>
            <el-tag :type="getStatusTag(strategy.status)">{{ getStatusText(strategy.status) }}</el-tag>
            <el-tag :type="getRiskTag(strategy.riskLevel)">{{ getRiskText(strategy.riskLevel) }}</el-tag>
          </div>
        </div>
        <div class="header-actions">
          <el-button type="primary" @click="startBacktest">启动回测</el-button>
          <el-button @click="editStrategy">编辑参数</el-button>
          <el-button 
            :type="strategy.status === 'ACTIVE' ? 'warning' : 'success'"
            @click="toggleStrategy"
          >
            {{ strategy.status === 'ACTIVE' ? '暂停' : '启动' }}
          </el-button>
        </div>
      </div>
    </div>

    <div class="detail-content">
      <!-- 基本信息 -->
      <el-card class="info-card">
        <template #header>
          <span>基本信息</span>
        </template>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="策略名称">{{ strategy.name }}</el-descriptions-item>
          <el-descriptions-item label="策略类型">{{ getTypeText(strategy.type) }}</el-descriptions-item>
          <el-descriptions-item label="策略状态">{{ getStatusText(strategy.status) }}</el-descriptions-item>
          <el-descriptions-item label="初始资金">¥{{ formatCurrency(strategy.initialCapital) }}</el-descriptions-item>
          <el-descriptions-item label="当前资金">¥{{ formatCurrency(strategy.currentCapital) }}</el-descriptions-item>
          <el-descriptions-item label="风险等级">{{ getRiskText(strategy.riskLevel) }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatDate(strategy.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ formatDate(strategy.updateTime) }}</el-descriptions-item>
          <el-descriptions-item label="创建人">{{ strategy.createBy }}</el-descriptions-item>
        </el-descriptions>
        <div class="description-section">
          <h4>策略描述</h4>
          <p>{{ strategy.description || '暂无描述' }}</p>
        </div>
      </el-card>

      <!-- 绩效指标 -->
      <el-card class="performance-card">
        <template #header>
          <span>绩效指标</span>
        </template>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="metric-item">
              <div class="metric-label">年化收益率</div>
              <div class="metric-value" :class="strategy.annualReturn >= 0 ? 'positive' : 'negative'">
                {{ strategy.annualReturn >= 0 ? '+' : '' }}{{ strategy.annualReturn }}%
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="metric-item">
              <div class="metric-label">最大回撤</div>
              <div class="metric-value negative">{{ strategy.maxDrawdown }}%</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="metric-item">
              <div class="metric-label">夏普比率</div>
              <div class="metric-value">{{ strategy.sharpeRatio }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="metric-item">
              <div class="metric-label">波动率</div>
              <div class="metric-value">{{ strategy.volatility }}%</div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 持仓明细 -->
      <el-card class="positions-card">
        <template #header>
          <span>持仓明细</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="refreshPositions">
            刷新
          </el-button>
        </template>
        <el-table :data="positions" style="width: 100%">
          <el-table-column prop="symbol" label="标的代码" width="120" />
          <el-table-column prop="symbolName" label="标的名称" width="150" />
          <el-table-column prop="symbolType" label="类型" width="100">
            <template #default="scope">
              <el-tag :type="getSymbolTypeTag(scope.row.symbolType)">
                {{ getSymbolTypeText(scope.row.symbolType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="quantity" label="持仓数量" width="120" />
          <el-table-column prop="targetWeight" label="目标权重" width="120">
            <template #default="scope">
              {{ scope.row.targetWeight }}%
            </template>
          </el-table-column>
          <el-table-column prop="currentWeight" label="当前权重" width="120">
            <template #default="scope">
              {{ scope.row.currentWeight }}%
            </template>
          </el-table-column>
          <el-table-column prop="weightDeviation" label="权重偏离" width="120">
            <template #default="scope">
              <span :class="scope.row.weightDeviation >= 0 ? 'positive' : 'negative'">
                {{ scope.row.weightDeviation >= 0 ? '+' : '' }}{{ scope.row.weightDeviation }}%
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="costPrice" label="成本价" width="120">
            <template #default="scope">
              ¥{{ scope.row.costPrice }}
            </template>
          </el-table-column>
          <el-table-column prop="currentPrice" label="当前价" width="120">
            <template #default="scope">
              ¥{{ scope.row.currentPrice }}
            </template>
          </el-table-column>
          <el-table-column prop="marketValue" label="市值" width="120">
            <template #default="scope">
              ¥{{ formatCurrency(scope.row.marketValue) }}
            </template>
          </el-table-column>
          <el-table-column prop="unrealizedPnl" label="浮动盈亏" width="120">
            <template #default="scope">
              <span :class="scope.row.unrealizedPnl >= 0 ? 'positive' : 'negative'">
                {{ scope.row.unrealizedPnl >= 0 ? '+' : '' }}¥{{ formatCurrency(scope.row.unrealizedPnl) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="unrealizedPnlRate" label="盈亏率" width="120">
            <template #default="scope">
              <span :class="scope.row.unrealizedPnlRate >= 0 ? 'positive' : 'negative'">
                {{ scope.row.unrealizedPnlRate >= 0 ? '+' : '' }}{{ scope.row.unrealizedPnlRate }}%
              </span>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 调仓记录 -->
      <el-card class="rebalance-card">
        <template #header>
          <span>调仓记录</span>
        </template>
        <el-table :data="rebalanceRecords" style="width: 100%">
          <el-table-column prop="date" label="调仓日期" width="150" />
          <el-table-column prop="type" label="调仓类型" width="120">
            <template #default="scope">
              <el-tag :type="getRebalanceTypeTag(scope.row.type)">
                {{ getRebalanceTypeText(scope.row.type) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="symbol" label="标的" width="120" />
          <el-table-column prop="action" label="操作" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.action === 'BUY' ? 'success' : 'danger'">
                {{ scope.row.action === 'BUY' ? '买入' : '卖出' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="120" />
          <el-table-column prop="price" label="价格" width="120">
            <template #default="scope">
              ¥{{ scope.row.price }}
            </template>
          </el-table-column>
          <el-table-column prop="amount" label="金额" width="120">
            <template #default="scope">
              ¥{{ formatCurrency(scope.row.amount) }}
            </template>
          </el-table-column>
          <el-table-column prop="reason" label="调仓原因" />
        </el-table>
      </el-card>

      <!-- 收益分析 -->
      <el-card class="analysis-card">
        <template #header>
          <span>收益分析</span>
        </template>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="chart-container" ref="returnChartRef"></div>
          </el-col>
          <el-col :span="12">
            <div class="chart-container" ref="drawdownChartRef"></div>
          </el-col>
        </el-row>
      </el-card>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { getStrategyDetail } from '@/api/strategy'

export default {
  name: 'StrategyDetail',
  setup() {
    const route = useRoute()
    const router = useRouter()
    const strategyId = route.params.id

    const strategy = reactive({
      id: strategyId,
      name: '稳健股债轮动策略',
      type: 'ASSET_ALLOCATION',
      status: 'ACTIVE',
      riskLevel: 'MEDIUM',
      description: '基于宏观经济周期和估值水平，动态调整股票和债券配置比例的稳健型策略。',
      initialCapital: 1000000,
      currentCapital: 1150000,
      annualReturn: 15.6,
      maxDrawdown: -8.3,
      sharpeRatio: 1.85,
      volatility: 12.3,
      winRate: 68.5,
      createTime: '2024-01-01 10:00:00',
      updateTime: '2024-01-15 14:30:00',
      createBy: 'admin'
    })

    const positions = ref([
      {
        symbol: '000300.SH',
        symbolName: '沪深300ETF',
        symbolType: 'ETF',
        quantity: 10000,
        targetWeight: 60,
        currentWeight: 58.5,
        weightDeviation: -1.5,
        costPrice: 3.85,
        currentPrice: 4.12,
        marketValue: 412000,
        unrealizedPnl: 27000,
        unrealizedPnlRate: 7.01
      },
      {
        symbol: '019547.SH',
        symbolName: '国债ETF',
        symbolType: 'ETF',
        quantity: 5000,
        targetWeight: 40,
        currentWeight: 41.5,
        weightDeviation: 1.5,
        costPrice: 102.5,
        currentPrice: 103.8,
        marketValue: 519000,
        unrealizedPnl: 6500,
        unrealizedPnlRate: 1.27
      }
    ])

    const rebalanceRecords = ref([
      {
        date: '2024-01-15',
        type: 'THRESHOLD',
        symbol: '000300.SH',
        action: 'BUY',
        quantity: 1000,
        price: 4.12,
        amount: 4120000,
        reason: '权重偏离超过阈值，自动调仓'
      },
      {
        date: '2024-01-10',
        type: 'PERIODIC',
        symbol: '019547.SH',
        action: 'SELL',
        quantity: 500,
        price: 103.8,
        amount: 519000,
        reason: '定期再平衡'
      }
    ])

    const returnChartRef = ref(null)
    const drawdownChartRef = ref(null)
    let returnChart = null
    let drawdownChart = null

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
        'DRAFT': 'info',
        'ACTIVE': 'success',
        'PAUSED': 'warning',
        'STOPPED': 'danger'
      }
      return statusMap[status] || 'default'
    }

    const getStatusText = (status) => {
      const statusMap = {
        'DRAFT': '草稿',
        'ACTIVE': '运行中',
        'PAUSED': '暂停',
        'STOPPED': '停止'
      }
      return statusMap[status] || status
    }

    const getRiskTag = (riskLevel) => {
      const riskMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger'
      }
      return riskMap[riskLevel] || 'default'
    }

    const getRiskText = (riskLevel) => {
      const riskMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险'
      }
      return riskMap[riskLevel] || riskLevel
    }

    const getSymbolTypeTag = (type) => {
      const typeMap = {
        'STOCK': 'primary',
        'BOND': 'success',
        'FUND': 'warning',
        'ETF': 'info',
        'GOLD': 'danger'
      }
      return typeMap[type] || 'default'
    }

    const getSymbolTypeText = (type) => {
      const typeMap = {
        'STOCK': '股票',
        'BOND': '债券',
        'FUND': '基金',
        'ETF': 'ETF',
        'GOLD': '黄金'
      }
      return typeMap[type] || type
    }

    const getRebalanceTypeTag = (type) => {
      const typeMap = {
        'THRESHOLD': 'warning',
        'PERIODIC': 'info',
        'MANUAL': 'primary'
      }
      return typeMap[type] || 'default'
    }

    const getRebalanceTypeText = (type) => {
      const typeMap = {
        'THRESHOLD': '阈值触发',
        'PERIODIC': '定期调仓',
        'MANUAL': '手动调仓'
      }
      return typeMap[type] || type
    }

    const formatCurrency = (value) => {
      return new Intl.NumberFormat('zh-CN', {
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
      }).format(value)
    }

    const formatDate = (dateStr) => {
      if (!dateStr) return ''
      return new Date(dateStr).toLocaleString('zh-CN')
    }

    const goBack = () => {
      router.back()
    }

    const startBacktest = () => {
      router.push(`/strategy/backtest?strategyId=${strategyId}`)
    }

    const editStrategy = () => {
      router.push(`/strategy/edit/${strategyId}`)
    }

    const toggleStrategy = () => {
      const newStatus = strategy.status === 'ACTIVE' ? 'PAUSED' : 'ACTIVE'
      strategy.status = newStatus
      ElMessage.success(`策略已${newStatus === 'ACTIVE' ? '启动' : '暂停'}`)
    }

    const refreshPositions = () => {
      ElMessage.success('持仓数据已刷新')
    }

    const initCharts = () => {
      // 初始化收益曲线图
      if (returnChartRef.value) {
        returnChart = echarts.init(returnChartRef.value)
        const returnOption = {
          title: { text: '累计收益率' },
          tooltip: { trigger: 'axis' },
          xAxis: { type: 'category', data: ['1月', '2月', '3月', '4月', '5月', '6月'] },
          yAxis: { type: 'value', axisLabel: { formatter: '{value}%' } },
          series: [{
            name: '累计收益',
            type: 'line',
            data: [2.1, 5.3, 7.1, 11.6, 14.5, 15.6],
            smooth: true,
            lineStyle: { color: '#1890ff' },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [
                  { offset: 0, color: 'rgba(24, 144, 255, 0.3)' },
                  { offset: 1, color: 'rgba(24, 144, 255, 0.1)' }
                ]
              }
            }
          }]
        }
        returnChart.setOption(returnOption)
      }

      // 初始化回撤图
      if (drawdownChartRef.value) {
        drawdownChart = echarts.init(drawdownChartRef.value)
        const drawdownOption = {
          title: { text: '回撤曲线' },
          tooltip: { trigger: 'axis' },
          xAxis: { type: 'category', data: ['1月', '2月', '3月', '4月', '5月', '6月'] },
          yAxis: { type: 'value', axisLabel: { formatter: '{value}%' } },
          series: [{
            name: '回撤',
            type: 'line',
            data: [-1.2, -3.5, -2.1, -5.8, -4.2, -8.3],
            smooth: true,
            lineStyle: { color: '#ff4d4f' },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [
                  { offset: 0, color: 'rgba(255, 77, 79, 0.3)' },
                  { offset: 1, color: 'rgba(255, 77, 79, 0.1)' }
                ]
              }
            }
          }]
        }
        drawdownChart.setOption(drawdownOption)
      }
    }

    onMounted(() => {
      // 加载策略详情
      getStrategyDetail(strategyId).then(response => {
        if (response.code === 200) {
          Object.assign(strategy, response.data)
        }
      })

      // 初始化图表
      setTimeout(() => {
        initCharts()
      }, 100)
    })

    return {
      strategy,
      positions,
      rebalanceRecords,
      returnChartRef,
      drawdownChartRef,
      getTypeTag,
      getTypeText,
      getStatusTag,
      getStatusText,
      getRiskTag,
      getRiskText,
      getSymbolTypeTag,
      getSymbolTypeText,
      getRebalanceTypeTag,
      getRebalanceTypeText,
      formatCurrency,
      formatDate,
      goBack,
      startBacktest,
      editStrategy,
      toggleStrategy,
      refreshPositions
    }
  }
}
</script>

<style scoped>
.strategy-detail {
  padding: 20px;
}

.page-header {
  margin-bottom: 30px;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.back-button {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: #1890ff;
  font-size: 14px;
}

.strategy-info h1 {
  margin: 0 0 10px 0;
  color: #303133;
}

.strategy-meta {
  display: flex;
  gap: 10px;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.detail-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.info-card,
.performance-card,
.positions-card,
.rebalance-card,
.analysis-card {
  margin-bottom: 20px;
}

.description-section {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

.description-section h4 {
  margin: 0 0 10px 0;
  color: #303133;
}

.description-section p {
  margin: 0;
  color: #606266;
  line-height: 1.6;
}

.metric-item {
  text-align: center;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
}

.metric-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.metric-value {
  font-size: 24px;
  font-weight: bold;
}

.metric-value.positive {
  color: #67c23a;
}

.metric-value.negative {
  color: #f56c6c;
}

.chart-container {
  height: 300px;
}

.positive {
  color: #67c23a;
}

.negative {
  color: #f56c6c;
}
</style> 