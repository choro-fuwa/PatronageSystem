<template>
  <div class="product-list">
    <div class="page-header">
      <h2>组合产品管理</h2>
      <el-button type="primary" @click="showCreateDialog = true">
        <el-icon><Plus /></el-icon>
        新建产品
      </el-button>
    </div>

    <!-- 搜索和筛选 -->
    <div class="search-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索产品名称或代码"
            clearable
            @keyup.enter="handleSearch"
          >
            <template #append>
              <el-button @click="handleSearch">
                <el-icon><Search /></el-icon>
              </el-button>
            </template>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-select v-model="filterStatus" placeholder="产品状态" clearable @change="handleFilter">
            <el-option label="草稿" value="DRAFT" />
            <el-option label="待审核" value="PENDING" />
            <el-option label="已审核" value="APPROVED" />
            <el-option label="已上线" value="ACTIVE" />
            <el-option label="暂停" value="SUSPENDED" />
            <el-option label="已关闭" value="CLOSED" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-select v-model="filterType" placeholder="产品类型" clearable @change="handleFilter">
            <el-option label="FOF" value="FOF" />
            <el-option label="组合" value="组合" />
            <el-option label="定制" value="定制" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-select v-model="filterRisk" placeholder="风险等级" clearable @change="handleFilter">
            <el-option label="低" value="低" />
            <el-option label="中" value="中" />
            <el-option label="高" value="高" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button @click="resetFilter">重置筛选</el-button>
          <el-button type="primary" @click="loadProducts">刷新</el-button>
        </el-col>
      </el-row>
    </div>

    <!-- 产品列表 -->
    <div class="product-table">
      <el-table :data="productList" v-loading="loading" stripe>
        <el-table-column prop="productCode" label="产品代码" width="120" />
        <el-table-column prop="productName" label="产品名称" min-width="200" />
        <el-table-column prop="productType" label="产品类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getProductTypeTag(row.productType)">{{ row.productType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="100">
          <template #default="{ row }">
            <el-tag :type="getRiskLevelTag(row.riskLevel)">{{ row.riskLevel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="targetReturn" label="目标收益率" width="120">
          <template #default="{ row }">
            {{ formatPercentage(row.targetReturn) }}
          </template>
        </el-table-column>
        <el-table-column prop="productStatus" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.productStatus)">{{ getStatusText(row.productStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="launchDate" label="成立日期" width="120" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="viewProduct(row)">查看</el-button>
            <el-button size="small" type="primary" @click="editProduct(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteProduct(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 创建/编辑产品对话框 -->
    <el-dialog
      v-model="showCreateDialog"
      :title="isEdit ? '编辑产品' : '新建产品'"
      width="800px"
    >
      <el-form ref="productFormRef" :model="productForm" :rules="productRules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="产品代码" prop="productCode">
              <el-input v-model="productForm.productCode" placeholder="请输入产品代码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="产品名称" prop="productName">
              <el-input v-model="productForm.productName" placeholder="请输入产品名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="产品简称" prop="productShortName">
              <el-input v-model="productForm.productShortName" placeholder="请输入产品简称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="产品类型" prop="productType">
              <el-select v-model="productForm.productType" placeholder="请选择产品类型">
                <el-option label="FOF" value="FOF" />
                <el-option label="组合" value="组合" />
                <el-option label="定制" value="定制" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="productForm.riskLevel" placeholder="请选择风险等级">
                <el-option label="低" value="低" />
                <el-option label="中" value="中" />
                <el-option label="高" value="高" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标收益率" prop="targetReturn">
              <el-input-number
                v-model="productForm.targetReturn"
                :precision="4"
                :step="0.01"
                :min="0"
                :max="1"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="最小投资金额" prop="minInvestment">
              <el-input-number
                v-model="productForm.minInvestment"
                :precision="2"
                :step="1000"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最大投资金额" prop="maxInvestment">
              <el-input-number
                v-model="productForm.maxInvestment"
                :precision="2"
                :step="10000"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="管理费率" prop="managementFee">
              <el-input-number
                v-model="productForm.managementFee"
                :precision="4"
                :step="0.001"
                :min="0"
                :max="1"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="托管费率" prop="custodianFee">
              <el-input-number
                v-model="productForm.custodianFee"
                :precision="4"
                :step="0.001"
                :min="0"
                :max="1"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="业绩比较基准" prop="benchmark">
          <el-input v-model="productForm.benchmark" placeholder="请输入业绩比较基准" />
        </el-form-item>
        <el-form-item label="投资策略" prop="investmentStrategy">
          <el-input
            v-model="productForm.investmentStrategy"
            type="textarea"
            :rows="3"
            placeholder="请输入投资策略"
          />
        </el-form-item>
        <el-form-item label="投资范围" prop="investmentScope">
          <el-input
            v-model="productForm.investmentScope"
            type="textarea"
            :rows="3"
            placeholder="请输入投资范围"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="submitProduct" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看产品详情对话框 -->
    <el-dialog v-model="showDetailDialog" title="产品详情" width="600px" :close-on-click-modal="false">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="产品代码">{{ detailProduct.productCode }}</el-descriptions-item>
        <el-descriptions-item label="产品名称">{{ detailProduct.productName }}</el-descriptions-item>
        <el-descriptions-item label="产品简称">{{ detailProduct.productShortName }}</el-descriptions-item>
        <el-descriptions-item label="产品类型">{{ detailProduct.productType }}</el-descriptions-item>
        <el-descriptions-item label="风险等级">{{ detailProduct.riskLevel }}</el-descriptions-item>
        <el-descriptions-item label="目标收益率">{{ formatPercentage(detailProduct.targetReturn) }}</el-descriptions-item>
        <el-descriptions-item label="最小投资金额">{{ detailProduct.minInvestment }}</el-descriptions-item>
        <el-descriptions-item label="最大投资金额">{{ detailProduct.maxInvestment }}</el-descriptions-item>
        <el-descriptions-item label="管理费率">{{ detailProduct.managementFee }}</el-descriptions-item>
        <el-descriptions-item label="托管费率">{{ detailProduct.custodianFee }}</el-descriptions-item>
        <el-descriptions-item label="业绩比较基准">{{ detailProduct.benchmark }}</el-descriptions-item>
        <el-descriptions-item label="投资策略">{{ detailProduct.investmentStrategy }}</el-descriptions-item>
        <el-descriptions-item label="投资范围">{{ detailProduct.investmentScope }}</el-descriptions-item>
        <el-descriptions-item label="产品状态">{{ getStatusText(detailProduct.productStatus) }}</el-descriptions-item>
        <el-descriptions-item label="成立日期">{{ detailProduct.launchDate }}</el-descriptions-item>
        <el-descriptions-item label="到期日期">{{ detailProduct.maturityDate }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="showDetailDialog = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import { portfolioProductApi } from '@/api/portfolioProduct'

// 响应式数据
const loading = ref(false)
const productList = ref([])
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)
const searchKeyword = ref('')
const filterStatus = ref('')
const filterType = ref('')
const filterRisk = ref('')

// 对话框相关
const showCreateDialog = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const productFormRef = ref()
const showDetailDialog = ref(false)
const detailProduct = ref({})

// 表单数据
const productForm = reactive({
  productCode: '',
  productName: '',
  productShortName: '',
  productType: '',
  riskLevel: '',
  targetReturn: 0,
  minInvestment: 0,
  maxInvestment: 0,
  managementFee: 0,
  custodianFee: 0,
  subscriptionFee: 0,
  redemptionFee: 0,
  benchmark: '',
  investmentStrategy: '',
  investmentScope: '',
  productStatus: 'DRAFT',
  launchDate: '',
  maturityDate: '',
  creatorId: 1
})

// 表单验证规则
const productRules = {
  productCode: [{ required: true, message: '请输入产品代码', trigger: 'blur' }],
  productName: [{ required: true, message: '请输入产品名称', trigger: 'blur' }],
  productType: [{ required: true, message: '请选择产品类型', trigger: 'change' }],
  riskLevel: [{ required: true, message: '请选择风险等级', trigger: 'change' }],
  targetReturn: [{ required: true, message: '请输入目标收益率', trigger: 'blur' }]
}

// 生命周期
onMounted(() => {
  loadProducts()
})

// 方法
const loadProducts = async () => {
  loading.value = true
  try {
    const response = await portfolioProductApi.getAllProducts()
    if (response.code === 200) {
      productList.value = response.data
      total.value = response.data.length
    } else {
      ElMessage.error(response.message || '获取产品列表失败')
    }
  } catch (error) {
    console.error('获取产品列表失败:', error)
    ElMessage.error('获取产品列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    searchProducts()
  } else {
    loadProducts()
  }
}

const searchProducts = async () => {
  loading.value = true
  try {
    const response = await portfolioProductApi.searchProducts(searchKeyword.value)
    if (response.code === 200) {
      productList.value = response.data
      total.value = response.data.length
    } else {
      ElMessage.error(response.message || '搜索产品失败')
    }
  } catch (error) {
    console.error('搜索产品失败:', error)
    ElMessage.error('搜索产品失败')
  } finally {
    loading.value = false
  }
}

const handleFilter = () => {
  loadProducts()
}

const resetFilter = () => {
  searchKeyword.value = ''
  filterStatus.value = ''
  filterType.value = ''
  filterRisk.value = ''
  loadProducts()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  loadProducts()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadProducts()
}

const viewProduct = (row) => {
  detailProduct.value = { ...row }
  showDetailDialog.value = true
}

const editProduct = (row) => {
  isEdit.value = true
  Object.assign(productForm, row)
  showCreateDialog.value = true
}

const deleteProduct = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该产品吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await portfolioProductApi.deleteProduct(row.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadProducts()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除产品失败:', error)
      ElMessage.error('删除产品失败')
    }
  }
}

const submitProduct = async () => {
  try {
    await productFormRef.value.validate()
    submitting.value = true
    
    const api = isEdit.value ? portfolioProductApi.updateProduct : portfolioProductApi.createProduct
    const response = await api(productForm)
    
    if (response.code === 200) {
      ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
      showCreateDialog.value = false
      loadProducts()
      resetForm()
    } else {
      ElMessage.error(response.message || (isEdit.value ? '更新失败' : '创建失败'))
    }
  } catch (error) {
    console.error('提交产品失败:', error)
    ElMessage.error('提交产品失败')
  } finally {
    submitting.value = false
  }
}

const resetForm = () => {
  productFormRef.value?.resetFields()
  Object.assign(productForm, {
    productCode: '',
    productName: '',
    productShortName: '',
    productType: '',
    riskLevel: '',
    targetReturn: 0,
    minInvestment: 0,
    maxInvestment: 0,
    managementFee: 0,
    custodianFee: 0,
    subscriptionFee: 0,
    redemptionFee: 0,
    benchmark: '',
    investmentStrategy: '',
    investmentScope: '',
    productStatus: 'DRAFT',
    launchDate: '',
    maturityDate: '',
    creatorId: 1
  })
  isEdit.value = false
}

// 工具方法
const formatPercentage = (value) => {
  if (value == null) return '-'
  return (value * 100).toFixed(2) + '%'
}

const getProductTypeTag = (type) => {
  const typeMap = {
    'FOF': 'primary',
    '组合': 'success',
    '定制': 'warning'
  }
  return typeMap[type] || 'info'
}

const getRiskLevelTag = (level) => {
  const levelMap = {
    '低': 'success',
    '中': 'warning',
    '高': 'danger'
  }
  return levelMap[level] || 'info'
}

const getStatusTag = (status) => {
  const statusMap = {
    'DRAFT': 'info',
    'PENDING': 'warning',
    'APPROVED': 'success',
    'ACTIVE': 'primary',
    'SUSPENDED': 'danger',
    'CLOSED': 'info'
  }
  return statusMap[status] || 'info'
}

const getStatusText = (status) => {
  const statusMap = {
    'DRAFT': '草稿',
    'PENDING': '待审核',
    'APPROVED': '已审核',
    'ACTIVE': '已上线',
    'SUSPENDED': '暂停',
    'CLOSED': '已关闭'
  }
  return statusMap[status] || status
}
</script>

<style scoped>
.product-list {
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

.search-section {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.product-table {
  margin-bottom: 20px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table th) {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: 600;
}
</style> 