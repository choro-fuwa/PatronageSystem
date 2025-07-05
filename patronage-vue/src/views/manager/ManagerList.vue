<template>
  <div>
    <!-- 页面头部 -->
    <div class="page-header">
      <h1 class="page-title">基金经理</h1>
      <p class="page-subtitle">支持通过基金经理姓名、标签筛选等方法，以基金经理为维度进行查询</p>
    </div>
    
    <!-- 搜索表单 -->
    <div class="search-form">
      <el-form :model="searchForm" inline>
        <el-form-item label="关键词">
          <el-input
            v-model="searchForm.keyword"
            placeholder="经理代码、经理姓名"
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
    
    <!-- 经理列表 -->
    <div class="card-container">
      <div v-if="loading" style="text-align: center; padding: 40px;">
        <el-loading />
      </div>
      
      <div v-else-if="managerList.length === 0" style="text-align: center; padding: 40px; color: #909399;">
        暂无数据
      </div>
      
      <div v-else>
        <el-table :data="managerList" style="width: 100%">
          <el-table-column prop="managerCode" label="经理代码" width="120" />
          <el-table-column prop="managerName" label="经理姓名" width="120" />
          <el-table-column prop="companyName" label="所属公司" min-width="200" />
          <el-table-column prop="education" label="学历" width="120" />
          <el-table-column prop="experienceYears" label="从业年限" width="100">
            <template #default="scope">
              {{ scope.row.experienceYears }}年
            </template>
          </el-table-column>
          <el-table-column prop="introduction" label="个人简介" min-width="200" show-overflow-tooltip />
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="scope">
              <el-button type="primary" size="small" @click="showFunds(scope.row.id)">
                查看基金
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <el-dialog v-model="showFundDialog" title="管理基金列表" width="800px">
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
import { getManagerList, searchManagers } from '@/api/manager'
import { getFundsByManager } from '@/api/fund'

const router = useRouter()

// 搜索表单
const searchForm = reactive({
  keyword: ''
})

// 经理列表
const managerList = ref([])
const loading = ref(false)
const showFundDialog = ref(false)
const fundList = ref([])

// 获取经理列表
const getManagers = async () => {
  try {
    loading.value = true
    const res = await getManagerList()
    managerList.value = res.data
  } catch (error) {
    console.error('获取经理列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 搜索经理
const handleSearch = async () => {
  try {
    loading.value = true
    const params = {}
    
    if (searchForm.keyword) params.keyword = searchForm.keyword
    
    const res = await searchManagers(params)
    managerList.value = res.data
  } catch (error) {
    console.error('搜索经理失败:', error)
  } finally {
    loading.value = false
  }
}

// 重置搜索
const handleReset = () => {
  searchForm.keyword = ''
  getManagers()
}

// 查看经理基金
const showFunds = (managerId) => {
  getFundsByManager(managerId).then(res => {
    fundList.value = res.data
    showFundDialog.value = true
  })
}

// 初始化
onMounted(() => {
  getManagers()
})
</script> 