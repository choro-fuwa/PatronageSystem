<template>
  <div class="monitor-alerts">
    <div class="page-header">
      <h1>预警管理</h1>
      <div class="header-actions">
        <el-button @click="refreshData">刷新</el-button>
        <el-button type="primary" @click="createAlert">新建预警</el-button>
      </div>
    </div>

    <div class="alerts-content">
      <!-- 预警统计 -->
      <el-row :gutter="20" class="stats-row">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon total">
                <el-icon><Warning /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ stats.totalAlerts }}</div>
                <div class="stat-label">总预警数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon active">
                <el-icon><CircleCheck /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ stats.activeAlerts }}</div>
                <div class="stat-label">活跃预警</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon high">
                <el-icon><Warning /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ stats.highPriorityAlerts }}</div>
                <div class="stat-label">高优先级</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon processed">
                <el-icon><Document /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ stats.processedAlerts }}</div>
                <div class="stat-label">已处理</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 预警列表 -->
      <el-card class="alerts-card">
        <template #header>
          <span>预警列表</span>
          <div style="float: right;">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索预警"
              style="width: 200px; margin-right: 10px;"
              clearable
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            <el-select v-model="filterLevel" placeholder="预警级别" style="width: 120px; margin-right: 10px;">
              <el-option label="全部" value="" />
              <el-option label="高" value="high" />
              <el-option label="中" value="medium" />
              <el-option label="低" value="low" />
            </el-select>
            <el-select v-model="filterStatus" placeholder="状态" style="width: 120px;">
              <el-option label="全部" value="" />
              <el-option label="活跃" value="active" />
              <el-option label="已处理" value="processed" />
              <el-option label="已忽略" value="ignored" />
            </el-select>
          </div>
        </template>
        
        <el-table :data="filteredAlerts" style="width: 100%">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="title" label="预警标题" width="200" />
          <el-table-column prop="strategyName" label="策略名称" width="150" />
          <el-table-column prop="level" label="级别" width="100">
            <template #default="scope">
              <el-tag :type="getLevelTag(scope.row.level)">
                {{ getLevelText(scope.row.level) }}
              </el-tag>
            </template>
          </el-table-column>
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
          <el-table-column prop="createTime" label="创建时间" width="150" />
          <el-table-column prop="lastTriggerTime" label="最后触发" width="150" />
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button type="text" @click="viewDetail(scope.row)">详情</el-button>
              <el-button type="text" @click="editAlert(scope.row)">编辑</el-button>
              <el-button 
                type="text" 
                :type="scope.row.status === 'active' ? 'warning' : 'success'"
                @click="toggleAlert(scope.row)"
              >
                {{ scope.row.status === 'active' ? '禁用' : '启用' }}
              </el-button>
              <el-button type="text" @click="deleteAlert(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="totalAlerts"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>

      <!-- 预警历史 -->
      <el-card class="history-card">
        <template #header>
          <span>预警历史</span>
        </template>
        
        <el-table :data="alertHistory" style="width: 100%">
          <el-table-column prop="alertTitle" label="预警标题" width="200" />
          <el-table-column prop="strategyName" label="策略名称" width="150" />
          <el-table-column prop="level" label="级别" width="100">
            <template #default="scope">
              <el-tag :type="getLevelTag(scope.row.level)">
                {{ getLevelText(scope.row.level) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="triggerTime" label="触发时间" width="150" />
          <el-table-column prop="processStatus" label="处理状态" width="100">
            <template #default="scope">
              <el-tag :type="getProcessStatusTag(scope.row.processStatus)">
                {{ getProcessStatusText(scope.row.processStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="processBy" label="处理人" width="120" />
          <el-table-column prop="processTime" label="处理时间" width="150" />
          <el-table-column prop="processRemark" label="处理备注" />
          <el-table-column label="操作" width="100">
            <template #default="scope">
              <el-button type="text" @click="viewHistoryDetail(scope.row)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

    <!-- 新建/编辑预警对话框 -->
    <el-dialog
      v-model="alertDialogVisible"
      :title="alertForm.id ? '编辑预警' : '新建预警'"
      width="600px"
    >
      <el-form
        ref="alertFormRef"
        :model="alertForm"
        :rules="alertRules"
        label-width="100px"
      >
        <el-form-item label="预警标题" prop="title">
          <el-input v-model="alertForm.title" placeholder="请输入预警标题" />
        </el-form-item>
        
        <el-form-item label="策略" prop="strategyId">
          <el-select v-model="alertForm.strategyId" placeholder="请选择策略" style="width: 100%">
            <el-option
              v-for="strategy in strategyList"
              :key="strategy.id"
              :label="strategy.name"
              :value="strategy.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="预警级别" prop="level">
          <el-select v-model="alertForm.level" placeholder="请选择预警级别" style="width: 100%">
            <el-option label="高" value="high" />
            <el-option label="中" value="medium" />
            <el-option label="低" value="low" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="预警类型" prop="type">
          <el-select v-model="alertForm.type" placeholder="请选择预警类型" style="width: 100%">
            <el-option label="回撤预警" value="drawdown" />
            <el-option label="波动率预警" value="volatility" />
            <el-option label="权重偏离预警" value="weight_deviation" />
            <el-option label="收益预警" value="return" />
            <el-option label="风险预警" value="risk" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="阈值" prop="threshold">
          <el-input-number
            v-model="alertForm.threshold"
            :min="0"
            :max="100"
            :step="0.1"
            style="width: 100%"
            placeholder="请输入阈值"
          />
        </el-form-item>
        
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="alertForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入预警描述"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="alertDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveAlert">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Warning, 
  CircleCheck, 
  Document, 
  Search 
} from '@element-plus/icons-vue'
import { 
  getAlertList, 
  getAlertHistoryList, 
  createAlert, 
  updateAlert, 
  deleteAlert, 
  toggleAlert 
} from '@/api/strategy'

export default {
  name: 'MonitorAlerts',
  setup() {
    const stats = reactive({
      totalAlerts: 25,
      activeAlerts: 8,
      highPriorityAlerts: 3,
      processedAlerts: 17
    })

    const searchKeyword = ref('')
    const filterLevel = ref('')
    const filterStatus = ref('')
    const currentPage = ref(1)
    const pageSize = ref(20)
    const totalAlerts = ref(25)

    const alertDialogVisible = ref(false)
    const alertFormRef = ref(null)
    const alertForm = reactive({
      id: null,
      title: '',
      strategyId: '',
      level: 'medium',
      type: 'drawdown',
      threshold: 10,
      description: ''
    })

    const alertRules = {
      title: [
        { required: true, message: '请输入预警标题', trigger: 'blur' }
      ],
      strategyId: [
        { required: true, message: '请选择策略', trigger: 'change' }
      ],
      level: [
        { required: true, message: '请选择预警级别', trigger: 'change' }
      ],
      type: [
        { required: true, message: '请选择预警类型', trigger: 'change' }
      ],
      threshold: [
        { required: true, message: '请输入阈值', trigger: 'blur' }
      ]
    }

    const alerts = ref([
      {
        id: 1,
        title: '回撤预警',
        strategyName: '稳健股债轮动策略',
        level: 'high',
        type: 'drawdown',
        status: 'active',
        createTime: '2024-01-15 10:00:00',
        lastTriggerTime: '2024-01-15 14:30:00'
      },
      {
        id: 2,
        title: '权重偏离预警',
        strategyName: 'FOF组合管理策略',
        level: 'medium',
        type: 'weight_deviation',
        status: 'active',
        createTime: '2024-01-14 09:00:00',
        lastTriggerTime: '2024-01-15 13:20:00'
      },
      {
        id: 3,
        title: '波动率预警',
        strategyName: '基金指数组合策略',
        level: 'low',
        type: 'volatility',
        status: 'processed',
        createTime: '2024-01-13 15:30:00',
        lastTriggerTime: '2024-01-14 11:45:00'
      }
    ])

    const alertHistory = ref([
      {
        id: 1,
        alertTitle: '回撤预警',
        strategyName: '稳健股债轮动策略',
        level: 'high',
        triggerTime: '2024-01-15 14:30:00',
        processStatus: 'processed',
        processBy: 'admin',
        processTime: '2024-01-15 15:00:00',
        processRemark: '已调整策略参数'
      },
      {
        id: 2,
        alertTitle: '权重偏离预警',
        strategyName: 'FOF组合管理策略',
        level: 'medium',
        triggerTime: '2024-01-15 13:20:00',
        processStatus: 'pending',
        processBy: '',
        processTime: '',
        processRemark: ''
      }
    ])

    const strategyList = ref([
      { id: 1, name: '稳健股债轮动策略' },
      { id: 2, name: 'FOF组合管理策略' },
      { id: 3, name: '基金指数组合策略' }
    ])

    const filteredAlerts = computed(() => {
      return alerts.value.filter(alert => {
        const matchKeyword = !searchKeyword.value || 
          alert.title.includes(searchKeyword.value) || 
          alert.strategyName.includes(searchKeyword.value)
        const matchLevel = !filterLevel.value || alert.level === filterLevel.value
        const matchStatus = !filterStatus.value || alert.status === filterStatus.value
        return matchKeyword && matchLevel && matchStatus
      })
    })

    const getLevelTag = (level) => {
      const levelMap = {
        'high': 'danger',
        'medium': 'warning',
        'low': 'success'
      }
      return levelMap[level] || 'default'
    }

    const getLevelText = (level) => {
      const levelMap = {
        'high': '高',
        'medium': '中',
        'low': '低'
      }
      return levelMap[level] || level
    }

    const getTypeTag = (type) => {
      const typeMap = {
        'drawdown': 'danger',
        'volatility': 'warning',
        'weight_deviation': 'info',
        'return': 'success',
        'risk': 'danger'
      }
      return typeMap[type] || 'default'
    }

    const getTypeText = (type) => {
      const typeMap = {
        'drawdown': '回撤预警',
        'volatility': '波动率预警',
        'weight_deviation': '权重偏离预警',
        'return': '收益预警',
        'risk': '风险预警'
      }
      return typeMap[type] || type
    }

    const getStatusTag = (status) => {
      const statusMap = {
        'active': 'success',
        'processed': 'info',
        'ignored': 'warning'
      }
      return statusMap[status] || 'default'
    }

    const getStatusText = (status) => {
      const statusMap = {
        'active': '活跃',
        'processed': '已处理',
        'ignored': '已忽略'
      }
      return statusMap[status] || status
    }

    const getProcessStatusTag = (status) => {
      const statusMap = {
        'processed': 'success',
        'pending': 'warning',
        'ignored': 'info'
      }
      return statusMap[status] || 'default'
    }

    const getProcessStatusText = (status) => {
      const statusMap = {
        'processed': '已处理',
        'pending': '待处理',
        'ignored': '已忽略'
      }
      return statusMap[status] || status
    }

    const refreshData = () => {
      ElMessage.success('数据已刷新')
    }

    const createAlert = () => {
      alertForm.id = null
      alertForm.title = ''
      alertForm.strategyId = ''
      alertForm.level = 'medium'
      alertForm.type = 'drawdown'
      alertForm.threshold = 10
      alertForm.description = ''
      alertDialogVisible.value = true
    }

    const editAlert = (alert) => {
      alertForm.id = alert.id
      alertForm.title = alert.title
      alertForm.strategyId = alert.strategyId
      alertForm.level = alert.level
      alertForm.type = alert.type
      alertForm.threshold = alert.threshold
      alertForm.description = alert.description
      alertDialogVisible.value = true
    }

    const saveAlert = () => {
      alertFormRef.value.validate((valid) => {
        if (valid) {
          const apiCall = alertForm.id ? updateAlert : createAlert
          apiCall(alertForm).then(response => {
            if (response.code === 200) {
              ElMessage.success(alertForm.id ? '预警更新成功' : '预警创建成功')
              alertDialogVisible.value = false
              refreshData()
            } else {
              ElMessage.error(response.message || '操作失败')
            }
          }).catch(error => {
            ElMessage.error('操作失败：' + error.message)
          })
        }
      })
    }

    const toggleAlert = (alert) => {
      const action = alert.status === 'active' ? '禁用' : '启用'
      ElMessageBox.confirm(`确定要${action}该预警吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        toggleAlert(alert.id, alert.status !== 'active').then(response => {
          if (response.code === 200) {
            ElMessage.success(`预警${action}成功`)
            refreshData()
          } else {
            ElMessage.error(response.message || '操作失败')
          }
        }).catch(error => {
          ElMessage.error('操作失败：' + error.message)
        })
      })
    }

    const deleteAlert = (alert) => {
      ElMessageBox.confirm('确定要删除该预警吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteAlert(alert.id).then(response => {
          if (response.code === 200) {
            ElMessage.success('预警删除成功')
            refreshData()
          } else {
            ElMessage.error(response.message || '删除失败')
          }
        }).catch(error => {
          ElMessage.error('删除失败：' + error.message)
        })
      })
    }

    const viewDetail = (alert) => {
      ElMessage.info('查看预警详情功能开发中')
    }

    const viewHistoryDetail = (history) => {
      ElMessage.info('查看历史详情功能开发中')
    }

    const handleSizeChange = (val) => {
      pageSize.value = val
      // 重新加载数据
    }

    const handleCurrentChange = (val) => {
      currentPage.value = val
      // 重新加载数据
    }

    onMounted(() => {
      // 加载数据
      refreshData()
    })

    return {
      stats,
      searchKeyword,
      filterLevel,
      filterStatus,
      currentPage,
      pageSize,
      totalAlerts,
      alertDialogVisible,
      alertFormRef,
      alertForm,
      alertRules,
      filteredAlerts,
      alertHistory,
      strategyList,
      getLevelTag,
      getLevelText,
      getTypeTag,
      getTypeText,
      getStatusTag,
      getStatusText,
      getProcessStatusTag,
      getProcessStatusText,
      refreshData,
      createAlert,
      editAlert,
      saveAlert,
      toggleAlert,
      deleteAlert,
      viewDetail,
      viewHistoryDetail,
      handleSizeChange,
      handleCurrentChange
    }
  }
}
</script>

<style scoped>
.monitor-alerts {
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

.alerts-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  .stat-content {
    display: flex;
    align-items: center;
    
    .stat-icon {
      width: 56px;
      height: 56px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;
      font-size: 24px;
      
      &.total {
        background-color: #fff2e8;
        color: #fa8c16;
      }
      
      &.active {
        background-color: #f6ffed;
        color: #52c41a;
      }
      
      &.high {
        background-color: #fff2f0;
        color: #ff4d4f;
      }
      
      &.processed {
        background-color: #e6f7ff;
        color: #1890ff;
      }
    }
    
    .stat-info {
      flex: 1;
      
      .stat-number {
        font-size: 28px;
        font-weight: bold;
        color: #1f2937;
        line-height: 1;
      }
      
      .stat-label {
        font-size: 14px;
        color: #6b7280;
        margin: 4px 0;
      }
    }
  }
}

.alerts-card,
.history-card {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  text-align: center;
}
</style> 