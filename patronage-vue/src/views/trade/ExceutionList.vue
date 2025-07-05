<template>
    <div class="execution-list">
      <div class="page-header">
        <h2>交割单管理</h2>
      </div>
  
      <el-card>
        <div class="search-bar">
          <el-form :inline="true" :model="searchForm">
            <el-form-item label="订单状态">
              <el-select v-model="searchForm.orderStatus" placeholder="选择状态" clearable>
                <el-option label="部分成交" value="PARTIAL" />
                <el-option label="全部成交" value="FILLED" />
              </el-select>
            </el-form-item>
            <el-form-item label="订单类型">
              <el-select v-model="searchForm.orderType" placeholder="选择类型" clearable>
                <el-option label="买入" value="BUY" />
                <el-option label="卖出" value="SELL" />
              </el-select>
            </el-form-item>
            <el-form-item label="业务类型">
              <el-select v-model="searchForm.orderBizType" placeholder="选择业务类型" clearable>
                <el-option label="建仓交易单" value="OPEN" />
                <el-option label="调仓交易单" value="REBALANCE" />
                <el-option label="差错处理交易单" value="ERROR_FIX" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">搜索</el-button>
              <el-button @click="resetSearch">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
  
        <el-table :data="orderList" v-loading="loading" stripe>
          <el-table-column prop="orderCode" label="订单编号" width="150" />
          <el-table-column prop="accountId" label="账户ID" width="100" />
          <el-table-column prop="fundId" label="基金ID" width="100" />
          <el-table-column prop="orderType" label="订单类型" width="100">
            <template #default="{ row }">
              <el-tag :type="row.orderType === 'BUY' ? 'success' : 'danger'">
                {{ row.orderType === 'BUY' ? '买入' : '卖出' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="orderStatus" label="订单状态" width="120">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.orderStatus)">
                {{ getStatusText(row.orderStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="orderBizType" label="业务类型" width="120">
            <template #default="{ row }">
              <el-tag :type="getBizTypeType(row.orderBizType)">
                {{ getBizTypeText(row.orderBizType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="filledQuantity" label="成交数量" width="120" />
          <el-table-column prop="filledAmount" label="成交金额" width="120">
            <template #default="{ row }">
              {{ formatCurrency(row.filledAmount) }}
            </template>
          </el-table-column>
          <el-table-column prop="avgFillPrice" label="成交均价" width="120">
            <template #default="{ row }">
              {{ formatCurrency(row.avgFillPrice) }}
            </template>
          </el-table-column>
          <el-table-column prop="orderTime" label="委托时间" width="180">
            <template #default="{ row }">
              {{ formatDateTime(row.orderTime) }}
            </template>
          </el-table-column>
        </el-table>
  
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="queryParams.pageNum"
            v-model:page-size="queryParams.pageSize"
            :page-sizes="[10, 20, 30, 50]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
  
      <el-dialog
        :title="dialogTitle"
        v-model="showCreateDialog"
        width="600px"
        @close="resetForm"
      >
        <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
          <el-form-item label="订单编号" prop="orderCode">
            <el-input v-model="form.orderCode" placeholder="请输入订单编号" />
          </el-form-item>
          <el-form-item label="账户ID" prop="accountId">
            <el-input v-model="form.accountId" placeholder="请输入账户ID" />
          </el-form-item>
          <el-form-item label="策略ID" prop="strategyId">
            <el-input v-model="form.strategyId" placeholder="请输入策略ID" />
          </el-form-item>
          <el-form-item label="基金ID" prop="fundId">
            <el-input v-model="form.fundId" placeholder="请输入基金ID" />
          </el-form-item>
          <el-form-item label="订单类型" prop="orderType">
            <el-select v-model="form.orderType" placeholder="选择订单类型">
              <el-option label="买入" value="BUY" />
              <el-option label="卖出" value="SELL" />
            </el-select>
          </el-form-item>
          <el-form-item label="业务类型" prop="orderBizType">
            <el-select v-model="form.orderBizType" placeholder="选择业务类型">
              <el-option label="建仓交易单" value="OPEN" />
              <el-option label="调仓交易单" value="REBALANCE" />
              <el-option label="差错处理交易单" value="ERROR_FIX" />
            </el-select>
          </el-form-item>
          <el-form-item label="委托价格" prop="orderPrice">
            <el-input v-model="form.orderPrice" placeholder="请输入委托价格" />
          </el-form-item>
          <el-form-item label="委托数量" prop="orderQuantity">
            <el-input v-model="form.orderQuantity" placeholder="请输入委托数量" />
          </el-form-item>
          <el-form-item label="订单状态" prop="orderStatus">
            <el-select v-model="form.orderStatus" placeholder="选择订单状态">
              <el-option label="待执行" value="PENDING" />
              <el-option label="执行中" value="EXECUTING" />
              <el-option label="部分成交" value="PARTIAL" />
              <el-option label="全部成交" value="FILLED" />
              <el-option label="已取消" value="CANCELLED" />
              <el-option label="已拒绝" value="REJECTED" />
            </el-select>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showCreateDialog = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </template>
      </el-dialog>
  
      <!-- 取消订单对话框 -->
      <el-dialog
        title="取消订单"
        v-model="showCancelDialog"
        width="400px"
      >
        <el-form :model="cancelForm" ref="cancelFormRef" label-width="100px">
          <el-form-item label="取消原因" prop="cancelReason">
            <el-input 
              v-model="cancelForm.cancelReason" 
              type="textarea" 
              placeholder="请输入取消原因"
              :rows="3"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showCancelDialog = false">取消</el-button>
          <el-button type="primary" @click="confirmCancel">确定</el-button>
        </template>
      </el-dialog>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive, onMounted, computed } from 'vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import { Plus, ArrowDown } from '@element-plus/icons-vue'
  import {
    searchTradeOrders,
    getTradeOrders,
    createTradeOrder,
    updateTradeOrder,
    deleteTradeOrder,
    updateTradeOrderStatus,
    executeTradeOrder,
    cancelTradeOrder
  } from '@/api/trade'
  
  const loading = ref(false)
  const orderList = ref([])
  const showCreateDialog = ref(false)
  const showCancelDialog = ref(false)
  const isEdit = ref(false)
  const formRef = ref()
  const cancelFormRef = ref()
  const currentCancelOrder = ref(null)
  const total = ref(0)
  
  const queryParams = reactive({
    pageNum: 1,
    pageSize: 10
  })
  
  const searchForm = reactive({
    orderStatus: '',
    orderType: '',
    orderBizType: ''
  })
  
  const form = reactive({
    id: null,
    orderCode: '',
    accountId: '',
    strategyId: '',
    fundId: '',
    orderType: '',
    orderStatus: 'PENDING',
    orderBizType: 'OPEN',
    orderPrice: '',
    orderQuantity: '',
    filledQuantity: 0,
    filledAmount: 0,
    avgFillPrice: null,
    commission: 0,
    orderTime: new Date().toISOString(),
    expireTime: null,
    cancelTime: null,
    cancelReason: '',
    remark: ''
  })
  
  const cancelForm = reactive({
    cancelReason: ''
  })
  
  const rules = {
    orderCode: [{ required: true, message: '请输入订单编号', trigger: 'blur' }],
    accountId: [{ required: true, message: '请输入账户ID', trigger: 'blur' }],
    fundId: [{ required: true, message: '请输入基金ID', trigger: 'blur' }],
    orderType: [{ required: true, message: '请选择订单类型', trigger: 'change' }],
    orderStatus: [{ required: true, message: '请选择订单状态', trigger: 'change' }],
    orderPrice: [{ required: true, message: '请输入委托价格', trigger: 'blur' }],
    orderQuantity: [{ required: true, message: '请输入委托数量', trigger: 'blur' }]
  }
  
  const dialogTitle = computed(() => isEdit.value ? '编辑订单' : '新增订单')
  
  onMounted(() => {
    loadOrders()
  })
  
  const loadOrders = async () => {
    loading.value = true
    try {
      const response = await getTradeOrders()
      orderList.value = response.filter(order => 
        ['FILLED', 'PARTIAL'].includes(order.orderStatus)
      )
    } catch (error) {
      ElMessage.error('加载交割单列表失败')
    } finally {
      loading.value = false
    }
  }
  
  const handleSearch = async () => {
    loading.value = true
    try {
      const params = {
        orderStatus: searchForm.orderStatus,
        orderType: searchForm.orderType,
        orderBizType: searchForm.orderBizType
      }
      const response = await searchTradeOrders(params)
      orderList.value = response.filter(order => 
        ['FILLED', 'PARTIAL'].includes(order.orderStatus)
      )
    } catch (error) {
      ElMessage.error('查询失败')
    } finally {
      loading.value = false
    }
  }
  
  const resetSearch = () => {
    Object.assign(searchForm, {
      orderStatus: '',
      orderType: '',
      orderBizType: ''
    })
    loadOrders()
  }
  
  const handleEdit = (row) => {
    isEdit.value = true
    Object.assign(form, row)
    showCreateDialog.value = true
  }
  
  const handleExecute = async (row) => {
    try {
      await ElMessageBox.confirm('确定要执行该订单吗？', '提示', {
        type: 'warning'
      })
      const response = await executeTradeOrder(row.id)
      if (response.success) {
        ElMessage.success('订单执行成功')
        loadOrders()
      } else {
        ElMessage.error(response.message || '订单执行失败')
      }
    } catch (error) {
      if (error !== 'cancel') {
        ElMessage.error('订单执行失败')
      }
    }
  }
  
  const handleCancel = (row) => {
    currentCancelOrder.value = row
    cancelForm.cancelReason = ''
    showCancelDialog.value = true
  }
  
  const confirmCancel = async () => {
    try {
      const response = await cancelTradeOrder(currentCancelOrder.value.id, cancelForm.cancelReason)
      if (response.success) {
        ElMessage.success('订单取消成功')
        showCancelDialog.value = false
        loadOrders()
      } else {
        ElMessage.error(response.message || '订单取消失败')
      }
    } catch (error) {
      ElMessage.error('订单取消失败')
    }
  }
  
  const handleDelete = async (row) => {
    try {
      await ElMessageBox.confirm('确定要删除该订单吗？', '提示', {
        type: 'warning'
      })
      await deleteTradeOrder(row.id)
      ElMessage.success('删除成功')
      loadOrders()
    } catch (error) {
      if (error !== 'cancel') {
        ElMessage.error('删除失败')
      }
    }
  }
  
  const handleSubmit = async () => {
    try {
      await formRef.value.validate()
      if (isEdit.value) {
        await updateTradeOrder(form.id, form)
        ElMessage.success('更新成功')
      } else {
        await createTradeOrder(form)
        ElMessage.success('创建成功')
      }
      showCreateDialog.value = false
      loadOrders()
    } catch (error) {
      ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
    }
  }
  
  const resetForm = () => {
    isEdit.value = false
    Object.assign(form, {
      id: null,
      orderCode: '',
      accountId: '',
      strategyId: '',
      fundId: '',
      orderType: '',
      orderStatus: 'PENDING',
      orderBizType: 'OPEN',
      orderPrice: '',
      orderQuantity: '',
      filledQuantity: 0,
      filledAmount: 0,
      avgFillPrice: null,
      commission: 0,
      orderTime: new Date().toISOString(),
      expireTime: null,
      cancelTime: null,
      cancelReason: '',
      remark: ''
    })
    formRef.value?.resetFields()
  }
  
  const canExecute = (status) => {
    return status === 'PENDING'
  }
  
  const canCancel = (status) => {
    return status === 'PENDING' || status === 'EXECUTING'
  }
  
  const getStatusType = (status) => {
    const statusMap = {
      'PARTIAL': 'warning',
      'FILLED': 'success'
    }
    return statusMap[status] || 'info'
  }
  
  const getStatusText = (status) => {
    const statusMap = {
      'PARTIAL': '部分成交',
      'FILLED': '全部成交'
    }
    return statusMap[status] || status
  }
  
  const getBizTypeType = (bizType) => {
    const bizTypeMap = {
      'OPEN': 'info',
      'REBALANCE': 'warning',
      'ERROR_FIX': 'danger'
    }
    return bizTypeMap[bizType] || 'info'
  }
  
  const getBizTypeText = (bizType) => {
    const bizTypeMap = {
      'OPEN': '建仓交易单',
      'REBALANCE': '调仓交易单',
      'ERROR_FIX': '差错处理交易单'
    }
    return bizTypeMap[bizType] || bizType
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
  
  const handleSizeChange = (val) => {
    queryParams.pageSize = val
    loadOrders()
  }
  
  const handleCurrentChange = (val) => {
    queryParams.pageNum = val
    loadOrders()
  }
  </script>
  
  <style scoped>
  .execution-list {
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
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
  </style>
  