<template>
  <div class="strategy-adjust">
    <div class="page-header">
      <div class="header-content">
        <div class="back-button" @click="goBack">
          <el-icon><ArrowLeft /></el-icon>
          返回
        </div>
        <h1>策略调整</h1>
        <div class="header-actions">
          <el-button @click="resetAdjustment">重置</el-button>
          <el-button type="primary" @click="saveAdjustment">保存调整</el-button>
        </div>
      </div>
    </div>

    <div class="adjust-content">
      <!-- 策略信息 -->
      <el-card class="strategy-info-card">
        <template #header>
          <span>策略信息</span>
        </template>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="策略名称">{{ strategy.name }}</el-descriptions-item>
          <el-descriptions-item label="策略类型">{{ getTypeText(strategy.type) }}</el-descriptions-item>
          <el-descriptions-item label="当前状态">{{ getStatusText(strategy.status) }}</el-descriptions-item>
          <el-descriptions-item label="当前资金">¥{{ formatCurrency(strategy.currentCapital) }}</el-descriptions-item>
          <el-descriptions-item label="累计收益">{{ strategy.totalReturn >= 0 ? '+' : '' }}{{ strategy.totalReturn }}%</el-descriptions-item>
          <el-descriptions-item label="最大回撤">{{ strategy.maxDrawdown }}%</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 当前持仓 -->
      <el-card class="positions-card">
        <template #header>
          <span>当前持仓</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="refreshPositions">
            刷新
          </el-button>
        </template>
        
        <el-table :data="currentPositions" style="width: 100%">
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
          <el-table-column prop="currentWeight" label="当前权重" width="120">
            <template #default="scope">
              {{ scope.row.currentWeight }}%
            </template>
          </el-table-column>
          <el-table-column prop="targetWeight" label="目标权重" width="120">
            <template #default="scope">
              {{ scope.row.targetWeight }}%
            </template>
          </el-table-column>
          <el-table-column prop="weightDeviation" label="权重偏离" width="120">
            <template #default="scope">
              <span :class="scope.row.weightDeviation >= 0 ? 'positive' : 'negative'">
                {{ scope.row.weightDeviation >= 0 ? '+' : '' }}{{ scope.row.weightDeviation }}%
              </span>
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
        </el-table>
      </el-card>

      <!-- 权重调整 -->
      <el-card class="adjustment-card">
        <template #header>
          <span>权重调整</span>
        </template>
        
        <el-table :data="adjustmentPositions" style="width: 100%">
          <el-table-column prop="symbol" label="标的代码" width="120" />
          <el-table-column prop="symbolName" label="标的名称" width="150" />
          <el-table-column prop="currentWeight" label="当前权重" width="120">
            <template #default="scope">
              {{ scope.row.currentWeight }}%
            </template>
          </el-table-column>
          <el-table-column label="调整后权重" width="150">
            <template #default="scope">
              <el-input-number
                v-model="scope.row.adjustedWeight"
                :min="0"
                :max="100"
                :step="0.1"
                style="width: 100%"
                placeholder="权重(%)"
                @change="calculateWeightChange(scope.$index)"
              />
            </template>
          </el-table-column>
          <el-table-column label="权重变化" width="120">
            <template #default="scope">
              <span :class="scope.row.weightChange >= 0 ? 'positive' : 'negative'">
                {{ scope.row.weightChange >= 0 ? '+' : '' }}{{ scope.row.weightChange }}%
              </span>
            </template>
          </el-table-column>
          <el-table-column label="调整金额" width="120">
            <template #default="scope">
              <span :class="scope.row.adjustmentAmount >= 0 ? 'positive' : 'negative'">
                {{ scope.row.adjustmentAmount >= 0 ? '+' : '' }}¥{{ formatCurrency(scope.row.adjustmentAmount) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template #default="scope">
              <el-button type="danger" size="small" @click="removePosition(scope.$index)">
                移除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="weight-summary">
          <span>权重总和: {{ totalAdjustedWeight }}%</span>
          <span v-if="totalAdjustedWeight !== 100" class="weight-warning">
            (权重总和应为100%)
          </span>
        </div>
      </el-card>

      <!-- 新增资产 -->
      <el-card class="new-assets-card">
        <template #header>
          <span>新增资产</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="addNewAsset">
            添加资产
          </el-button>
        </template>
        
        <el-table :data="newAssets" style="width: 100%">
          <el-table-column label="标的代码" width="150">
            <template #default="scope">
              <el-input v-model="scope.row.symbol" placeholder="标的代码" />
            </template>
          </el-table-column>
          <el-table-column label="标的名称" width="200">
            <template #default="scope">
              <el-input v-model="scope.row.symbolName" placeholder="标的名称" />
            </template>
          </el-table-column>
          <el-table-column label="标的类型" width="120">
            <template #default="scope">
              <el-select v-model="scope.row.symbolType" placeholder="类型" style="width: 100%">
                <el-option label="股票" value="STOCK" />
                <el-option label="债券" value="BOND" />
                <el-option label="基金" value="FUND" />
                <el-option label="ETF" value="ETF" />
                <el-option label="黄金" value="GOLD" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="目标权重" width="120">
            <template #default="scope">
              <el-input-number
                v-model="scope.row.targetWeight"
                :min="0"
                :max="100"
                :step="0.1"
                style="width: 100%"
                placeholder="权重(%)"
              />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template #default="scope">
              <el-button type="danger" size="small" @click="removeNewAsset(scope.$index)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 调整预览 -->
      <el-card class="preview-card">
        <template #header>
          <span>调整预览</span>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <h4>调整前</h4>
            <div class="chart-container" ref="beforeChartRef"></div>
          </el-col>
          <el-col :span="12">
            <h4>调整后</h4>
            <div class="chart-container" ref="afterChartRef"></div>
          </el-col>
        </el-row>

        <div class="adjustment-summary">
          <h4>调整摘要</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="总调整金额">
              ¥{{ formatCurrency(totalAdjustmentAmount) }}
            </el-descriptions-item>
            <el-descriptions-item label="调整资产数量">
              {{ adjustmentPositions.length + newAssets.length }}
            </el-descriptions-item>
            <el-descriptions-item label="预计交易成本">
              ¥{{ formatCurrency(estimatedTradingCost) }}
            </el-descriptions-item>
            <el-descriptions-item label="调整原因">
              <el-input v-model="adjustmentReason" placeholder="请输入调整原因" />
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { getStrategyDetail, adjustStrategy } from '@/api/strategy'

export default {
  name: 'StrategyAdjust',
  setup() {
    const route = useRoute()
    const router = useRouter()
    const strategyId = route.params.id

    const strategy = reactive({
      id: strategyId,
      name: '稳健股债轮动策略',
      type: 'ASSET_ALLOCATION',
      status: 'ACTIVE',
      currentCapital: 1150000,
      totalReturn: 15.6,
      maxDrawdown: -8.3
    })

    const currentPositions = ref([
      {
        symbol: '000300.SH',
        symbolName: '沪深300ETF',
        symbolType: 'ETF',
        quantity: 10000,
        currentWeight: 58.5,
        targetWeight: 60,
        weightDeviation: -1.5,
        marketValue: 412000,
        unrealizedPnl: 27000
      },
      {
        symbol: '019547.SH',
        symbolName: '国债ETF',
        symbolType: 'ETF',
        quantity: 5000,
        currentWeight: 41.5,
        targetWeight: 40,
        weightDeviation: 1.5,
        marketValue: 519000,
        unrealizedPnl: 6500
      }
    ])

    const adjustmentPositions = ref([])
    const newAssets = ref([])
    const adjustmentReason = ref('')

    const beforeChartRef = ref(null)
    const afterChartRef = ref(null)
    let beforeChart = null
    let afterChart = null

    const totalAdjustedWeight = computed(() => {
      const positionWeight = adjustmentPositions.value.reduce((sum, pos) => sum + (pos.adjustedWeight || 0), 0)
      const newAssetWeight = newAssets.value.reduce((sum, asset) => sum + (asset.targetWeight || 0), 0)
      return positionWeight + newAssetWeight
    })

    const totalAdjustmentAmount = computed(() => {
      return adjustmentPositions.value.reduce((sum, pos) => sum + (pos.adjustmentAmount || 0), 0)
    })

    const estimatedTradingCost = computed(() => {
      return Math.abs(totalAdjustmentAmount.value) * 0.001 // 假设交易成本为0.1%
    })

    const getTypeText = (type) => {
      const typeMap = {
        'ASSET_ALLOCATION': '大类资产配置',
        'FOF': 'FOF组合',
        'INDEX_COPY': '基金指数组合',
        'TIMING': '择时策略'
      }
      return typeMap[type] || type
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

    const formatCurrency = (value) => {
      return new Intl.NumberFormat('zh-CN', {
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
      }).format(value)
    }

    const goBack = () => {
      router.back()
    }

    const refreshPositions = () => {
      ElMessage.success('持仓数据已刷新')
    }

    const calculateWeightChange = (index) => {
      const position = adjustmentPositions.value[index]
      if (position.adjustedWeight !== undefined) {
        position.weightChange = position.adjustedWeight - position.currentWeight
        position.adjustmentAmount = (position.weightChange / 100) * strategy.currentCapital
      }
    }

    const removePosition = (index) => {
      adjustmentPositions.value.splice(index, 1)
    }

    const addNewAsset = () => {
      newAssets.value.push({
        symbol: '',
        symbolName: '',
        symbolType: 'ETF',
        targetWeight: 0
      })
    }

    const removeNewAsset = (index) => {
      newAssets.value.splice(index, 1)
    }

    const resetAdjustment = () => {
      ElMessageBox.confirm('确定要重置所有调整吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        adjustmentPositions.value = []
        newAssets.value = []
        adjustmentReason.value = ''
        ElMessage.success('调整已重置')
      })
    }

    const saveAdjustment = () => {
      if (totalAdjustedWeight.value !== 100) {
        ElMessage.warning('权重总和必须为100%')
        return
      }

      if (!adjustmentReason.value.trim()) {
        ElMessage.warning('请输入调整原因')
        return
      }

      const adjustmentData = {
        strategyId: strategyId,
        reason: adjustmentReason.value,
        positions: adjustmentPositions.value.map(pos => ({
          symbol: pos.symbol,
          targetWeight: pos.adjustedWeight
        })),
        newAssets: newAssets.value
      }

      adjustStrategy(adjustmentData).then(response => {
        if (response.code === 200) {
          ElMessage.success('策略调整成功')
          router.push(`/strategy/detail/${strategyId}`)
        } else {
          ElMessage.error(response.message || '调整失败')
        }
      }).catch(error => {
        ElMessage.error('调整失败：' + error.message)
      })
    }

    const initCharts = () => {
      nextTick(() => {
        // 初始化调整前图表
        if (beforeChartRef.value) {
          beforeChart = echarts.init(beforeChartRef.value)
          const beforeOption = {
            tooltip: { trigger: 'item' },
            series: [{
              name: '当前配置',
              type: 'pie',
              radius: '60%',
              data: currentPositions.value.map(pos => ({
                name: pos.symbolName,
                value: pos.currentWeight
              }))
            }]
          }
          beforeChart.setOption(beforeOption)
        }

        // 初始化调整后图表
        if (afterChartRef.value) {
          afterChart = echarts.init(afterChartRef.value)
          const afterOption = {
            tooltip: { trigger: 'item' },
            series: [{
              name: '调整后配置',
              type: 'pie',
              radius: '60%',
              data: []
            }]
          }
          afterChart.setOption(afterOption)
        }
      })
    }

    const updateAfterChart = () => {
      if (afterChart) {
        const data = []
        
        // 添加调整后的持仓
        adjustmentPositions.value.forEach(pos => {
          if (pos.adjustedWeight > 0) {
            data.push({
              name: pos.symbolName,
              value: pos.adjustedWeight
            })
          }
        })
        
        // 添加新资产
        newAssets.value.forEach(asset => {
          if (asset.targetWeight > 0) {
            data.push({
              name: asset.symbolName || asset.symbol,
              value: asset.targetWeight
            })
          }
        })
        
        afterChart.setOption({
          series: [{
            data: data
          }]
        })
      }
    }

    onMounted(() => {
      // 加载策略详情
      getStrategyDetail(strategyId).then(response => {
        if (response.code === 200) {
          Object.assign(strategy, response.data)
          
          // 初始化调整持仓
          adjustmentPositions.value = currentPositions.value.map(pos => ({
            ...pos,
            adjustedWeight: pos.currentWeight,
            weightChange: 0,
            adjustmentAmount: 0
          }))
        }
      })

      // 初始化图表
      setTimeout(() => {
        initCharts()
      }, 100)
    })

    // 监听调整变化，更新图表
    const watchAdjustments = () => {
      updateAfterChart()
    }

    return {
      strategy,
      currentPositions,
      adjustmentPositions,
      newAssets,
      adjustmentReason,
      beforeChartRef,
      afterChartRef,
      totalAdjustedWeight,
      totalAdjustmentAmount,
      estimatedTradingCost,
      getTypeText,
      getStatusText,
      getSymbolTypeTag,
      getSymbolTypeText,
      formatCurrency,
      goBack,
      refreshPositions,
      calculateWeightChange,
      removePosition,
      addNewAsset,
      removeNewAsset,
      resetAdjustment,
      saveAdjustment
    }
  }
}
</script>

<style scoped>
.strategy-adjust {
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

.adjust-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.strategy-info-card,
.positions-card,
.adjustment-card,
.new-assets-card,
.preview-card {
  margin-bottom: 20px;
}

.weight-summary {
  margin-top: 15px;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 4px;
  font-size: 14px;
}

.weight-warning {
  color: #f56c6c;
  margin-left: 10px;
}

.chart-container {
  height: 300px;
}

.adjustment-summary {
  margin-top: 20px;
}

.adjustment-summary h4 {
  margin: 0 0 15px 0;
  color: #303133;
}

.positive {
  color: #67c23a;
}

.negative {
  color: #f56c6c;
}
</style> 