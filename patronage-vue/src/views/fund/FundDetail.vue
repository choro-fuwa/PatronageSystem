<template>
  <div>
    <!-- 页面头部 -->
    <div class="page-header">
      <div style="display: flex; align-items: center; gap: 10px;">
        <el-button @click="$router.go(-1)" icon="ArrowLeft">返回</el-button>
        <h1 class="page-title">基金画像</h1>
      </div>
      <p class="page-subtitle">基金产品信息全维度展示</p>
    </div>
    
    <div v-if="loading" style="text-align: center; padding: 40px;">
      <el-loading />
    </div>
    
    <div v-else-if="!fund" style="text-align: center; padding: 40px; color: #909399;">
      基金不存在
    </div>
    
    <div v-else>
      <!-- 基本信息 -->
      <div class="card-container">
        <h3 style="margin-bottom: 20px; color: #303133;">基本信息</h3>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>基金代码：</label>
              <span>{{ fund.fundCode }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>基金名称：</label>
              <span>{{ fund.fundName }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>基金简称：</label>
              <span>{{ fund.fundShortName }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>基金类型：</label>
              <span>{{ fund.fundType }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>风险等级：</label>
              <el-tag :type="getRiskLevelType(fund.riskLevel)">{{ fund.riskLevel }}</el-tag>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>成立日期：</label>
              <span>{{ fund.establishDate }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>基金公司：</label>
              <span>{{ fund.companyName }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>基金经理：</label>
              <span>{{ fund.managerName }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>基金规模：</label>
              <span>{{ formatFundSize(fund.fundSize) }}</span>
            </div>
          </el-col>
        </el-row>
      </div>
      
      <!-- 业绩表现 -->
      <div class="card-container" style="margin-top: 20px;">
        <h3 style="margin-bottom: 20px; color: #303133;">业绩表现</h3>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="performance-item">
              <div class="performance-label">最新净值</div>
              <div class="performance-value">{{ fund.nav }}</div>
              <div class="performance-date">{{ fund.navDate }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="performance-item">
              <div class="performance-label">累计收益率</div>
              <div class="performance-value" :class="fund.totalReturn >= 0 ? 'positive' : 'negative'">
                {{ fund.totalReturn }}%
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="performance-item">
              <div class="performance-label">年化收益率</div>
              <div class="performance-value" :class="fund.annualReturn >= 0 ? 'positive' : 'negative'">
                {{ fund.annualReturn }}%
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="performance-item">
              <div class="performance-label">最大回撤</div>
              <div class="performance-value negative">{{ fund.maxDrawdown }}%</div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20" style="margin-top: 20px;">
          <el-col :span="6">
            <div class="performance-item">
              <div class="performance-label">夏普比率</div>
              <div class="performance-value">{{ fund.sharpeRatio }}</div>
            </div>
          </el-col>
        </el-row>
      </div>
      
      <!-- 基金标签 -->
      <div v-if="fund.tags && fund.tags.length > 0" class="card-container" style="margin-top: 20px;">
        <h3 style="margin-bottom: 20px; color: #303133;">基金标签</h3>
        <div class="tag-list">
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
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getFundById } from '@/api/fund'

const route = useRoute()

const fund = ref(null)
const loading = ref(false)

// 获取基金详情
const getFundDetail = async () => {
  try {
    loading.value = true
    const res = await getFundById(route.params.id)
    fund.value = res.data
  } catch (error) {
    console.error('获取基金详情失败:', error)
  } finally {
    loading.value = false
  }
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

// 格式化基金规模
const formatFundSize = (size) => {
  if (!size) return '-'
  if (size >= 100000000) {
    return (size / 100000000).toFixed(2) + '亿'
  } else if (size >= 10000) {
    return (size / 10000).toFixed(2) + '万'
  }
  return size.toFixed(2)
}

// 初始化
onMounted(() => {
  getFundDetail()
})
</script>

<style scoped>
.info-item {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}

.info-item label {
  font-weight: 600;
  color: #606266;
  width: 100px;
  flex-shrink: 0;
}

.info-item span {
  color: #303133;
}

.performance-item {
  text-align: center;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
}

.performance-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.performance-value {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 5px;
}

.performance-value.positive {
  color: #67c23a;
}

.performance-value.negative {
  color: #f56c6c;
}

.performance-date {
  font-size: 12px;
  color: #909399;
}
</style> 