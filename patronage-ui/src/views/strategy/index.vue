<template>
  <div class="strategy-container">
    <!-- 页面标题和操作按钮 -->
    <div class="page-header">
      <div class="header-left">
          <h2>策略管理</h2>
        <p class="subtitle">管理和监控您的投资策略</p>
      </div>
      <div class="header-right">
        <el-button type="primary" @click="handleCreate" :icon="Plus">
          创建策略
        </el-button>
        <el-button @click="handleImport" :icon="Upload">
          导入策略
        </el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon running">
                <el-icon><VideoPlay /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ stats.running }}</div>
                <div class="stat-label">运行中</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon total">
                <el-icon><TrendCharts /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ stats.total }}</div>
                <div class="stat-label">总策略</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon profit">
                <el-icon><TrendCharts /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ stats.totalReturn }}%</div>
                <div class="stat-label">总收益率</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon risk">
                <el-icon><Warning /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ stats.maxDrawdown }}%</div>
                <div class="stat-label">最大回撤</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
        </div>

    <!-- 搜索栏 -->
    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="策略名称">
          <el-input
            v-model="searchForm.strategyName"
            placeholder="请输入策略名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="策略类型">
          <el-select
            v-model="searchForm.strategyType"
            placeholder="请选择策略类型"
            clearable
            style="width: 150px"
          >
            <el-option label="趋势策略" :value="1" />
            <el-option label="均值回归" :value="2" />
            <el-option label="套利策略" :value="3" />
            <el-option label="量化策略" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="策略状态">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="草稿" :value="0" />
            <el-option label="运行中" :value="1" />
            <el-option label="暂停" :value="2" />
            <el-option label="已停止" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作栏 -->
    <el-card class="action-card">
      <el-button type="primary" @click="handleCreate">
        <el-icon><Plus /></el-icon>
        创建策略
      </el-button>
      <el-button @click="handleRefresh">
        <el-icon><Refresh /></el-icon>
        刷新
      </el-button>
    </el-card>

    <!-- 策略列表 -->
    <el-card class="list-card">
      <el-table
        v-loading="loading"
        :data="strategyList"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="strategyName" label="策略名称" min-width="50">
          <template #default="{ row }">
            <el-link type="primary" @click="handleView(row)">{{ row.strategyName }}</el-link>
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
        <el-table-column prop="initialCapital" label="初始资金" width="120">
          <template #default="{ row }">
            {{ formatCurrency(row.initialCapital) }}
          </template>
        </el-table-column>
        <el-table-column prop="currentCapital" label="当前资金" width="120">
          <template #default="{ row }">
            {{ formatCurrency(row.currentCapital) }}
      </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="360" fixed="right">
          <template #default="{ row }">
            <div style="display: flex; gap: 8px; flex-wrap: wrap;">
              <el-button size="small" @click="handleView(row)">查看</el-button>
              <el-button size="small" type="primary" @click="handleEdit(row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
              <el-button
                v-if="row.status === 0"
                size="small"
                type="success"
                @click="handleStart(row)"
              >启动</el-button>
              <el-button
                v-if="row.status === 1"
                size="small"
                type="warning"
                @click="handlePause(row)"
              >暂停</el-button>
              <el-button
                v-if="row.status === 1 || row.status === 2"
                size="small"
                type="text"
                @click="handleStop(row)"
              >停止</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.pageNum"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 创建/编辑策略对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="策略名称" prop="strategyName">
          <el-input v-model="form.strategyName" placeholder="请输入策略名称" />
        </el-form-item>
        <el-form-item label="策略描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入策略描述"
          />
        </el-form-item>
        <el-form-item label="策略类型" prop="strategyType">
          <el-select v-model="form.strategyType" placeholder="请选择策略类型" style="width: 100%">
            <el-option label="趋势策略" :value="1" />
            <el-option label="均值回归" :value="2" />
            <el-option label="套利策略" :value="3" />
            <el-option label="量化策略" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="初始资金" prop="initialCapital">
          <el-input-number
            v-model="form.initialCapital"
            :min="0"
            :precision="2"
            style="width: 100%"
            placeholder="请输入初始资金"
          />
        </el-form-item>
        <el-form-item label="策略参数" prop="parameters">
          <el-input
            v-model="form.parameters"
            type="textarea"
            :rows="4"
            placeholder="请输入策略参数（JSON格式）"
          />
        </el-form-item>
        <el-form-item label="风险控制" prop="riskControl">
          <el-input
            v-model="form.riskControl"
            type="textarea"
            :rows="4"
            placeholder="请输入风险控制参数（JSON格式）"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Plus,
  Upload,
  Search,
  Refresh,
  Download,
  ArrowDown,
  VideoPlay,
  TrendCharts,
  Warning
} from '@element-plus/icons-vue'
import { getStrategyList, deleteStrategy } from '@/api/strategy'
import dayjs from 'dayjs'

const router = useRouter()
const loading = ref(false)
const strategyList = ref([])
const selectedRows = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref()

// 统计数据
const stats = reactive({
  running: 12,
  total: 45,
  totalReturn: 15.8,
  maxDrawdown: -8.2
})

// 搜索表单
const searchForm = reactive({
  strategyName: '',
  strategyType: null,
  status: null
})

// 分页
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 表单数据
const form = reactive({
  id: null,
  strategyName: '',
  description: '',
  strategyType: null,
  initialCapital: null,
  currentCapital: null,
  parameters: '',
  riskControl: ''
})

// 表单验证规则
const rules = {
  strategyName: [
    { required: true, message: '请输入策略名称', trigger: 'blur' }
  ],
  strategyType: [
    { required: true, message: '请选择策略类型', trigger: 'change' }
  ],
  initialCapital: [
    { required: true, message: '请输入初始资金', trigger: 'blur' }
  ]
}

// 获取策略列表
const fetchStrategyList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    const response = await getStrategyList(params)
    strategyList.value = response.records
    pagination.total = response.total
  } catch (error) {
    ElMessage.error('获取策略列表失败')
  } finally {
    loading.value = false
  }
}

// 获取状态对应的标签类型
const getStatusType = (status) => {
  const statusMap = {
    'running': 'success',
    'stopped': 'info',
    'backtesting': 'warning',
    'error': 'danger'
  }
  return statusMap[status] || 'info'
}

// 获取状态标签文本
const getStatusLabel = (status) => {
  const statusMap = {
    'running': '运行中',
    'stopped': '已停止',
    'backtesting': '回测中',
    'error': '异常'
  }
  return statusMap[status] || status
}

// 获取类型对应的标签类型
const getTypeTagType = (type) => {
  const typeMap = {
    'asset_allocation': 'primary',
    'fof_portfolio': 'success',
    'fund_index': 'warning',
    'timing': 'danger'
  }
  return typeMap[type] || 'info'
}

// 获取类型标签文本
const getTypeLabel = (type) => {
  const typeMap = {
    'asset_allocation': '大类资产配置',
    'fof_portfolio': 'FOF组合管理',
    'fund_index': '基金指数组合',
    'timing': '择时组合'
  }
  return typeMap[type] || type
}

// 格式化日期
const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleString('zh-CN')
}

// 创建新策略
const handleCreate = () => {
  dialogTitle.value = '创建策略'
  dialogVisible.value = true
  resetForm()
}

// 导入策略
const handleImport = () => {
  ElMessage.info('导入功能开发中')
}

// 搜索
const handleSearch = () => {
  pagination.pageNum = 1
  fetchStrategyList()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, {
    strategyName: '',
    strategyType: null,
    status: null
  })
  pagination.pageNum = 1
  fetchStrategyList()
}

// 刷新
const handleRefresh = () => {
  fetchStrategyList()
}

// 导出
const handleExport = () => {
  ElMessage.info('导出功能开发中')
}

// 查看监控
const handleMonitor = (strategy) => {
  router.push(`/strategy/monitor/${strategy.id}`)
}

// 执行回测
const handleBacktest = (strategy) => {
  router.push(`/strategy/backtest/${strategy.id}`)
}

// 策略调整
const handleAdjust = (strategy) => {
  router.push(`/strategy/adjust/${strategy.id}`)
}

// 查看策略
const handleView = (row) => {
  // TODO: 跳转到策略详情页
  console.log('查看策略:', row)
}

// 编辑策略
const handleEdit = (row) => {
  dialogTitle.value = '编辑策略'
  dialogVisible.value = true
  Object.assign(form, row)
}

// 启动策略
const handleStart = async (row) => {
  try {
    const res = await strategyApi.startStrategy(row.id)
    if (res.success) {
      ElMessage.success('策略启动成功')
      fetchStrategyList()
    } else {
      ElMessage.error(res.message || '策略启动失败')
    }
  } catch (error) {
    console.error('启动策略失败:', error)
    ElMessage.error('策略启动失败')
  }
}

// 暂停策略
const handlePause = async (row) => {
  try {
    const res = await strategyApi.pauseStrategy(row.id)
    if (res.success) {
      ElMessage.success('策略暂停成功')
      fetchStrategyList()
    } else {
      ElMessage.error(res.message || '策略暂停失败')
    }
  } catch (error) {
    console.error('暂停策略失败:', error)
    ElMessage.error('策略暂停失败')
  }
}

// 停止策略
const handleStop = async (row) => {
  try {
    const res = await strategyApi.stopStrategy(row.id)
    if (res.success) {
      ElMessage.success('策略停止成功')
      fetchStrategyList()
    } else {
      ElMessage.error(res.message || '策略停止失败')
    }
  } catch (error) {
    console.error('停止策略失败:', error)
    ElMessage.error('策略停止失败')
  }
}

// 删除策略
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该策略吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
    })
    
    const res = await strategyApi.deleteStrategy(row.id)
    if (res.success) {
      ElMessage.success('策略删除成功')
      fetchStrategyList()
    } else {
      ElMessage.error(res.message || '策略删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除策略失败:', error)
      ElMessage.error('策略删除失败')
    }
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    const api = form.id ? strategyApi.updateStrategy : strategyApi.createStrategy
    const res = await api(form)
    
    if (res.success) {
      ElMessage.success(form.id ? '策略更新成功' : '策略创建成功')
      dialogVisible.value = false
      fetchStrategyList()
    } else {
      ElMessage.error(res.message || (form.id ? '策略更新失败' : '策略创建失败'))
    }
  } catch (error) {
    console.error('提交表单失败:', error)
    ElMessage.error('提交失败')
  }
}

// 重置表单
const resetForm = () => {
  Object.assign(form, {
    id: null,
    strategyName: '',
    description: '',
    strategyType: null,
    initialCapital: null,
    currentCapital: null,
    parameters: '',
    riskControl: ''
  })
  formRef.value?.resetFields()
}

// 对话框关闭
const handleDialogClose = () => {
  resetForm()
}

// 选择变化
const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.pageSize = size
  pagination.pageNum = 1
  fetchStrategyList()
}

// 当前页变化
const handleCurrentChange = (page) => {
  pagination.pageNum = page
  fetchStrategyList()
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

const formatCurrency = (value) => {
  return new Intl.NumberFormat('zh-CN', {
    style: 'currency',
    currency: 'CNY'
  }).format(value)
}

const formatDateTime = (value) => {
  return dayjs(value).format('YYYY-MM-DD HH:mm:ss')
}

onMounted(() => {
  fetchStrategyList()
})
</script>

<style scoped lang="scss">
.strategy-container {
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
  
  .stats-cards {
    margin-bottom: 24px;
    
    .stat-card {
      .stat-content {
        display: flex;
        align-items: center;
        
        .stat-icon {
          width: 48px;
          height: 48px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          
          &.running {
            background-color: #e6f7ff;
            color: #1890ff;
          }
          
          &.total {
            background-color: #f6ffed;
            color: #52c41a;
          }
          
          &.profit {
            background-color: #fff7e6;
            color: #fa8c16;
          }
          
          &.risk {
            background-color: #fff2f0;
            color: #ff4d4f;
          }
        }
        
        .stat-info {
          .stat-number {
            font-size: 24px;
            font-weight: 600;
            color: #1f2937;
            line-height: 1;
          }
          
          .stat-label {
            font-size: 14px;
            color: #6b7280;
            margin-top: 4px;
          }
        }
      }
    }
  }
  
  .search-card {
    margin-bottom: 20px;
  }
  
  .action-card {
    margin-bottom: 20px;
  }
  
  .list-card {
    margin-bottom: 20px;
  }
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
  
  .dialog-footer {
    text-align: right;
  }
}
</style>