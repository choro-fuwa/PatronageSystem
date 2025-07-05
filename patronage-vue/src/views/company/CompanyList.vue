<template>
  <div>
    <!-- 页面头部 -->
    <div class="page-header">
      <h1 class="page-title">基金公司</h1>
      <p class="page-subtitle">支持通过基金公司名称、标签筛选等方法，以基金公司为维度进行查询</p>
    </div>
    
    <!-- 搜索表单 -->
    <div class="search-form">
      <el-form :model="searchForm" inline>
        <el-form-item label="关键词">
          <el-input
            v-model="searchForm.keyword"
            placeholder="公司代码、公司名称"
            style="width: 300px"
            clearable
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <!-- 公司列表 -->
    <div class="card-container">
      <div v-if="loading" style="text-align: center; padding: 40px;">
        <el-loading />
      </div>
      
      <div v-else-if="companyList.length === 0" style="text-align: center; padding: 40px; color: #909399;">
        暂无数据
      </div>
      
      <div v-else>
        <el-table :data="companyList" style="width: 100%">
          <el-table-column prop="companyCode" label="公司代码" width="120" />
          <el-table-column prop="companyName" label="公司名称" min-width="200" />
          <el-table-column prop="companyShortName" label="公司简称" width="150" />
          <el-table-column prop="establishDate" label="成立日期" width="120" />
          <el-table-column prop="registeredCapital" label="注册资本" width="120">
            <template #default="scope">
              {{ formatCapital(scope.row.registeredCapital) }}
            </template>
          </el-table-column>
          <el-table-column prop="legalRepresentative" label="法定代表人" width="120" />
          <el-table-column prop="companyType" label="公司类型" width="100" />
          <el-table-column label="操作" width="120">
            <template #default="{ row }">
              <el-button size="small" @click="showFunds(row.id)">查看基金</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <el-dialog v-model="showFundDialog" title="公司基金列表" width="800px">
      <el-table :data="fundList">
        <el-table-column prop="fundCode" label="基金代码" />
        <el-table-column prop="fundName" label="基金名称" />
        <el-table-column prop="fundType" label="类型" />
        <el-table-column prop="riskLevel" label="风险等级" />
        <el-table-column prop="fundSize" label="规模" />
        <el-table-column prop="nav" label="净值" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getCompanyList, searchCompanies } from '@/api/company'
import { getFundsByCompanyId } from '@/api/fund'

const router = useRouter()

// 搜索表单
const searchForm = reactive({
  keyword: ''
})

// 公司列表
const companyList = ref([])
const loading = ref(false)
const showFundDialog = ref(false)
const fundList = ref([])

// 获取公司列表
const getCompanies = async () => {
  try {
    loading.value = true
    const res = await getCompanyList()
    companyList.value = res.data
  } catch (error) {
    console.error('获取公司列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 搜索公司
const handleSearch = async () => {
  try {
    loading.value = true
    const params = {}
    
    if (searchForm.keyword) params.keyword = searchForm.keyword
    
    const res = await searchCompanies(params)
    companyList.value = res.data
  } catch (error) {
    console.error('搜索公司失败:', error)
  } finally {
    loading.value = false
  }
}

// 重置搜索
const handleReset = () => {
  searchForm.keyword = ''
  getCompanies()
}

// 查看公司基金
const showFunds = (companyId) => {
  getFundsByCompanyId(companyId).then(res => {
    fundList.value = res.data
    showFundDialog.value = true
  })
}

// 格式化注册资本
const formatCapital = (capital) => {
  if (!capital) return '-'
  return (capital / 10000).toFixed(2) + '万'
}

// 初始化
onMounted(() => {
  getCompanies()
})
</script> 