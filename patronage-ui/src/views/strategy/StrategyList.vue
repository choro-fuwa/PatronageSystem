<template>
  <div class="strategy-list">
    <div class="page-header">
      <h2>策略管理</h2>
      <div class="header-actions">
        <el-button type="primary" @click="showAddDialog = true">
          <el-icon><Plus /></el-icon>
          新增策略
        </el-button>
      </div>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-form :model="searchForm" inline>
        <el-form-item label="策略类型">
          <el-select v-model="searchForm.strategyType" placeholder="请选择策略类型" clearable>
            <el-option label="多因子" value="多因子" />
            <el-option label="单因子" value="单因子" />
            <el-option label="技术分析" value="技术分析" />
            <el-option label="基本面" value="基本面" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="searchForm.riskLevel" placeholder="请选择风险等级" clearable>
            <el-option label="低" value="低" />
            <el-option label="中" value="中" />
            <el-option label="高" value="高" />
          </el-select>
        </el-form-item>
        <el-form-item label="公开状态">
          <el-select v-model="searchForm.isPublic" placeholder="请选择公开状态" clearable>
            <el-option label="公开" :value="1" />
            <el-option label="私有" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchStrategies">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-cards" v-if="statsData">
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-value">{{ statsData.total_count || 0 }}</div>
          <div class="stat-label">总策略数</div>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-value">{{ statsData.active_count || 0 }}</div>
          <div class="stat-label">启用策略</div>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-value">{{ statsData.public_count || 0 }}</div>
          <div class="stat-label">公开策略</div>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-value">{{ statsData.strategy_type_count || 0 }}</div>
          <div class="stat-label">策略类型</div>
        </div>
      </el-card>
    </div>

    <!-- 策略列表 -->
    <div class="strategy-grid">
      <el-card
          v-for="strategy in strategies"
          :key="strategy.id"
          class="strategy-card"
          @click="viewStrategyDetail(strategy.id)"
      >
        <div class="strategy-header">
          <h3>{{ strategy.strategyName }}</h3>
          <div class="strategy-tags">
            <el-tag :type="getRiskLevelType(strategy.riskLevel)" size="small">
              {{ strategy.riskLevel }}
            </el-tag>
            <el-tag type="info" size="small">{{ strategy.strategyType }}</el-tag>
          </div>
        </div>
        <div class="strategy-info">
          <p class="strategy-code">代码：{{ strategy.strategyCode }}</p>
          <p class="strategy-portfolio">组合：{{ strategy.portfolioName || '未关联' }}</p>
          <p class="strategy-benchmark">基准：{{ strategy.benchmark }}</p>
          <p class="strategy-frequency">调仓：{{ strategy.rebalanceFrequency }}</p>
        </div>
        <div class="strategy-description">
          {{ strategy.description }}
        </div>
        <div class="strategy-targets">
          <div class="target-item">
            <span class="target-label">目标收益：</span>
            <span class="target-value">{{ formatPercent(strategy.targetReturn) }}</span>
          </div>
          <div class="target-item">
            <span class="target-label">最大回撤：</span>
            <span class="target-value">{{ formatPercent(strategy.maxDrawdown) }}</span>
          </div>
        </div>
        <div class="strategy-actions">
          <el-button size="small" @click.stop="editStrategy(strategy)">编辑</el-button>
          <el-button size="small" type="danger" @click.stop="deleteStrategy(strategy.id)">删除</el-button>
          <el-button size="small" @click.stop="showBacktest(strategy.id)">回测结果</el-button>
          <el-button size="small" @click.stop="showMonitor(strategy.id)">监控信息</el-button>
        </div>
      </el-card>
    </div>

    <!-- 新增/编辑策略对话框 -->
    <el-dialog
        v-model="showAddDialog"
        :title="editingStrategy ? '编辑策略' : '新增策略'"
        width="700px"
    >
      <el-form :model="strategyForm" :rules="strategyRules" ref="strategyFormRef" label-width="120px">
        <el-form-item label="策略代码" prop="strategyCode">
          <el-input v-model="strategyForm.strategyCode" placeholder="请输入策略代码" />
        </el-form-item>
        <el-form-item label="策略名称" prop="strategyName">
          <el-input v-model="strategyForm.strategyName" placeholder="请输入策略名称" />
        </el-form-item>
        <el-form-item label="策略类型" prop="strategyType">
          <el-select v-model="strategyForm.strategyType" placeholder="请选择策略类型">
            <el-option label="多因子" value="多因子" />
            <el-option label="单因子" value="单因子" />
            <el-option label="技术分析" value="技术分析" />
            <el-option label="基本面" value="基本面" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级" prop="riskLevel">
          <el-select v-model="strategyForm.riskLevel" placeholder="请选择风险等级">
            <el-option label="低" value="低" />
            <el-option label="中" value="中" />
            <el-option label="高" value="高" />
          </el-select>
        </el-form-item>
        <el-form-item label="目标收益率" prop="targetReturn">
          <el-input-number
              v-model="strategyForm.targetReturn"
              :precision="4"
              :step="0.01"
              :min="0"
              :max="1"
              placeholder="请输入目标收益率"
          />
        </el-form-item>
        <el-form-item label="最大回撤限制" prop="maxDrawdown">
          <el-input-number
              v-model="strategyForm.maxDrawdown"
              :precision="4"
              :step="0.01"
              :min="0"
              :max="1"
              placeholder="请输入最大回撤限制"
          />
        </el-form-item>
        <el-form-item label="调仓频率" prop="rebalanceFrequency">
          <el-select v-model="strategyForm.rebalanceFrequency" placeholder="请选择调仓频率">
            <el-option label="日" value="日" />
            <el-option label="周" value="周" />
            <el-option label="月" value="月" />
            <el-option label="季" value="季" />
          </el-select>
        </el-form-item>
        <el-form-item label="基准指数" prop="benchmark">
          <el-input v-model="strategyForm.benchmark" placeholder="请输入基准指数" />
        </el-form-item>
        <el-form-item label="策略描述" prop="description">
          <el-input
              v-model="strategyForm.description"
              type="textarea"
              :rows="3"
              placeholder="请输入策略描述"
          />
        </el-form-item>
        <el-form-item label="是否公开" prop="isPublic">
          <el-radio-group v-model="strategyForm.isPublic">
            <el-radio :label="1">公开</el-radio>
            <el-radio :label="0">私有</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="saveStrategy">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="showBacktestDialog" title="策略回测结果" width="900px">
      <el-table :data="backtestList">
        <el-table-column prop="backtestDate" label="回测日期" />
        <el-table-column prop="startDate" label="开始日期" />
        <el-table-column prop="endDate" label="结束日期" />
        <el-table-column prop="totalReturn" label="总收益率" />
        <el-table-column prop="maxDrawdown" label="最大回撤" />
        <el-table-column prop="sharpeRatio" label="夏普比率" />
      </el-table>
    </el-dialog>

    <el-dialog v-model="showMonitorDialog" title="策略监控信息" width="900px">
      <el-table :data="monitorList">
        <el-table-column prop="monitorDate" label="监控日期" />
        <el-table-column prop="currentReturn" label="当前收益率" />
        <el-table-column prop="currentDrawdown" label="当前回撤" />
        <el-table-column prop="status" label="监控状态" />
        <el-table-column prop="alertMessage" label="预警信息" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, TrendCharts, Monitor } from '@element-plus/icons-vue'
import { strategyApi } from '@/api/strategy'

const router = useRouter()

// 数据
const strategies = ref([])
const statsData = ref(null)
const showAddDialog = ref(false)
const editingStrategy = ref(null)
const strategyFormRef = ref()
const showBacktestDialog = ref(false)
const backtestList = ref([])
const showMonitorDialog = ref(false)
const monitorList = ref([])

// 搜索表单
const searchForm = reactive({
  strategyType: '',
  riskLevel: '',
  isPublic: null
})

// 策略表单
const strategyForm = reactive({
  strategyCode: '',
  strategyName: '',
  strategyType: '',
  riskLevel: '',
  targetReturn: null,
  maxDrawdown: null,
  rebalanceFrequency: '',
  benchmark: '',
  description: '',
  isPublic: 1
})

// 表单验证规则
const strategyRules = {
  strategyCode: [{ required: true, message: '请输入策略代码', trigger: 'blur' }],
  strategyName: [{ required: true, message: '请输入策略名称', trigger: 'blur' }],
  strategyType: [{ required: true, message: '请选择策略类型', trigger: 'change' }],
  riskLevel: [{ required: true, message: '请选择风险等级', trigger: 'change' }],
  targetReturn: [{ required: true, message: '请输入目标收益率', trigger: 'blur' }],
  maxDrawdown: [{ required: true, message: '请输入最大回撤限制', trigger: 'blur' }],
  rebalanceFrequency: [{ required: true, message: '请选择调仓频率', trigger: 'change' }],
  benchmark: [{ required: true, message: '请输入基准指数', trigger: 'blur' }]
}

// 获取策略列表
const getStrategies = async () => {
  try {
    const response = await strategyApi.getStrategies()
    if (response.success) {
      strategies.value = response.data
    }
  } catch (error) {
    ElMessage.error('获取策略列表失败')
  }
}

// 获取统计信息
const getStats = async () => {
  try {
    const response = await strategyApi.getStrategyStats()
    if (response.success) {
      statsData.value = response.data
    }
  } catch (error) {
    console.error('获取统计信息失败:', error)
  }
}

// 搜索策略
const searchStrategies = async () => {
  try {
    const response = await strategyApi.searchStrategies(searchForm)
    if (response.success) {
      strategies.value = response.data
    }
  } catch (error) {
    ElMessage.error('搜索策略失败')
  }
}

// 重置搜索
const resetSearch = () => {
  Object.assign(searchForm, {
    strategyType: '',
    riskLevel: '',
    isPublic: null
  })
  getStrategies()
}

// 查看策略详情
const viewStrategyDetail = (id) => {
  router.push(`/strategy/${id}`)
}

// 编辑策略
const editStrategy = (strategy) => {
  editingStrategy.value = strategy
  Object.assign(strategyForm, strategy)
  showAddDialog.value = true
}

// 删除策略
const deleteStrategy = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个策略吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const response = await strategyApi.deleteStrategy(id)
    if (response.success) {
      ElMessage.success('删除成功')
      getStrategies()
      getStats()
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 保存策略
const saveStrategy = async () => {
  try {
    await strategyFormRef.value.validate()

    const api = editingStrategy.value ? strategyApi.updateStrategy : strategyApi.addStrategy
    const response = await api(strategyForm)

    if (response.success) {
      ElMessage.success(editingStrategy.value ? '更新成功' : '新增成功')
      showAddDialog.value = false
      resetForm()
      getStrategies()
      getStats()
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

// 重置表单
const resetForm = () => {
  editingStrategy.value = null
  Object.assign(strategyForm, {
    strategyCode: '',
    strategyName: '',
    strategyType: '',
    riskLevel: '',
    targetReturn: null,
    maxDrawdown: null,
    rebalanceFrequency: '',
    benchmark: '',
    description: '',
    isPublic: 1
  })
}

// 获取风险等级标签类型
const getRiskLevelType = (riskLevel) => {
  const types = {
    '低': 'success',
    '中': 'warning',
    '高': 'danger'
  }
  return types[riskLevel] || 'info'
}

// 格式化百分比
const formatPercent = (value) => {
  if (value == null) return '-'
  return (value * 100).toFixed(2) + '%'
}

// 显示回测结果
const showBacktest = (strategyId) => {
  strategyApi.getStrategyBacktests(strategyId).then(res => {
    backtestList.value = res.data
    showBacktestDialog.value = true
  })
}

// 显示监控信息
const showMonitor = (strategyId) => {
  strategyApi.getStrategyMonitors(strategyId).then(res => {
    monitorList.value = res.data
    showMonitorDialog.value = true
  })
}

// 初始化
onMounted(() => {
  getStrategies()
  getStats()
})
</script>

<style scoped>
.strategy-list {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #303133;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.search-bar {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  text-align: center;
}

.stat-content {
  padding: 10px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 5px;
}

.stat-label {
  color: #606266;
  font-size: 14px;
}

.strategy-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
}

.strategy-card {
  cursor: pointer;
  transition: all 0.3s;
}

.strategy-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.strategy-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 10px;
}

.strategy-header h3 {
  margin: 0;
  color: #303133;
  flex: 1;
}

.strategy-tags {
  display: flex;
  gap: 5px;
}

.strategy-info {
  margin-bottom: 10px;
}

.strategy-info p {
  margin: 5px 0;
  color: #606266;
  font-size: 14px;
}

.strategy-description {
  color: #909399;
  font-size: 14px;
  line-height: 1.5;
  margin-bottom: 15px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.strategy-targets {
  margin-bottom: 15px;
}

.target-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
}

.target-label {
  color: #606266;
  font-size: 14px;
}

.target-value {
  color: #409eff;
  font-weight: bold;
  font-size: 14px;
}

.strategy-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}
</style>