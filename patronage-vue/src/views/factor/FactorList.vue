<template>
  <div class="factor-list">
    <div class="page-header">
      <h2>因子管理</h2>
      <div class="header-actions">
        <el-button type="primary" @click="showAddDialog = true">
          <el-icon><Plus /></el-icon>
          新增因子
        </el-button>
        <el-button @click="$router.push('/factor-category')">
          <el-icon><Folder /></el-icon>
          分类管理
        </el-button>
      </div>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-form :model="searchForm" inline>
        <el-form-item label="因子类型">
          <el-select v-model="searchForm.factorType" placeholder="请选择因子类型" clearable>
            <el-option label="技术因子" value="技术因子" />
            <el-option label="基本面因子" value="基本面因子" />
            <el-option label="宏观因子" value="宏观因子" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="searchForm.riskLevel" placeholder="请选择风险等级" clearable>
            <el-option label="低风险" value="低风险" />
            <el-option label="中风险" value="中风险" />
            <el-option label="高风险" value="高风险" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchForm.categoryId" placeholder="请选择分类" clearable>
            <el-option
              v-for="category in categories"
              :key="category.id"
              :label="category.categoryName"
              :value="category.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchFactors">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 因子列表 -->
    <div class="factor-grid">
      <el-card
        v-for="factor in factors"
        :key="factor.id"
        class="factor-card"
        @click="viewFactorDetail(factor.id)"
      >
        <div class="factor-header">
          <h3>{{ factor.factorName }}</h3>
          <el-tag :type="getRiskLevelType(factor.riskLevel)" size="small">
            {{ factor.riskLevel }}
          </el-tag>
        </div>
        <div class="factor-info">
          <p class="factor-code">代码：{{ factor.factorCode }}</p>
          <p class="factor-type">类型：{{ factor.factorType }}</p>
          <p class="factor-frequency">更新频率：{{ factor.updateFrequency }}</p>
        </div>
        <div class="factor-description">
          {{ factor.description }}
        </div>
        <div class="factor-actions">
          <el-button size="small" @click.stop="editFactor(factor)">编辑</el-button>
          <el-button size="small" type="danger" @click.stop="deleteFactor(factor.id)">删除</el-button>
        </div>
      </el-card>
    </div>

    <!-- 新增/编辑因子对话框 -->
    <el-dialog
      v-model="showAddDialog"
      :title="editingFactor ? '编辑因子' : '新增因子'"
      width="600px"
    >
      <el-form :model="factorForm" :rules="factorRules" ref="factorFormRef" label-width="100px">
        <el-form-item label="因子代码" prop="factorCode">
          <el-input v-model="factorForm.factorCode" placeholder="请输入因子代码" />
        </el-form-item>
        <el-form-item label="因子名称" prop="factorName">
          <el-input v-model="factorForm.factorName" placeholder="请输入因子名称" />
        </el-form-item>
        <el-form-item label="因子简称" prop="factorShortName">
          <el-input v-model="factorForm.factorShortName" placeholder="请输入因子简称" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="factorForm.categoryId" placeholder="请选择分类">
            <el-option
              v-for="category in categories"
              :key="category.id"
              :label="category.categoryName"
              :value="category.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="因子类型" prop="factorType">
          <el-select v-model="factorForm.factorType" placeholder="请选择因子类型">
            <el-option label="技术因子" value="技术因子" />
            <el-option label="基本面因子" value="基本面因子" />
            <el-option label="宏观因子" value="宏观因子" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级" prop="riskLevel">
          <el-select v-model="factorForm.riskLevel" placeholder="请选择风险等级">
            <el-option label="低风险" value="低风险" />
            <el-option label="中风险" value="中风险" />
            <el-option label="高风险" value="高风险" />
          </el-select>
        </el-form-item>
        <el-form-item label="更新频率" prop="updateFrequency">
          <el-select v-model="factorForm.updateFrequency" placeholder="请选择更新频率">
            <el-option label="日频" value="daily" />
            <el-option label="周频" value="weekly" />
            <el-option label="月频" value="monthly" />
          </el-select>
        </el-form-item>
        <el-form-item label="数据来源" prop="dataSource">
          <el-input v-model="factorForm.dataSource" placeholder="请输入数据来源" />
        </el-form-item>
        <el-form-item label="因子描述" prop="description">
          <el-input
            v-model="factorForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入因子描述"
          />
        </el-form-item>
        <el-form-item label="计算公式" prop="formula">
          <el-input
            v-model="factorForm.formula"
            type="textarea"
            :rows="3"
            placeholder="请输入计算公式"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="saveFactor">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Folder } from '@element-plus/icons-vue'
import { factorApi } from '@/api/factor'
import { debugFactorApi } from '@/utils/debug'

const router = useRouter()

// 数据
const factors = ref([])
const categories = ref([])
const showAddDialog = ref(false)
const editingFactor = ref(null)
const factorFormRef = ref()

// 搜索表单
const searchForm = reactive({
  factorType: '',
  riskLevel: '',
  categoryId: null
})

// 因子表单
const factorForm = reactive({
  factorCode: '',
  factorName: '',
  factorShortName: '',
  categoryId: null,
  factorType: '',
  riskLevel: '',
  updateFrequency: 'daily',
  dataSource: '',
  description: '',
  formula: ''
})

// 表单验证规则
const factorRules = {
  factorCode: [{ required: true, message: '请输入因子代码', trigger: 'blur' }],
  factorName: [{ required: true, message: '请输入因子名称', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  factorType: [{ required: true, message: '请选择因子类型', trigger: 'change' }],
  riskLevel: [{ required: true, message: '请选择风险等级', trigger: 'change' }]
}

// 获取因子列表
const getFactors = async () => {
  try {
    const response = await factorApi.getFactors()
    if (response.success) {
      factors.value = response.data
    }
  } catch (error) {
    ElMessage.error('获取因子列表失败')
  }
}

// 获取分类列表
const getCategories = async () => {
  try {
    const response = await factorApi.getCategories()
    if (response.success) {
      categories.value = response.data
    }
  } catch (error) {
    ElMessage.error('获取分类列表失败')
  }
}

// 搜索因子
const searchFactors = async () => {
  try {
    const response = await factorApi.searchFactors(searchForm)
    if (response.success) {
      factors.value = response.data
    }
  } catch (error) {
    ElMessage.error('搜索因子失败')
  }
}

// 重置搜索
const resetSearch = () => {
  Object.assign(searchForm, {
    factorType: '',
    riskLevel: '',
    categoryId: null
  })
  getFactors()
}

// 查看因子详情
const viewFactorDetail = (id) => {
  router.push(`/factor/${id}`)
}

// 编辑因子
const editFactor = (factor) => {
  editingFactor.value = factor
  Object.assign(factorForm, factor)
  showAddDialog.value = true
}

// 删除因子
const deleteFactor = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个因子吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await factorApi.deleteFactor(id)
    if (response.success) {
      ElMessage.success('删除成功')
      getFactors()
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 保存因子
const saveFactor = async () => {
  try {
    await factorFormRef.value.validate()
    
    const api = editingFactor.value ? factorApi.updateFactor : factorApi.addFactor
    const response = await api(factorForm)
    
    if (response.success) {
      ElMessage.success(editingFactor.value ? '更新成功' : '新增成功')
      showAddDialog.value = false
      resetForm()
      getFactors()
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

// 重置表单
const resetForm = () => {
  editingFactor.value = null
  Object.assign(factorForm, {
    factorCode: '',
    factorName: '',
    factorShortName: '',
    categoryId: null,
    factorType: '',
    riskLevel: '',
    updateFrequency: 'daily',
    dataSource: '',
    description: '',
    formula: ''
  })
}

// 获取风险等级标签类型
const getRiskLevelType = (riskLevel) => {
  const types = {
    '低风险': 'success',
    '中风险': 'warning',
    '高风险': 'danger'
  }
  return types[riskLevel] || 'info'
}

// 初始化
onMounted(() => {
  getFactors()
  getCategories()
  
  // 添加调试功能（开发环境）
  if (process.env.NODE_ENV === 'development') {
    // 在控制台执行 debugFactorApi() 来测试API
    console.log('调试模式：在控制台执行 debugFactorApi() 来测试因子API')
  }
})
</script>

<style scoped>
.factor-list {
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

.factor-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.factor-card {
  cursor: pointer;
  transition: all 0.3s;
}

.factor-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.factor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.factor-header h3 {
  margin: 0;
  color: #303133;
}

.factor-info {
  margin-bottom: 10px;
}

.factor-info p {
  margin: 5px 0;
  color: #606266;
  font-size: 14px;
}

.factor-description {
  color: #909399;
  font-size: 14px;
  line-height: 1.5;
  margin-bottom: 15px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.factor-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}
</style> 