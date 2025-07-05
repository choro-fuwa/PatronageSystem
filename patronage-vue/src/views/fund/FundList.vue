<template>
  <div>
    <!-- 页面头部 -->
    <div class="page-header">
      <h1 class="page-title">基金研究</h1>
      <p class="page-subtitle">查看和筛选全部公募基金，支持基金代码、标签筛选等方法</p>
    </div>
    
    <!-- 搜索表单 -->
    <div class="search-form">
      <el-form :model="searchForm" inline>
        <el-form-item label="关键词">
          <el-input
            v-model="searchForm.keyword"
            placeholder="基金代码、基金名称"
            style="width: 200px"
            clearable
          />
        </el-form-item>
        
        <el-form-item label="基金类型">
          <el-select
            v-model="searchForm.fundType"
            placeholder="请选择基金类型"
            style="width: 150px"
            clearable
          >
            <el-option label="股票型" value="股票型" />
            <el-option label="混合型" value="混合型" />
            <el-option label="债券型" value="债券型" />
            <el-option label="货币型" value="货币型" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="风险等级">
          <el-select
            v-model="searchForm.riskLevel"
            placeholder="请选择风险等级"
            style="width: 150px"
            clearable
          >
            <el-option label="低风险" value="低风险" />
            <el-option label="中风险" value="中风险" />
            <el-option label="中高风险" value="中高风险" />
            <el-option label="高风险" value="高风险" />
          </el-select>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <!-- 基金列表 -->
    <div class="card-container">
      <div v-if="loading" style="text-align: center; padding: 40px;">
        <el-loading />
      </div>
      
      <div v-else-if="fundList.length === 0" style="text-align: center; padding: 40px; color: #909399;">
        暂无数据
      </div>
      
      <div v-else>
        <div
          v-for="fund in fundList"
          :key="fund.id"
          class="fund-card"
          @click="handleFundClick(fund)"
        >
          <div class="fund-header">
            <div>
              <div class="fund-code">{{ fund.fundCode }}</div>
              <div class="fund-name">{{ fund.fundName }}</div>
              <div class="fund-company">{{ fund.companyName }} | {{ fund.managerName }}</div>
            </div>
            <div>
              <el-tag :type="getRiskLevelType(fund.riskLevel)">{{ fund.riskLevel }}</el-tag>
            </div>
          </div>
          
          <div class="fund-stats">
            <div class="stat-item">
              <div class="stat-label">最新净值</div>
              <div class="stat-value">{{ fund.nav }}</div>
            </div>
            <div class="stat-item">
              <div class="stat-label">累计收益</div>
              <div class="stat-value" :class="fund.totalReturn >= 0 ? 'positive' : 'negative'">
                {{ fund.totalReturn }}%
              </div>
            </div>
            <div class="stat-item">
              <div class="stat-label">年化收益</div>
              <div class="stat-value" :class="fund.annualReturn >= 0 ? 'positive' : 'negative'">
                {{ fund.annualReturn }}%
              </div>
            </div>
            <div class="stat-item">
              <div class="stat-label">最大回撤</div>
              <div class="stat-value negative">{{ fund.maxDrawdown }}%</div>
            </div>
            <div class="stat-item">
              <div class="stat-label">夏普比率</div>
              <div class="stat-value">{{ fund.sharpeRatio }}</div>
            </div>
          </div>
          
          <div v-if="fund.tags && fund.tags.length > 0" class="tag-list">
            <span
              v-for="tag in fund.tags"
              :key="tag"
              class="tag"
            >
              {{ tag }}
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getFundList, searchFunds } from '@/api/fund'

const router = useRouter()

// 搜索表单
const searchForm = reactive({
  keyword: '',
  fundType: '',
  riskLevel: ''
})

// 基金列表
const fundList = ref([])
const loading = ref(false)

// 获取基金列表
const getFunds = async () => {
  try {
    loading.value = true
    const res = await getFundList()
    fundList.value = res.data
  } catch (error) {
    console.error('获取基金列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 搜索基金
const handleSearch = async () => {
  try {
    loading.value = true
    const params = {}
    
    if (searchForm.keyword) params.keyword = searchForm.keyword
    if (searchForm.fundType) params.fundType = searchForm.fundType
    if (searchForm.riskLevel) params.riskLevel = searchForm.riskLevel
    
    const res = await searchFunds(params)
    fundList.value = res.data
  } catch (error) {
    console.error('搜索基金失败:', error)
  } finally {
    loading.value = false
  }
}

// 重置搜索
const handleReset = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = ''
  })
  getFunds()
}

// 基金点击处理
const handleFundClick = (fund) => {
  router.push(`/fund/${fund.id}`)
}

// 获取风险等级类型
const getRiskLevelType = (riskLevel) => {
  const typeMap = {
    '低风险': 'success',
    '中风险': 'warning',
    '中高风险': 'danger',
    '高风险': 'danger'
  }
  return typeMap[riskLevel] || 'info'
}

// 初始化
onMounted(() => {
  getFunds()
})
</script> 