<template>
  <div class="backtest-page">
    <div class="page-header">
      <div class="header-content">
        <div class="back-button" @click="goBack">
          <el-icon><ArrowLeft /></el-icon>
          返回
        </div>
        <h1>回测结果</h1>
        <div class="header-actions">
          <el-button @click="exportReport">导出报告</el-button>
          <el-button type="primary" @click="startNewBacktest">新建回测</el-button>
        </div>
      </div>
    </div>

    <div class="backtest-content">
      <!-- 回测基本信息 -->
      <el-card class="backtest-info-card">
        <template #header>
          <span>回测基本信息</span>
        </template>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="策略名称">{{ backtestResult.strategyName }}</el-descriptions-item>
          <el-descriptions-item label="回测周期">{{ backtestResult.startDate }} 至 {{ backtestResult.endDate }}</el-descriptions-item>
          <el-descriptions-item label="初始资金">¥{{ formatCurrency(backtestResult.initialCapital) }}</el-descriptions-item>
          <el-descriptions-item label="最终资金">¥{{ formatCurrency(backtestResult.finalCapital) }}</el-descriptions-item>
          <el-descriptions-item label="总收益率">{{ backtestResult.totalReturn >= 0 ? '+' : '' }}{{ backtestResult.totalReturn }}%</el-descriptions-item>
          <el-descriptions-item label="年化收益率">{{ backtestResult.annualReturn >= 0 ? '+' : '' }}{{ backtestResult.annualReturn }}%</el-descriptions-item>
          <el-descriptions-item label="最大回撤">{{ backtestResult.maxDrawdown }}%</el-descriptions-item>
          <el-descriptions-item label="夏普比率">{{ backtestResult.sharpeRatio }}</el-descriptions-item>
          <el-descriptions-item label="胜率">{{ backtestResult.winRate }}%</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 收益曲线对比 -->
      <el-card class="performance-card">
        <template #header>
          <span>收益曲线对比</span>
          <div style="float: right;">
            <el-radio-group v-model="chartPeriod" size="small">
              <el-radio-button label="1m">1月</el-radio-button>
              <el-radio-button label="3m">3月</el-radio-button>
              <el-radio-button label="6m">6月</el-radio-button>
              <el-radio-button label="1y">1年</el-radio-button>
              <el-radio-button label="all">全部</el-radio-button>
            </el-radio-group>
          </div>
        </template>
        <div class="chart-container" ref="performanceChartRef"></div>
      </el-card>

      <!-- 关键指标 -->
      <el-row :gutter="20" class="metrics-row">
        <el-col :span="6">
          <el-card class="metric-card">
            <div class="metric-content">
              <div class="metric-icon return">
                <el-icon><TrendCharts /></el-icon>
              </div>
              <div class="metric-info">
                <div class="metric-number" :class="backtestResult.totalReturn >= 0 ? 'positive' : 'negative'">
                  {{ backtestResult.totalReturn >= 0 ? '+' : '' }}{{ backtestResult.totalReturn }}%
                </div>
                <div class="metric-label">总收益率</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="metric-card">
            <div class="metric-content">
              <div class="metric-icon drawdown">
                <el-icon><Warning /></el-icon>
              </div>
              <div class="metric-info">
                <div class="metric-number negative">{{ backtestResult.maxDrawdown }}%</div>
                <div class="metric-label">最大回撤</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="metric-card">
            <div class="metric-content">
              <div class="metric-icon sharpe">
                <el-icon><DataAnalysis /></el-icon>
              </div>
              <div class="metric-info">
                <div class="metric-number">{{ backtestResult.sharpeRatio }}</div>
                <div class="metric-label">夏普比率</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="metric-card">
            <div class="metric-content">
              <div class="metric-icon winrate">
                <el-icon><CircleCheck /></el-icon>
              </div>
              <div class="metric-info">
                <div class="metric-number">{{ backtestResult.winRate }}%</div>
                <div class="metric-label">胜率</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 详细指标 -->
      <el-card class="detailed-metrics-card">
        <template #header>
          <span>详细指标</span>
        </template>
        <el-row :gutter="20">
          <el-col :span="12">
            <h4>收益指标</h4>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="年化收益率">{{ backtestResult.annualReturn }}%</el-descriptions-item>
              <el-descriptions-item label="月化收益率">{{ backtestResult.monthlyReturn }}%</el-descriptions-item>
              <el-descriptions-item label="日化收益率">{{ backtestResult.dailyReturn }}%</el-descriptions-item>
              <el-descriptions-item label="超额收益">{{ backtestResult.excessReturn }}%</el-descriptions-item>
              <el-descriptions-item label="信息比率">{{ backtestResult.informationRatio }}</el-descriptions-item>
            </el-descriptions>
          </el-col>
          <el-col :span="12">
            <h4>风险指标</h4>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="波动率">{{ backtestResult.volatility }}%</el-descriptions-item>
              <el-descriptions-item label="下行风险">{{ backtestResult.downsideRisk }}%</el-descriptions-item>
              <el-descriptions-item label="VaR(95%)">{{ backtestResult.var95 }}%</el-descriptions-item>
              <el-descriptions-item label="CVaR(95%)">{{ backtestResult.cvar95 }}%</el-descriptions-item>
              <el-descriptions-item label="Beta系数">{{ backtestResult.beta }}</el-descriptions-item>
            </el-descriptions>
          </el-col>
        </el-row>
      </el-card>

      <!-- 回撤分析 -->
      <el-card class="drawdown-card">
        <template #header>
          <span>回撤分析</span>
        </template>
        <div class="chart-container" ref="drawdownChartRef"></div>
        <el-table :data="drawdownPeriods" style="width: 100%; margin-top: 20px;">
          <el-table-column prop="startDate" label="开始日期" width="120" />
          <el-table-column prop="endDate" label="结束日期" width="120" />
          <el-table-column prop="duration" label="持续天数" width="100" />
          <el-table-column prop="maxDrawdown" label="最大回撤" width="120">
            <template #default="scope">
              {{ scope.row.maxDrawdown }}%
            </template>
          </el-table-column>
          <el-table-column prop="recoveryDays" label="恢复天数" width="100" />
          <el-table-column prop="reason" label="回撤原因" />
        </el-table>
      </el-card>

      <!-- 交易记录 -->
      <el-card class="trades-card">
        <template #header>
          <span>交易记录</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="exportTrades">
            导出交易记录
          </el-button>
        </template>
        <el-table :data="trades" style="width: 100%">
          <el-table-column prop="date" label="交易日期" width="120" />
          <el-table-column prop="symbol" label="标的代码" width="120" />
          <el-table-column prop="symbolName" label="标的名称" width="150" />
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
          <el-table-column prop="commission" label="手续费" width="120">
            <template #default="scope">
              ¥{{ formatCurrency(scope.row.commission) }}
            </template>
          </el-table-column>
          <el-table-column prop="reason" label="交易原因" />
        </el-table>
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="totalTrades"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>

      <!-- 持仓分析 -->
      <el-card class="positions-card">
        <template #header>
          <span>持仓分析</span>
        </template>
        <el-row :gutter="20">
          <el-col :span="12">
            <h4>最终持仓</h4>
            <div class="chart-container" ref="positionsChartRef"></div>
          </el-col>
          <el-col :span="12">
            <h4>持仓贡献度</h4>
            <el-table :data="positionContributions" style="width: 100%">
              <el-table-column prop="symbol" label="标的" width="100" />
              <el-table-column prop="contribution" label="贡献度" width="120">
                <template #default="scope">
                  <span :class="scope.row.contribution >= 0 ? 'positive' : 'negative'">
                    {{ scope.row.contribution >= 0 ? '+' : '' }}{{ scope.row.contribution }}%
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="weight" label="权重" width="100">
                <template #default="scope">
                  {{ scope.row.weight }}%
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-row>
      </el-card>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { 
  ArrowLeft, 
  TrendCharts, 
  Warning, 
  DataAnalysis, 
  CircleCheck 
} from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { getBacktestResult } from '@/api/strategy'

export default {
  name: 'BacktestResult',
  setup() {
    const route = useRoute()
    const router = useRouter()
    const backtestId = route.params.id

    const backtestResult = reactive({
      id: backtestId,
      strategyName: '稳健股债轮动策略',
      startDate: '2023-01-01',
      endDate: '2023-12-31',
      initialCapital: 1000000,
      finalCapital: 1156000,
      totalReturn: 15.6,
      annualReturn: 15.6,
      maxDrawdown: -8.3,
      sharpeRatio: 1.85,
      winRate: 68.5,
      monthlyReturn: 1.22,
      dailyReturn: 0.04,
      excessReturn: 5.2,
      informationRatio: 0.85,
      volatility: 12.3,
      downsideRisk: 8.7,
      var95: -2.1,
      cvar95: -3.2,
      beta: 0.85
    })

    const chartPeriod = ref('all')
    const currentPage = ref(1)
    const pageSize = ref(20)
    const totalTrades = ref(156)

    const performanceChartRef = ref(null)
    const drawdownChartRef = ref(null)
    const positionsChartRef = ref(null)
    let performanceChart = null
    let drawdownChart = null
    let positionsChart = null

    const drawdownPeriods = ref([
      {
        startDate: '2023-03-15',
        endDate: '2023-04-20',
        duration: 36,
        maxDrawdown: -8.3,
        recoveryDays: 45,
        reason: '市场波动加剧，股债双杀'
      },
      {
        startDate: '2023-08-10',
        endDate: '2023-09-05',
        duration: 26,
        maxDrawdown: -5.2,
        recoveryDays: 30,
        reason: '政策预期变化，市场调整'
      }
    ])

    const trades = ref([
      {
        date: '2023-01-03',
        symbol: '000300.SH',
        symbolName: '沪深300ETF',
        action: 'BUY',
        quantity: 10000,
        price: 3.85,
        amount: 385000,
        commission: 385,
        reason: '策略初始化'
      },
      {
        date: '2023-01-03',
        symbol: '019547.SH',
        symbolName: '国债ETF',
        action: 'BUY',
        quantity: 5000,
        price: 102.5,
        amount: 512500,
        commission: 512,
        reason: '策略初始化'
      }
    ])

    const positionContributions = ref([
      {
        symbol: '000300.SH',
        contribution: 9.8,
        weight: 60
      },
      {
        symbol: '019547.SH',
        contribution: 5.8,
        weight: 40
      }
    ])

    const formatCurrency = (value) => {
      return new Intl.NumberFormat('zh-CN', {
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
      }).format(value)
    }

    const goBack = () => {
      router.back()
    }

    const exportReport = () => {
      ElMessage.success('报告导出功能开发中')
    }

    const startNewBacktest = () => {
      router.push('/strategy/backtest')
    }

    const exportTrades = () => {
      ElMessage.success('交易记录导出功能开发中')
    }

    const handleSizeChange = (val) => {
      pageSize.value = val
      // 重新加载数据
    }

    const handleCurrentChange = (val) => {
      currentPage.value = val
      // 重新加载数据
    }

    const initCharts = () => {
      nextTick(() => {
        initPerformanceChart()
        initDrawdownChart()
        initPositionsChart()
      })
    }

    const initPerformanceChart = () => {
      if (!performanceChartRef.value) return
      
      performanceChart = echarts.init(performanceChartRef.value)
      
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
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
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
            data: [2.1, 3.2, 1.8, 4.5, 2.9, 3.8, 1.5, 2.3, 3.1, 2.8, 4.2, 3.5],
            smooth: true,
            lineStyle: {
              color: '#1890ff'
            },
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
          },
          {
            name: '基准收益',
            type: 'line',
            data: [1.5, 2.1, 1.2, 3.2, 2.1, 2.8, 1.8, 1.9, 2.5, 2.2, 3.1, 2.8],
            smooth: true,
            lineStyle: {
              color: '#d9d9d9'
            }
          }
        ]
      }
      
      performanceChart.setOption(option)
    }

    const initDrawdownChart = () => {
      if (!drawdownChartRef.value) return
      
      drawdownChart = echarts.init(drawdownChartRef.value)
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: '{value}%'
          }
        },
        series: [
          {
            name: '回撤',
            type: 'line',
            data: [-1.2, -2.1, -8.3, -6.5, -4.2, -2.8, -1.5, -5.2, -3.1, -1.8, -0.5, -0.2],
            smooth: true,
            lineStyle: {
              color: '#ff4d4f'
            },
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
          }
        ]
      }
      
      drawdownChart.setOption(option)
    }

    const initPositionsChart = () => {
      if (!positionsChartRef.value) return
      
      positionsChart = echarts.init(positionsChartRef.value)
      
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
                fontSize: '16',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: 60, name: '沪深300ETF' },
              { value: 40, name: '国债ETF' }
            ]
          }
        ]
      }
      
      positionsChart.setOption(option)
    }

    onMounted(() => {
      // 加载回测结果
      getBacktestResult(backtestId).then(response => {
        if (response.code === 200) {
          Object.assign(backtestResult, response.data)
        }
      })

      // 初始化图表
      setTimeout(() => {
        initCharts()
      }, 100)
    })

    return {
      backtestResult,
      chartPeriod,
      currentPage,
      pageSize,
      totalTrades,
      performanceChartRef,
      drawdownChartRef,
      positionsChartRef,
      drawdownPeriods,
      trades,
      positionContributions,
      formatCurrency,
      goBack,
      exportReport,
      startNewBacktest,
      exportTrades,
      handleSizeChange,
      handleCurrentChange
    }
  }
}
</script>

<style scoped>
.backtest-page {
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

.header-content h1 {
  margin: 0;
  color: #303133;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.backtest-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.backtest-info-card,
.performance-card,
.detailed-metrics-card,
.drawdown-card,
.trades-card,
.positions-card {
  margin-bottom: 20px;
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
      
      &.return {
        background-color: #f6ffed;
        color: #52c41a;
      }
      
      &.drawdown {
        background-color: #fff2e8;
        color: #fa8c16;
      }
      
      &.sharpe {
        background-color: #e6f7ff;
        color: #1890ff;
      }
      
      &.winrate {
        background-color: #f6ffed;
        color: #52c41a;
      }
    }
    
    .metric-info {
      flex: 1;
      
      .metric-number {
        font-size: 24px;
        font-weight: bold;
        line-height: 1;
        
        &.positive {
          color: #52c41a;
        }
        
        &.negative {
          color: #f56c6c;
        }
      }
      
      .metric-label {
        font-size: 14px;
        color: #6b7280;
        margin: 4px 0;
      }
    }
  }
}

.chart-container {
  height: 400px;
}

.pagination-container {
  margin-top: 20px;
  text-align: center;
}

.positive {
  color: #67c23a;
}

.negative {
  color: #f56c6c;
}
</style> 