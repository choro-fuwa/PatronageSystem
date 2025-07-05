<template>
  <div class="account-list">
    <div class="page-header">
      <h2>交易账户管理</h2>
      <el-button type="primary" @click="showCreateDialog = true">
        <el-icon><Plus /></el-icon>
        新增账户
      </el-button>
    </div>

    <el-card>
      <div class="search-bar">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="账户状态">
            <el-select v-model="searchForm.status" placeholder="选择状态" clearable>
              <el-option label="正常" value="ACTIVE" />
              <el-option label="暂停" value="SUSPENDED" />
              <el-option label="关闭" value="CLOSED" />
            </el-select>
          </el-form-item>
          <el-form-item label="账户类型">
            <el-select v-model="searchForm.accountType" placeholder="选择类型" clearable>
              <el-option label="个人" value="个人" />
              <el-option label="机构" value="机构" />
              <el-option label="产品" value="产品" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table :data="accountList" v-loading="loading" stripe>
        <el-table-column prop="accountCode" label="账户代码" width="120" />
        <el-table-column prop="accountName" label="账户名称" width="150" />
        <el-table-column prop="accountType" label="账户类型" width="100" />
        <el-table-column prop="broker" label="券商" width="120" />
        <el-table-column prop="accountStatus" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.accountStatus)">
              {{ getStatusText(row.accountStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalAssets" label="总资产" width="120">
          <template #default="{ row }">
            {{ formatCurrency(row.totalAssets) }}
          </template>
        </el-table-column>
        <el-table-column prop="availableCash" label="可用资金" width="120">
          <template #default="{ row }">
            {{ formatCurrency(row.availableCash) }}
          </template>
        </el-table-column>
        <el-table-column prop="marketValue" label="市值" width="120">
          <template #default="{ row }">
            {{ formatCurrency(row.marketValue) }}
          </template>
        </el-table-column>
        <el-table-column prop="totalPnl" label="总盈亏" width="120">
          <template #default="{ row }">
            <span :class="{ 'text-success': row.totalPnl > 0, 'text-danger': row.totalPnl < 0 }">
              {{ formatCurrency(row.totalPnl) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
            <el-dropdown @command="handleStatusChange">
              <el-button size="small">
                状态<el-icon class="el-icon--right"><arrow-down /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item :command="{ id: row.id, status: 'ACTIVE' }">设为正常</el-dropdown-item>
                  <el-dropdown-item :command="{ id: row.id, status: 'SUSPENDED' }">设为暂停</el-dropdown-item>
                  <el-dropdown-item :command="{ id: row.id, status: 'CLOSED' }">设为关闭</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="showCreateDialog"
      width="600px"
      @close="resetForm"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="账户代码" prop="accountCode">
          <el-input v-model="form.accountCode" placeholder="请输入账户代码" />
        </el-form-item>
        <el-form-item label="账户名称" prop="accountName">
          <el-input v-model="form.accountName" placeholder="请输入账户名称" />
        </el-form-item>
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="账户类型" prop="accountType">
          <el-select v-model="form.accountType" placeholder="选择账户类型">
            <el-option label="个人" value="个人" />
            <el-option label="机构" value="机构" />
            <el-option label="产品" value="产品" />
          </el-select>
        </el-form-item>
        <el-form-item label="券商" prop="broker">
          <el-input v-model="form.broker" placeholder="请输入券商名称" />
        </el-form-item>
        <el-form-item label="账户状态" prop="accountStatus">
          <el-select v-model="form.accountStatus" placeholder="选择账户状态">
            <el-option label="正常" value="ACTIVE" />
            <el-option label="暂停" value="SUSPENDED" />
            <el-option label="关闭" value="CLOSED" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, ArrowDown } from '@element-plus/icons-vue'
import {
  searchTradeAccounts,
  getTradeAccounts,
  createTradeAccount,
  updateTradeAccount,
  deleteTradeAccount,
  updateTradeAccountStatus
} from '@/api/trade'

const loading = ref(false)
const accountList = ref([])
const showCreateDialog = ref(false)
const isEdit = ref(false)
const formRef = ref()

const searchForm = reactive({
  status: '',
  accountType: ''
})

const form = reactive({
  id: null,
  accountCode: '',
  accountName: '',
  userId: '',
  accountType: '',
  broker: '',
  accountStatus: 'ACTIVE'
})

const rules = {
  accountCode: [{ required: true, message: '请输入账户代码', trigger: 'blur' }],
  accountName: [{ required: true, message: '请输入账户名称', trigger: 'blur' }],
  userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
  accountType: [{ required: true, message: '请选择账户类型', trigger: 'change' }],
  accountStatus: [{ required: true, message: '请选择账户状态', trigger: 'change' }]
}

const dialogTitle = ref('新增账户')

onMounted(() => {
  loadAccounts()
})

const loadAccounts = async () => {
  loading.value = true
  try {
    const response = await getTradeAccounts()
    console.log('账户接口返回', response)
    accountList.value = response
  } catch (error) {
    ElMessage.error('加载账户列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = async () => {
  loading.value = true
  try {
    const params = {}
    if (searchForm.status) params.accountStatus = searchForm.status
    if (searchForm.accountType) params.accountType = searchForm.accountType
    const response = await searchTradeAccounts(params)
    console.log('账户搜索结果', response)
    accountList.value = response
  } catch (error) {
    ElMessage.error('查询失败')
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  Object.assign(searchForm, {
    status: '',
    accountType: ''
  })
  loadAccounts()
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑账户'
  Object.assign(form, row)
  showCreateDialog.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该账户吗？', '提示', {
      type: 'warning'
    })
    await deleteTradeAccount(row.id)
    ElMessage.success('删除成功')
    loadAccounts()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleStatusChange = async (command) => {
  try {
    await updateTradeAccountStatus(command.id, command.status)
    ElMessage.success('状态更新成功')
    loadAccounts()
  } catch (error) {
    ElMessage.error('状态更新失败')
  }
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    if (isEdit.value) {
      await updateTradeAccount(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await createTradeAccount(form)
      ElMessage.success('创建成功')
    }
    showCreateDialog.value = false
    loadAccounts()
  } catch (error) {
    ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
  }
}

const resetForm = () => {
  isEdit.value = false
  dialogTitle.value = '新增账户'
  Object.assign(form, {
    id: null,
    accountCode: '',
    accountName: '',
    userId: '',
    accountType: '',
    broker: '',
    accountStatus: 'ACTIVE'
  })
  formRef.value?.resetFields()
}

const getStatusType = (status) => {
  const statusMap = {
    'ACTIVE': 'success',
    'SUSPENDED': 'warning',
    'CLOSED': 'danger'
  }
  return statusMap[status] || 'info'
}

const getStatusText = (status) => {
  const statusMap = {
    'ACTIVE': '正常',
    'SUSPENDED': '暂停',
    'CLOSED': '关闭'
  }
  return statusMap[status] || status
}

const formatCurrency = (value) => {
  if (!value) return '0.00'
  return new Intl.NumberFormat('zh-CN', {
    style: 'currency',
    currency: 'CNY'
  }).format(value)
}

const formatDateTime = (value) => {
  if (!value) return ''
  return new Date(value).toLocaleString('zh-CN')
}
</script>

<style scoped>
.account-list {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-bar {
  margin-bottom: 20px;
}

.text-success {
  color: #67c23a;
}

.text-danger {
  color: #f56c6c;
}
</style>
