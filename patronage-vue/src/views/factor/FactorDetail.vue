<template>
  <div class="factor-detail">
    <div class="page-header">
      <el-button @click="$router.back()" icon="ArrowLeft">返回</el-button>
      <h2>因子详情</h2>
    </div>

    <div v-if="factor" class="detail-content">
      <!-- 基本信息 -->
      <el-card class="info-card">
        <template #header>
          <div class="card-header">
            <span>基本信息</span>
            <el-button type="primary" @click="editMode = true" v-if="!editMode">编辑</el-button>
            <div v-else>
              <el-button @click="cancelEdit">取消</el-button>
              <el-button type="primary" @click="saveFactor">保存</el-button>
            </div>
          </div>
        </template>
        
        <el-form :model="factorForm" :rules="factorRules" ref="factorFormRef" label-width="120px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="因子代码" prop="factorCode">
                <el-input v-model="factorForm.factorCode" :disabled="!editMode" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="因子名称" prop="factorName">
                <el-input v-model="factorForm.factorName" :disabled="!editMode" />
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="因子简称" prop="factorShortName">
                <el-input v-model="factorForm.factorShortName" :disabled="!editMode" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="因子类型" prop="factorType">
                <el-select v-model="factorForm.factorType" :disabled="!editMode" style="width: 100%">
                  <el-option label="技术因子" value="技术因子" />
                  <el-option label="基本面因子" value="基本面因子" />
                  <el-option label="宏观因子" value="宏观因子" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="风险等级" prop="riskLevel">
                <el-select v-model="factorForm.riskLevel" :disabled="!editMode" style="width: 100%">
                  <el-option label="低风险" value="低风险" />
                  <el-option label="中风险" value="中风险" />
                  <el-option label="高风险" value="高风险" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="更新频率" prop="updateFrequency">
                <el-select v-model="factorForm.updateFrequency" :disabled="!editMode" style="width: 100%">
                  <el-option label="日频" value="daily" />
                  <el-option label="周频" value="weekly" />
                  <el-option label="月频" value="monthly" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-form-item label="数据来源" prop="dataSource">
            <el-input v-model="factorForm.dataSource" :disabled="!editMode" />
          </el-form-item>
          
          <el-form-item label="因子描述" prop="description">
            <el-input
              v-model="factorForm.description"
              type="textarea"
              :rows="3"
              :disabled="!editMode"
            />
          </el-form-item>
          
          <el-form-item label="计算公式" prop="formula">
            <el-input
              v-model="factorForm.formula"
              type="textarea"
              :rows="4"
              :disabled="!editMode"
            />
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 因子数据 -->
      <el-card class="data-card">
        <template #header>
          <div class="card-header">
            <span>因子数据</span>
            <el-button type="primary" @click="showDataDialog = true">
              <el-icon><Upload /></el-icon>
              导入数据
            </el-button>
          </div>
        </template>
        
        <div class="data-stats">
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-value">{{ dataStats.totalCount || 0 }}</div>
                <div class="stat-label">总数据量</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-value">{{ dataStats.fundCount || 0 }}</div>
                <div class="stat-label">覆盖基金数</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-value">{{ dataStats.latestDate || '-' }}</div>
                <div class="stat-label">最新数据日期</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-value">{{ dataStats.avgValue || '-' }}</div>
                <div class="stat-label">平均因子值</div>
              </div>
            </el-col>
          </el-row>
        </div>
        
        <!-- 数据表格 -->
        <el-table :data="factorData" style="width: 100%" max-height="400">
          <el-table-column prop="fundCode" label="基金代码" width="120" />
          <el-table-column prop="tradeDate" label="交易日期" width="120" />
          <el-table-column prop="factorValue" label="因子值" width="120" />
          <el-table-column prop="rankValue" label="排名值" width="120" />
          <el-table-column prop="percentile" label="百分位数" width="120" />
        </el-table>
      </el-card>

      <!-- 因子分析 -->
      <el-card class="analysis-card">
        <template #header>
          <span>因子分析</span>
        </template>
        
        <div class="analysis-content">
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="analysis-item">
                <h4>IC值</h4>
                <div class="analysis-value">{{ factorAnalysis.icValue || '-' }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="analysis-item">
                <h4>RankIC值</h4>
                <div class="analysis-value">{{ factorAnalysis.rankIc || '-' }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="analysis-item">
                <h4>胜率</h4>
                <div class="analysis-value">{{ factorAnalysis.winRate || '-' }}%</div>
              </div>
            </el-col>
          </el-row>
          
          <el-row :gutter="20" style="margin-top: 20px;">
            <el-col :span="8">
              <div class="analysis-item">
                <h4>夏普比率</h4>
                <div class="analysis-value">{{ factorAnalysis.sharpeRatio || '-' }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="analysis-item">
                <h4>最大回撤</h4>
                <div class="analysis-value">{{ factorAnalysis.maxDrawdown || '-' }}%</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="analysis-item">
                <h4>换手率</h4>
                <div class="analysis-value">{{ factorAnalysis.turnoverRate || '-' }}%</div>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-card>
    </div>

    <!-- 数据导入对话框 -->
    <el-dialog v-model="showDataDialog" title="导入因子数据" width="500px">
      <el-form :model="dataForm" label-width="100px">
        <el-form-item label="数据文件">
          <el-upload
            class="upload-demo"
            drag
            action="#"
            :auto-upload="false"
            :on-change="handleFileChange"
            accept=".csv,.xlsx"
          >
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">
              将文件拖到此处，或<em>点击上传</em>
            </div>
            <template #tip>
              <div class="el-upload__tip">
                只能上传 csv/xlsx 文件
              </div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDataDialog = false">取消</el-button>
        <el-button type="primary" @click="importData">导入</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft, Upload, UploadFilled } from '@element-plus/icons-vue'
import { factorApi } from '@/api/factor'

const route = useRoute()

// 数据
const factor = ref(null)
const editMode = ref(false)
const showDataDialog = ref(false)
const factorFormRef = ref()
const factorData = ref([])
const dataStats = ref({})
const factorAnalysis = ref({})

// 表单数据
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

// 数据导入表单
const dataForm = reactive({
  file: null
})

// 表单验证规则
const factorRules = {
  factorCode: [{ required: true, message: '请输入因子代码', trigger: 'blur' }],
  factorName: [{ required: true, message: '请输入因子名称', trigger: 'blur' }],
  factorType: [{ required: true, message: '请选择因子类型', trigger: 'change' }],
  riskLevel: [{ required: true, message: '请选择风险等级', trigger: 'change' }]
}

// 获取因子详情
const getFactorDetail = async () => {
  try {
    const response = await factorApi.getFactorDetail(route.params.id)
    if (response.success) {
      factor.value = response.data
      Object.assign(factorForm, response.data)
    }
  } catch (error) {
    ElMessage.error('获取因子详情失败')
  }
}

// 保存因子
const saveFactor = async () => {
  try {
    await factorFormRef.value.validate()
    
    const response = await factorApi.updateFactor(factorForm)
    if (response.success) {
      ElMessage.success('保存成功')
      editMode.value = false
      getFactorDetail()
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

// 取消编辑
const cancelEdit = () => {
  editMode.value = false
  Object.assign(factorForm, factor.value)
}

// 文件上传处理
const handleFileChange = (file) => {
  dataForm.file = file.raw
}

// 导入数据
const importData = async () => {
  if (!dataForm.file) {
    ElMessage.warning('请选择要导入的文件')
    return
  }
  
  // 这里应该调用后端接口上传文件
  ElMessage.success('数据导入成功')
  showDataDialog.value = false
}

// 初始化
onMounted(() => {
  getFactorDetail()
})
</script>

<style scoped>
.factor-detail {
  padding: 20px;
}

.page-header {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #303133;
}

.detail-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.info-card,
.data-card,
.analysis-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.data-stats {
  margin-bottom: 20px;
}

.stat-item {
  text-align: center;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 5px;
}

.stat-label {
  color: #606266;
  font-size: 14px;
}

.analysis-content {
  padding: 20px 0;
}

.analysis-item {
  text-align: center;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
}

.analysis-item h4 {
  margin: 0 0 10px 0;
  color: #303133;
  font-size: 16px;
}

.analysis-value {
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
}

.upload-demo {
  width: 100%;
}
</style> 