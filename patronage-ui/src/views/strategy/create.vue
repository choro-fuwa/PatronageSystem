<template>
  <div class="strategy-create">
    <div class="page-header">
      <div class="header-content">
        <div class="back-button" @click="goBack">
          <el-icon><ArrowLeft /></el-icon>
          返回
        </div>
        <h1>创建策略</h1>
        <div class="header-actions">
          <el-button @click="resetForm">重置</el-button>
          <el-button type="primary" @click="saveStrategy">保存策略</el-button>
        </div>
      </div>
    </div>

    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="120px"
      class="strategy-form"
    >
      <!-- 策略模板选择 -->
      <el-card class="template-card">
        <template #header>
          <span>选择策略模板</span>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="6" v-for="template in strategyTemplates" :key="template.id">
            <div 
              class="template-item"
              :class="{ active: selectedTemplate === template.id }"
              @click="selectTemplate(template)"
            >
              <div class="template-icon">
                <el-icon><component :is="template.icon" /></el-icon>
              </div>
              <div class="template-info">
                <h4>{{ template.name }}</h4>
                <p>{{ template.description }}</p>
                <div class="template-tags">
                  <el-tag size="small" :type="getRiskTag(template.riskLevel)">
                    {{ getRiskText(template.riskLevel) }}
                  </el-tag>
                  <el-tag size="small" type="info">{{ template.type }}</el-tag>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 基本信息 -->
      <el-card class="form-card">
        <template #header>
          <span>基本信息</span>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="策略名称" prop="strategyName">
              <el-input v-model="form.strategyName" placeholder="请输入策略名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="策略类型" prop="strategyType">
              <el-select v-model="form.strategyType" placeholder="请选择策略类型" style="width: 100%">
                <el-option label="大类资产配置" value="ASSET_ALLOCATION" />
                <el-option label="FOF组合管理" value="FOF" />
                <el-option label="基金指数组合" value="INDEX_COPY" />
                <el-option label="择时组合" value="TIMING" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="form.riskLevel" placeholder="请选择风险等级" style="width: 100%">
                <el-option label="低风险" value="LOW" />
                <el-option label="中风险" value="MEDIUM" />
                <el-option label="高风险" value="HIGH" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="初始资金" prop="initialCapital">
              <el-input-number
                v-model="form.initialCapital"
                :min="10000"
                :max="100000000"
                :step="10000"
                style="width: 100%"
                placeholder="请输入初始资金"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="策略描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="4"
            placeholder="请输入策略描述"
          />
        </el-form-item>
      </el-card>

      <!-- 策略参数 -->
      <el-card class="form-card">
        <template #header>
          <span>策略参数</span>
        </template>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="调仓周期" prop="rebalancePeriod">
              <el-select v-model="form.rebalancePeriod" placeholder="请选择调仓周期" style="width: 100%">
                <el-option label="每日" value="DAILY" />
                <el-option label="每周" value="WEEKLY" />
                <el-option label="每月" value="MONTHLY" />
                <el-option label="每季度" value="QUARTERLY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="权重偏离阈值" prop="weightDeviationThreshold">
              <el-input-number
                v-model="form.weightDeviationThreshold"
                :min="0.1"
                :max="10"
                :step="0.1"
                style="width: 100%"
                placeholder="权重偏离阈值(%)"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="止损阈值" prop="stopLossThreshold">
              <el-input-number
                v-model="form.stopLossThreshold"
                :min="0.1"
                :max="50"
                :step="0.1"
                style="width: 100%"
                placeholder="止损阈值(%)"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="止盈阈值" prop="takeProfitThreshold">
              <el-input-number
                v-model="form.takeProfitThreshold"
                :min="0.1"
                :max="100"
                :step="0.1"
                style="width: 100%"
                placeholder="止盈阈值(%)"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="最大回撤预警" prop="maxDrawdownAlert">
              <el-input-number
                v-model="form.maxDrawdownAlert"
                :min="0.1"
                :max="50"
                :step="0.1"
                style="width: 100%"
                placeholder="最大回撤预警(%)"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="波动率预警" prop="volatilityAlert">
              <el-input-number
                v-model="form.volatilityAlert"
                :min="0.1"
                :max="100"
                :step="0.1"
                style="width: 100%"
                placeholder="波动率预警(%)"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- 资产配置 -->
      <el-card class="form-card">
        <template #header>
          <span>资产配置</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="addAsset">
            添加资产
          </el-button>
        </template>

        <el-table :data="form.assets" style="width: 100%">
          <el-table-column label="标的代码" width="150">
            <template #default="scope">
              <el-input v-model="scope.row.symbol" placeholder="标的代码" />
            </template>
          </el-table-column>
          <el-table-column label="标的名称" width="200">
            <template #default="scope">
              <el-input v-model="scope.row.symbolName" placeholder="标的名称" />
            </template>
          </el-table-column>
          <el-table-column label="标的类型" width="120">
            <template #default="scope">
              <el-select v-model="scope.row.symbolType" placeholder="类型" style="width: 100%">
                <el-option label="股票" value="STOCK" />
                <el-option label="债券" value="BOND" />
                <el-option label="基金" value="FUND" />
                <el-option label="ETF" value="ETF" />
                <el-option label="黄金" value="GOLD" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="目标权重" width="120">
            <template #default="scope">
              <el-input-number
                v-model="scope.row.targetWeight"
                :min="0"
                :max="100"
                :step="0.1"
                style="width: 100%"
                placeholder="权重(%)"
              />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template #default="scope">
              <el-button type="danger" size="small" @click="removeAsset(scope.$index)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="weight-summary">
          <span>权重总和: {{ totalWeight }}%</span>
          <span v-if="totalWeight !== 100" class="weight-warning">
            (权重总和应为100%)
          </span>
        </div>
      </el-card>

      <!-- 风控设置 -->
      <el-card class="form-card">
        <template #header>
          <span>风控设置</span>
        </template>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="单资产最大权重" prop="maxSingleAssetWeight">
              <el-input-number
                v-model="form.maxSingleAssetWeight"
                :min="0"
                :max="100"
                :step="0.1"
                style="width: 100%"
                placeholder="单资产最大权重(%)"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="行业集中度限制" prop="industryConcentrationLimit">
              <el-input-number
                v-model="form.industryConcentrationLimit"
                :min="0"
                :max="100"
                :step="0.1"
                style="width: 100%"
                placeholder="行业集中度限制(%)"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="杠杆限制" prop="leverageLimit">
              <el-input-number
                v-model="form.leverageLimit"
                :min="0"
                :max="3"
                :step="0.1"
                style="width: 100%"
                placeholder="杠杆限制"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="流动性要求" prop="liquidityRequirement">
              <el-input-number
                v-model="form.liquidityRequirement"
                :min="0"
                :max="100"
                :step="0.1"
                style="width: 100%"
                placeholder="流动性要求(%)"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
    </el-form>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  ArrowLeft, 
  DataAnalysis, 
  VideoPlay, 
  Money, 
  TrendCharts 
} from '@element-plus/icons-vue'
import { createStrategy } from '@/api/strategy'

export default {
  name: 'StrategyCreate',
  setup() {
    const router = useRouter()
    const formRef = ref(null)
    const selectedTemplate = ref(null)

    const form = reactive({
      strategyName: '',
      strategyType: '',
      riskLevel: '',
      initialCapital: 1000000,
      description: '',
      rebalancePeriod: 'MONTHLY',
      weightDeviationThreshold: 5,
      stopLossThreshold: 10,
      takeProfitThreshold: 20,
      maxDrawdownAlert: 15,
      volatilityAlert: 25,
      maxSingleAssetWeight: 30,
      industryConcentrationLimit: 40,
      leverageLimit: 1,
      liquidityRequirement: 20,
      assets: []
    })

    const rules = {
      strategyName: [
        { required: true, message: '请输入策略名称', trigger: 'blur' },
        { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
      ],
      type: [
        { required: true, message: '请选择策略类型', trigger: 'change' }
      ],
      riskLevel: [
        { required: true, message: '请选择风险等级', trigger: 'change' }
      ],
      initialCapital: [
        { required: true, message: '请输入初始资金', trigger: 'blur' },
        { type: 'number', min: 10000, message: '初始资金不能少于10000', trigger: 'blur' }
      ],
      rebalancePeriod: [
        { required: true, message: '请选择调仓周期', trigger: 'change' }
      ]
    }

    const strategyTemplates = ref([
      {
        id: 1,
        name: '稳健股债轮动',
        description: '基于宏观经济周期和估值水平，动态调整股票和债券配置比例的稳健型策略',
        strategyType: '大类资产配置',
        riskLevel: 'MEDIUM',
        icon: 'DataAnalysis',
        defaultAssets: [
          { symbol: '000300.SH', symbolName: '沪深300ETF', symbolType: 'ETF', targetWeight: 60 },
          { symbol: '019547.SH', symbolName: '国债ETF', symbolType: 'ETF', targetWeight: 40 }
        ]
      },
      {
        id: 2,
        name: 'FOF组合管理',
        description: '通过精选优质基金，构建多元化投资组合，实现风险分散和收益优化',
        strategyType: 'FOF组合',
        riskLevel: 'MEDIUM',
        icon: 'VideoPlay',
        defaultAssets: [
          { symbol: '000300.SH', symbolName: '沪深300ETF', symbolType: 'ETF', targetWeight: 40 },
          { symbol: '000905.SH', symbolName: '中证500ETF', symbolType: 'ETF', targetWeight: 30 },
          { symbol: '019547.SH', symbolName: '国债ETF', symbolType: 'ETF', targetWeight: 30 }
        ]
      },
      {
        id: 3,
        name: '基金指数组合',
        description: '跟踪主流指数，通过被动投资方式获取市场平均收益',
        strategyType: '基金指数组合',
        riskLevel: 'LOW',
        icon: 'Money',
        defaultAssets: [
          { symbol: '000300.SH', symbolName: '沪深300ETF', symbolType: 'ETF', targetWeight: 50 },
          { symbol: '000905.SH', symbolName: '中证500ETF', symbolType: 'ETF', targetWeight: 30 },
          { symbol: '000852.SH', symbolName: '中证1000ETF', symbolType: 'ETF', targetWeight: 20 }
        ]
      },
      {
        id: 4,
        name: '择时组合',
        description: '基于技术分析和市场情绪，进行择时操作，追求超额收益',
        strategyType: '择时策略',
        riskLevel: 'HIGH',
        icon: 'TrendCharts',
        defaultAssets: [
          { symbol: '000300.SH', symbolName: '沪深300ETF', symbolType: 'ETF', targetWeight: 80 },
          { symbol: '019547.SH', symbolName: '国债ETF', symbolType: 'ETF', targetWeight: 20 }
        ]
      }
    ])

    const totalWeight = computed(() => {
      return form.assets.reduce((sum, asset) => sum + (asset.targetWeight || 0), 0)
    })

    const getRiskTag = (riskLevel) => {
      const riskMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger'
      }
      return riskMap[riskLevel] || 'default'
    }

    const getRiskText = (riskLevel) => {
      const riskMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险'
      }
      return riskMap[riskLevel] || riskLevel
    }

    const selectTemplate = (template) => {
      selectedTemplate.value = template.id
      
      // 应用模板配置
      form.strategyType = template.type === '大类资产配置' ? 'ASSET_ALLOCATION' : 
                          template.type === 'FOF组合' ? 'FOF' :
                          template.type === '基金指数组合' ? 'INDEX_COPY' : 'TIMING'
      form.riskLevel = template.riskLevel
      form.description = template.description
      form.assets = JSON.parse(JSON.stringify(template.defaultAssets))
      
      ElMessage.success(`已选择模板: ${template.name}`)
    }

    const addAsset = () => {
      form.assets.push({
        symbol: '',
        symbolName: '',
        symbolType: 'ETF',
        targetWeight: 0
      })
    }

    const removeAsset = (index) => {
      form.assets.splice(index, 1)
    }

    const resetForm = () => {
      ElMessageBox.confirm('确定要重置表单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        formRef.value.resetFields()
        form.assets = []
        selectedTemplate.value = null
        ElMessage.success('表单已重置')
      })
    }

    const saveStrategy = () => {
      formRef.value.validate((valid) => {
        if (valid) {
          if (totalWeight.value !== 100) {
            ElMessage.warning('资产权重总和必须为100%')
            return
          }
          console.log('提交数据', form)
          createStrategy(form).then(response => {
            if (response.code === 200) {
              ElMessage.success('策略创建成功')
              router.push('/strategy')
            } else {
              ElMessage.error(response.message || '创建失败')
            }
          }).catch(error => {
            ElMessage.error('创建失败：' + error.message)
          })
        }
      })
    }

    const goBack = () => {
      router.back()
    }

    onMounted(() => {
      // 初始化默认资产配置
      form.assets = [
        {
          symbol: '000300.SH',
          symbolName: '沪深300ETF',
          symbolType: 'ETF',
          targetWeight: 60
        },
        {
          symbol: '019547.SH',
          symbolName: '国债ETF',
          symbolType: 'ETF',
          targetWeight: 40
        }
      ]
    })

    return {
      formRef,
      form,
      rules,
      selectedTemplate,
      strategyTemplates,
      totalWeight,
      getRiskTag,
      getRiskText,
      selectTemplate,
      addAsset,
      removeAsset,
      resetForm,
      saveStrategy,
      goBack
    }
  }
}
</script>

<style scoped>
.strategy-create {
  padding: 20px;
}

.page-header {
  margin-bottom: 30px;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.back-button {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: #1890ff;
  font-size: 14px;
}

.header-content h1 {
  margin: 0;
  color: #303133;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.strategy-form {
  max-width: 1200px;
  margin: 0 auto;
}

.template-card,
.form-card {
  margin-bottom: 20px;
}

.template-item {
  border: 2px solid #f0f0f0;
  border-radius: 8px;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s;
  text-align: center;
  
  &:hover {
    border-color: #1890ff;
    background-color: #f8f9fa;
  }
  
  &.active {
    border-color: #1890ff;
    background-color: #e6f7ff;
  }
  
  .template-icon {
    font-size: 32px;
    color: #1890ff;
    margin-bottom: 12px;
  }
  
  .template-info {
    h4 {
      margin: 0 0 8px 0;
      color: #303133;
    }
    
    p {
      margin: 0 0 12px 0;
      color: #606266;
      font-size: 14px;
      line-height: 1.4;
    }
    
    .template-tags {
      display: flex;
      gap: 8px;
      justify-content: center;
    }
  }
}

.weight-summary {
  margin-top: 15px;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 4px;
  font-size: 14px;
}

.weight-warning {
  color: #f56c6c;
  margin-left: 10px;
}
</style> 