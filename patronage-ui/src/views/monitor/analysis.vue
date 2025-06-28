<template>
  <div class="monitor-analysis">
    <div class="page-header">
      <h1>绩效分析</h1>
      <div class="header-actions">
        <el-button @click="refreshData">刷新</el-button>
        <el-button type="primary" @click="exportReport">导出报告</el-button>
      </div>
    </div>

    <div class="analysis-content">
      <!-- 分析维度选择 -->
      <el-card class="dimension-card">
        <template #header>
          <span>分析维度</span>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="策略选择">
              <el-select v-model="selectedStrategy" placeholder="请选择策略" style="width: 100%">
                <el-option label="全部策略" value="" />
                <el-option
                  v-for="strategy in strategyList"
                  :key="strategy.id"
                  :label="strategy.name"
                  :value="strategy.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="时间范围">
              <el-select v-model="timeRange" placeholder="请选择时间范围" style="width: 100%">
                <el-option label="最近1周" value="1w" />
                <el-option label="最近1月" value="1m" />
                <el-option label="最近3月" value="3m" />
                <el-option label="最近6月" value="6m" />
                <el-option label="最近1年" value="1y" />
                <el-option label="自定义" value="custom" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="基准选择">
              <el-select v-model="selectedBenchmark" placeholder="请选择基准" style="width: 100%">
                <el-option label="沪深300" value="000300.SH" />
                <el-option label="中证500" value="000905.SH" />
                <el-option label="中证1000" value="000852.SH" />
                <el-option label="国债指数" value="019547.SH" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="分析类型">
              <el-select v-model="analysisType" placeholder="请选择分析类型" style="width: 100%">
                <el-option label="收益分析" value="return" />
                <el-option label="风险分析" value="risk" />
                <el-option label="归因分析" value="attribution" />
                <el-option label="对比分析" value="comparison" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
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
                <div class="metric-number" :class="metrics.totalReturn >= 0 ? 'positive' : 'negative'">
                  {{ metrics.totalReturn >= 0 ? '+' : '' }}{{ metrics.totalReturn }}%
                </div>
                <div class="metric-label">总收益率</div>
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
              <div class="metric-icon sharpe">
                <el-icon><DataAnalysis /></el-icon>
              </div>
              <div class="metric-info">
                <div class="metric-number">{{ metrics.sharpeRatio }}</div>
                <div class="metric-label">夏普比率</div>
                <div class="metric-change" :class="metrics.sharpeChange >= 0 ? 'positive' : 'negative'">
                  {{ metrics.sharpeChange >= 0 ? '+' : '' }}{{ metrics.sharpeChange }}
                </div>
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
                <div class="metric-number negative">{{ metrics.maxDrawdown }}%</div>
                <div class="metric-label">最大回撤</div>
                <div class="metric-change" :class="metrics.drawdownChange <= 0 ? 'positive' : 'negative'">
                  {{ metrics.drawdownChange >= 0 ? '+' : '' }}{{ metrics.drawdownChange }}%
                </div>
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
                <div class="metric-number">{{ metrics.winRate }}%</div>
                <div class="metric-label">胜率</div>
                <div class="metric-change" :class="metrics.winRateChange >= 0 ? 'positive' : 'negative'">
                  {{ metrics.winRateChange >= 0 ? '+' : '' }}{{ metrics.winRateChange }}%
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 收益分析图表 -->
      <el-card class="chart-card">
        <template #header>
          <span>收益分析</span>
          <div style="float: right;">
            <el-radio-group v-model="chartType" size="small">
              <el-radio-button label="cumulative">累计收益</el-radio-button>
              <el-radio-button label="daily">日收益</el-radio-button>
              <el-radio-button label="rolling">滚动收益</el-radio-button>
            </el-radio-group>
          </div>
        </template>
        <div class="chart-container" ref="returnChartRef"></div>
      </el-card>

      <!-- 风险分析 -->
      <el-row :gutter="20" class="risk-row">
        <el-col :span="12">
          <el-card class="risk-card">
            <template #header>
              <span>风险指标</span>
            </template>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="年化收益率">{{ metrics.annualReturn }}%</el-descriptions-item>
              <el-descriptions-item label="年化波动率">{{ metrics.annualVolatility }}%</el-descriptions-item>
              <el-descriptions-item label="下行风险">{{ metrics.downsideRisk }}%</el-descriptions-item>
              <el-descriptions-item label="VaR(95%)">{{ metrics.var95 }}%</el-descriptions-item>
              <el-descriptions-item label="CVaR(95%)">{{ metrics.cvar95 }}%</el-descriptions-item>
              <el-descriptions-item label="Beta系数">{{ metrics.beta }}</el-descriptions-item>
              <el-descriptions-item label="信息比率">{{ metrics.informationRatio }}</el-descriptions-item>
              <el-descriptions-item label="特雷诺比率">{{ metrics.treynorRatio }}</el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="risk-card">
            <template #header>
              <span>回撤分析</span>
            </template>
            <div class="chart-container" ref="drawdownChartRef"></div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 归因分析 -->
      <el-card class="attribution-card">
        <template #header>
          <span>归因分析</span>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <h4>收益归因</h4>
            <div class="chart-container" ref="returnAttributionChartRef"></div>
          </el-col>
          <el-col :span="12">
            <h4>风险归因</h4>
            <div class="chart-container" ref="riskAttributionChartRef"></div>
          </el-col>
        </el-row>

        <el-table :data="attributionData" style="width: 100%; margin-top: 20px;">
          <el-table-column prop="factor" label="因子" width="150" />
          <el-table-column prop="contribution" label="贡献度" width="120">
            <template #default="scope">
              <span :class="scope.row.contribution >= 0 ? 'positive' : 'negative'">
                {{ scope.row.contribution >= 0 ? '+' : '' }}{{ scope.row.contribution }}%
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="weight" label="权重" width="120">
            <template #default="scope">
              {{ scope.row.weight }}%
            </template>
          </el-table-column>
          <el-table-column prop="return" label="收益率" width="120">
            <template #default="scope">
              <span :class="scope.row.return >= 0 ? 'positive' : 'negative'">
                {{ scope.row.return >= 0 ? '+' : '' }}{{ scope.row.return }}%
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="说明" />
        </el-table>
      </el-card>

      <!-- 对比分析 -->
      <el-card class="comparison-card">
        <template #header>
          <span>对比分析</span>
        </template>
        
        <el-table :data="comparisonData" style="width: 100%">
          <el-table-column prop="metric" label="指标" width="150" />
          <el-table-column prop="strategy" label="策略" width="120">
            <template #default="scope">
              <span :class="scope.row.strategy >= 0 ? 'positive' : 'negative'">
                {{ scope.row.strategy >= 0 ? '+' : '' }}{{ scope.row.strategy }}{{ scope.row.unit }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="benchmark" label="基准" width="120">
            <template #default="scope">
              <span :class="scope.row.benchmark >= 0 ? 'positive' : 'negative'">
                {{ scope.row.benchmark >= 0 ? '+' : '' }}{{ scope.row.benchmark }}{{ scope.row.unit }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="excess" label="超额" width="120">
            <template #default="scope">
              <span :class="scope.row.excess >= 0 ? 'positive' : 'negative'">
                {{ scope.row.excess >= 0 ? '+' : '' }}{{ scope.row.excess }}{{ scope.row.unit }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="rank" label="排名" width="100">
            <template #default="scope">
              <el-tag :type="getRankTag(scope.row.rank)">
                {{ scope.row.rank }}/{{ scope.row.total }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="说明" />
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  TrendCharts, 
  DataAnalysis, 
  Warning, 
  CircleCheck 
} from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { 
  getPerformanceAnalysis, 
  getReturnAttribution, 
  getRiskAttribution 
} from '@/api/strategy'

export default {
  name: 'MonitorAnalysis',
  setup() {
    const selectedStrategy = ref('')
    const timeRange = ref('1m')
    const selectedBenchmark = ref('000300.SH')
    const analysisType = ref('return')
    const chartType = ref('cumulative')

    const metrics = reactive({
      totalReturn: 15.6,
      returnChange: 2.3,
      sharpeRatio: 1.85,
      sharpeChange: 0.12,
      maxDrawdown: -8.3,
      drawdownChange: -1.2,
      winRate: 68.5,
      winRateChange: 3.2,
      annualReturn: 15.6,
      annualVolatility: 12.3,
      downsideRisk: 8.7,
      var95: -2.1,
      cvar95: -3.2,
      beta: 0.85,
      informationRatio: 0.85,
      treynorRatio: 0.18
    })

    const strategyList = ref([
      { id: 1, name: '稳健股债轮动策略' },
      { id: 2, name: 'FOF组合管理策略' },
      { id: 3, name: '基金指数组合策略' }
    ])

    const attributionData = ref([
      {
        factor: '股票配置',
        contribution: 9.8,
        weight: 60,
        return: 16.3,
        description: '股票类资产配置贡献'
      },
      {
        factor: '债券配置',
        contribution: 5.8,
        weight: 40,
        return: 14.5,
        description: '债券类资产配置贡献'
      },
      {
        factor: '择时操作',
        contribution: 2.1,
        weight: 10,
        return: 21.0,
        description: '择时操作贡献'
      },
      {
        factor: '选股能力',
        contribution: 1.9,
        weight: 50,
        return: 3.8,
        description: '个股选择贡献'
      }
    ])

    const comparisonData = ref([
      {
        metric: '总收益率',
        strategy: 15.6,
        benchmark: 12.3,
        excess: 3.3,
        rank: 2,
        total: 10,
        unit: '%',
        description: '策略相对基准的超额收益'
      },
      {
        metric: '夏普比率',
        strategy: 1.85,
        benchmark: 1.45,
        excess: 0.40,
        rank: 1,
        total: 10,
        unit: '',
        description: '风险调整后收益'
      },
      {
        metric: '最大回撤',
        strategy: -8.3,
        benchmark: -12.5,
        excess: 4.2,
        rank: 3,
        total: 10,
        unit: '%',
        description: '风险控制能力'
      },
      {
        metric: '胜率',
        strategy: 68.5,
        benchmark: 62.1,
        excess: 6.4,
        rank: 2,
        total: 10,
        unit: '%',
        description: '盈利概率'
      }
    ])

    const returnChartRef = ref(null)
    const drawdownChartRef = ref(null)
    const returnAttributionChartRef = ref(null)
    const riskAttributionChartRef = ref(null)
    let returnChart = null
    let drawdownChart = null
    let returnAttributionChart = null
    let riskAttributionChart = null

    const getRankTag = (rank) => {
      if (rank <= 3) return 'success'
      if (rank <= 6) return 'warning'
      return 'danger'
    }

    const refreshData = () => {
      ElMessage.success('数据已刷新')
    }

    const exportReport = () => {
      ElMessage.success('报告导出功能开发中')
    }

    const initCharts = () => {
      nextTick(() => {
        initReturnChart()
        initDrawdownChart()
        initAttributionCharts()
      })
    }

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
          data: ['策略收益', '基准收益', '超额收益']
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
          },
          {
            name: '超额收益',
            type: 'line',
            data: [0.6, 1.1, 0.6, 1.3, 0.8, 1.0, -0.3, 0.4, 0.6, 0.6, 1.1, 0.7],
            smooth: true,
            lineStyle: {
              color: '#52c41a'
            }
          }
        ]
      }
      
      returnChart.setOption(option)
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

    const initAttributionCharts = () => {
      // 收益归因图
      if (returnAttributionChartRef.value) {
        returnAttributionChart = echarts.init(returnAttributionChartRef.value)
        const returnOption = {
          tooltip: { trigger: 'item' },
          series: [{
            name: '收益归因',
            type: 'pie',
            radius: '60%',
            data: attributionData.value.map(item => ({
              name: item.factor,
              value: Math.abs(item.contribution)
            }))
          }]
        }
        returnAttributionChart.setOption(returnOption)
      }

      // 风险归因图
      if (riskAttributionChartRef.value) {
        riskAttributionChart = echarts.init(riskAttributionChartRef.value)
        const riskOption = {
          tooltip: { trigger: 'item' },
          series: [{
            name: '风险归因',
            type: 'pie',
            radius: '60%',
            data: [
              { name: '市场风险', value: 45 },
              { name: '行业风险', value: 25 },
              { name: '个股风险', value: 20 },
              { name: '其他风险', value: 10 }
            ]
          }]
        }
        riskAttributionChart.setOption(riskOption)
      }
    }

    onMounted(() => {
      setTimeout(() => {
        initCharts()
      }, 100)
    })

    return {
      selectedStrategy,
      timeRange,
      selectedBenchmark,
      analysisType,
      chartType,
      metrics,
      strategyList,
      attributionData,
      comparisonData,
      returnChartRef,
      drawdownChartRef,
      returnAttributionChartRef,
      riskAttributionChartRef,
      getRankTag,
      refreshData,
      exportReport
    }
  }
}
</script>

<style scoped>
.monitor-analysis {
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

.analysis-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.dimension-card {
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
      
      &.sharpe {
        background-color: #e6f7ff;
        color: #1890ff;
      }
      
      &.drawdown {
        background-color: #fff2e8;
        color: #fa8c16;
      }
      
      &.winrate {
        background-color: #f6ffed;
        color: #52c41a;
      }
    }
    
    .metric-info {
      flex: 1;
      
      .metric-number {
        font-size: 28px;
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
      
      .metric-change {
        font-size: 12px;
        
        &.positive {
          color: #52c41a;
        }
        
        &.negative {
          color: #f56c6c;
        }
      }
    }
  }
}

.chart-card,
.risk-card,
.attribution-card,
.comparison-card {
  margin-bottom: 20px;
}

.risk-row {
  margin-bottom: 20px;
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