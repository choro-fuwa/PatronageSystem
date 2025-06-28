<template>
  <div class="strategy-edit">
    <div class="page-header">
      <div class="header-content">
        <div class="back-button" @click="goBack">
          <el-icon><ArrowLeft /></el-icon>
          返回
        </div>
        <h1>编辑策略</h1>
        <div class="header-actions">
          <el-button @click="resetForm">重置</el-button>
          <el-button type="primary" @click="saveStrategy">保存</el-button>
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
      <!-- 基本信息 -->
      <el-card class="form-card">
        <template #header>
          <span>基本信息</span>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="策略名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入策略名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="策略类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择策略类型" style="width: 100%">
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
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getStrategyDetail, updateStrategy } from '@/api/strategy'

export default {
  name: 'StrategyEdit',
  setup() {
    const route = useRoute()
    const router = useRouter()
    const strategyId = route.params.id

    const formRef = ref(null)
    const form = reactive({
      name: '',
      type: '',
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
      name: [
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

    const totalWeight = computed(() => {
      return form.assets.reduce((sum, asset) => sum + (asset.targetWeight || 0), 0)
    })

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

          updateStrategy(strategyId, form).then(response => {
            if (response.code === 200) {
              ElMessage.success('策略更新成功')
              router.push(`/strategy/detail/${strategyId}`)
            } else {
              ElMessage.error(response.message || '更新失败')
            }
          }).catch(error => {
            ElMessage.error('更新失败：' + error.message)
          })
        }
      })
    }

    const goBack = () => {
      router.back()
    }

    onMounted(() => {
      // 加载策略详情
      getStrategyDetail(strategyId).then(response => {
        if (response.code === 200) {
          const data = response.data
          Object.assign(form, {
            name: data.name,
            type: data.type,
            riskLevel: data.riskLevel,
            initialCapital: data.initialCapital,
            description: data.description,
            rebalancePeriod: data.rebalancePeriod || 'MONTHLY',
            weightDeviationThreshold: data.weightDeviationThreshold || 5,
            stopLossThreshold: data.stopLossThreshold || 10,
            takeProfitThreshold: data.takeProfitThreshold || 20,
            maxDrawdownAlert: data.maxDrawdownAlert || 15,
            volatilityAlert: data.volatilityAlert || 25,
            maxSingleAssetWeight: data.maxSingleAssetWeight || 30,
            industryConcentrationLimit: data.industryConcentrationLimit || 40,
            leverageLimit: data.leverageLimit || 1,
            liquidityRequirement: data.liquidityRequirement || 20
          })
          
          // 加载资产配置
          if (data.assets && data.assets.length > 0) {
            form.assets = data.assets
          } else {
            // 默认资产配置
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
          }
        }
      }).catch(error => {
        ElMessage.error('加载策略详情失败：' + error.message)
      })
    })

    return {
      formRef,
      form,
      rules,
      totalWeight,
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
.strategy-edit {
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

.form-card {
  margin-bottom: 20px;
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