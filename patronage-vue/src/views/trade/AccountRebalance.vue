<template>
    <div class="account-rebalance-bg">
      <div class="section">
        <div class="section-title">基本信息</div>
        <el-form :model="form" :rules="rules" ref="formRef" label-width="110px" class="info-form" status-icon>
          <el-form-item label="账户ID" prop="accountId">
            <el-input v-model="form.accountId" placeholder="请输入账户ID" class="input-lg" />
            <el-button @click="fetchPositions" class="ml-btn" type="primary" round>查询持仓</el-button>
          </el-form-item>
          <el-form-item label="计划名称" prop="planName">
            <el-input v-model="form.planName" placeholder="请输入计划名称" class="input-lg" />
          </el-form-item>
          <el-form-item label="组合名称" prop="portfolioName">
            <el-input v-model="form.portfolioName" placeholder="请输入组合名称" class="input-lg" />
          </el-form-item>
          <el-form-item label="组合类型" prop="portfolioType">
            <el-select v-model="form.portfolioType" placeholder="请选择组合类型" class="input-lg">
              <el-option label="FOF" value="FOF" />
              <el-option label="组合" value="组合" />
              <el-option label="定制" value="定制" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="success" class="submit-btn" :loading="loading" @click="submit">完成！提交调仓计划</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="section">
        <div class="section-title">持仓信息</div>
        <el-table v-if="positions.length" :data="positions" border style="margin-bottom: 24px;">
          <el-table-column v-for="col in orderedColumns" :key="col" :prop="col" :label="columnMap[col] || col" />
        </el-table>
        <el-empty v-else description="暂无持仓数据" />
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, computed } from 'vue'
  import { ElMessage } from 'element-plus'
  import { getAccountPositions, createAccountRebalance } from '@/api/accountRebalance'
  
  const form = ref({
    accountId: '',
    planName: '',
    portfolioName: '',
    portfolioType: '',
    currentHoldings: ''
  })
  const rules = {
    accountId: [{ required: true, message: '请输入账户ID', trigger: 'blur' }],
    planName: [{ required: true, message: '请输入计划名称', trigger: 'blur' }],
    portfolioName: [{ required: true, message: '请输入组合名称', trigger: 'blur' }],
    portfolioType: [{ required: true, message: '请选择组合类型', trigger: 'change' }]
  }
  const formRef = ref()
  const positions = ref([])
  const loading = ref(false)
  const columns = ref([])
  const columnMap = {
    id: '编号',
    accountId: '账户ID',
    fundId: '基金ID',
    totalQuantity: '总持仓',
    availableQuantity: '可用持仓',
    frozenQuantity: '冻结持仓',
    avgCost: '平均成本',
    marketPrice: '市价',
    marketValue: '市值',
    unrealizedPnl: '未实现盈亏',
    realizedPnl: '已实现盈亏',
    totalPnl: '总盈亏',
    updateTime: '更新时间',
  }
  // 指定表格字段顺序
  const orderedColumns = computed(() => {
    const order = [
      'id', 'accountId', 'fundId', 'totalQuantity', 'availableQuantity', 'frozenQuantity',
      'avgCost', 'marketPrice', 'marketValue', 'unrealizedPnl', 'realizedPnl', 'totalPnl', 'updateTime'
    ]
    // 只展示有的字段
    return order.filter(col => columns.value.includes(col))
  })
  
  const fetchPositions = async () => {
    if (!form.value.accountId) {
      ElMessage.error('请输入账户ID')
      return
    }
    const res = await getAccountPositions(form.value.accountId)
    positions.value = res
    form.value.currentHoldings = JSON.stringify(res, null, 2)
    columns.value = res.length ? Object.keys(res[0]) : []
  }
  
  const submit = () => {
    formRef.value.validate(async (valid) => {
      if (!valid) return
      loading.value = true
      try {
        await createAccountRebalance(form.value)
        ElMessage.success('调仓计划已提交')
      } catch {
        ElMessage.error('提交失败')
      } finally {
        loading.value = false
      }
    })
  }
  </script>
  
  <style scoped>
  .account-rebalance-bg {
    min-height: 100vh;
    background: #f5f7fa;
    padding: 40px 0;
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  .section {
    background: #fff;
    border-radius: 12px;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,0.04);
    padding: 32px 40px 24px 40px;
    margin-bottom: 40px;
    width: 1200px;
  }
  .section-title {
    font-size: 20px;
    font-weight: bold;
    color: #222;
    margin-bottom: 18px;
    letter-spacing: 1px;
  }
  .info-form {
    margin-top: 10px;
  }
  .input-lg {
    border-radius: 8px;
    font-size: 16px;
    height: 40px;
  }
  .ml-btn {
    margin-top: 10px;
    margin-left: 900px;
  }
  .submit-btn {
    width: 100%;
    height: 52px;
    font-size: 20px;
    border-radius: 26px;
    background: linear-gradient(90deg, #52c41a 0%, #7be141 100%);
    border: none;
    color: #fff;
    font-weight: bold;
    letter-spacing: 2px;
    margin-top: 18px;
  }
  .submit-btn:hover {
    background: linear-gradient(90deg, #7be141 0%, #52c41a 100%);
  }
  @media (max-width: 700px) {
    .section {
      width: 95vw;
      min-width: unset;
      max-width: 98vw;
      padding: 16px 8px;
    }
  }
  </style>
  