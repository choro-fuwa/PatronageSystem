<template>
    <div class="risk-profile-container">
      <el-card class="survey-card">
        <template #header>
          <div class="card-header">
            <el-icon><Document /></el-icon>
            <span>投资者风险承受能力评测</span>
          </div>
        </template>
  
        <div class="survey-form-wrapper">
          <el-form
            ref="formRef"
            :model="formData"
            :rules="rules"
            label-width="200px"
            class="survey-form"
          >
            <!-- 1. 年龄 -->
            <el-form-item label="1. 您的年龄是？" prop="age">
              <el-radio-group v-model="formData.age">
                <el-radio value="under_18">18周岁以下</el-radio>
                <el-radio value="18_30">18-30</el-radio>
                <el-radio value="31_40">31-40</el-radio>
                <el-radio value="41_50">41-50</el-radio>
                <el-radio value="51_64">51-64</el-radio>
                <el-radio value="65_above">年满或高于65岁</el-radio>
              </el-radio-group>
            </el-form-item>
  
            <!-- 2. 投资目标 -->
            <el-form-item label="2. 您的投资目标是？" prop="investmentGoal">
              <el-radio-group v-model="formData.investmentGoal">
                <el-radio value="education_retirement">子女教育费，退休计划</el-radio>
                <el-radio value="personal_goals">个人目标（如置业、购车）</el-radio>
                <el-radio value="wealth_growth">让财富保值增值</el-radio>
              </el-radio-group>
            </el-form-item>
  
            <!-- 3. 期望的投资回报 -->
            <el-form-item label="3. 您期望的投资回报是？" prop="expectedReturn">
              <el-radio-group v-model="formData.expectedReturn">
                <el-radio value="same_as_deposit">跟银行存款利率大体相同</el-radio>
                <el-radio value="slightly_higher">比定期存款利率稍高</el-radio>
                <el-radio value="much_higher">远超过定期存款利率</el-radio>
              </el-radio-group>
            </el-form-item>
  
            <!-- 4. 是否需要赎回 -->
            <el-form-item label="4. 您在金融产品持有期内是否有赎回产品的需求？" prop="redemptionNeed">
              <el-radio-group v-model="formData.redemptionNeed">
                <el-radio value="yes">有</el-radio>
                <el-radio value="not_sure">不确定</el-radio>
                <el-radio value="no">没有</el-radio>
              </el-radio-group>
            </el-form-item>
  
            <!-- 5. 不保本型产品风险 -->
            <el-form-item label="5. 对于不保本型金融产品，您可能无法获得任何收益且无法收回全部本金。您是否了解并接受？" prop="riskAcceptance">
              <el-radio-group v-model="formData.riskAcceptance">
                <el-radio value="yes">是</el-radio>
                <el-radio value="no">否</el-radio>
              </el-radio-group>
            </el-form-item>
  
            <!-- 6. 主要收入来源 -->
            <el-form-item label="6. 您的主要收入来源是？" prop="incomeSource">
              <el-radio-group v-model="formData.incomeSource">
                <el-radio value="no_fixed_income">无固定收入</el-radio>
                <el-radio value="non_financial_income">非金融投资收入</el-radio>
                <el-radio value="salary">工资薪金</el-radio>
                <el-radio value="business_income">经营收入</el-radio>
                <el-radio value="financial_income">金融投资收入</el-radio>
              </el-radio-group>
            </el-form-item>
  
            <!-- 7. 年收入范围 -->
            <el-form-item label="7. 您的年收入范围是？" prop="annualIncome">
              <el-radio-group v-model="formData.annualIncome">
                <el-radio value="under_50k">50万以下</el-radio>
                <el-radio value="50k_200k">50万-200万</el-radio>
                <el-radio value="200k_above">200万以上</el-radio>
              </el-radio-group>
            </el-form-item>
  
            <!-- 8. 投资资产占比 -->
            <el-form-item label="8. 您用于投资的资产占比大约为？" prop="investableProportion">
              <el-radio-group v-model="formData.investableProportion">
                <el-radio value="under_20%">20%以下</el-radio>
                <el-radio value="20%-50%">20%-50%</el-radio>
                <el-radio value="above_50%">50%以上</el-radio>
              </el-radio-group>
            </el-form-item>
  
            <!-- 9. 负债情况 -->
            <el-form-item label="9. 您目前的负债情况是？" prop="debtStatus">
              <el-radio-group v-model="formData.debtStatus">
                <el-radio value="none">无负债</el-radio>
                <el-radio value="manageable">负债率较低，可控范围内</el-radio>
                <el-radio value="high">负债率较高，有一定风险</el-radio>
              </el-radio-group>
            </el-form-item>
  
            <!-- 10. 投资知识 -->
            <el-form-item label="10. 您的投资知识水平是？" prop="investmentKnowledge">
              <el-radio-group v-model="formData.investmentKnowledge">
                <el-radio value="none">无相关知识</el-radio>
                <el-radio value="basic">基础知识</el-radio>
                <el-radio value="proficient">较专业</el-radio>
              </el-radio-group>
            </el-form-item>
  
            <!-- 11. 投资经验 -->
            <el-form-item label="11. 您的投资经验是？" prop="investmentExperience">
              <el-radio-group v-model="formData.investmentExperience">
                <el-radio value="none">无经验</el-radio>
                <el-radio value="limited">有限经验</el-radio>
                <el-radio value="rich">丰富经验</el-radio>
              </el-radio-group>
            </el-form-item>
  
            <!-- 12. 投资期限 -->
            <el-form-item label="12. 您的投资期限是？" prop="investmentDuration">
              <el-radio-group v-model="formData.investmentDuration">
                <el-radio value="short_term">短期（1年以内）</el-radio>
                <el-radio value="medium_term">中期（1-5年）</el-radio>
                <el-radio value="long_term">长期（5年以上）</el-radio>
              </el-radio-group>
            </el-form-item>
  
            <!-- 13. 偏好产品 -->
            <el-form-item label="13. 您偏好的投资产品是？（可多选）" prop="preferredProducts">
              <el-checkbox-group v-model="formData.preferredProducts">
                <el-checkbox value="savings">储蓄存款</el-checkbox>
                <el-checkbox value="fund">基金</el-checkbox>
                <el-checkbox value="stocks">股票</el-checkbox>
                <el-checkbox value="futures">期货</el-checkbox>
                <el-checkbox value="others">其他</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
  
            <!-- 14. 亏损容忍度 -->
            <el-form-item label="14. 您对投资亏损的容忍度是？" prop="lossTolerance">
              <el-radio-group v-model="formData.lossTolerance">
                <el-radio value="very_sensitive">非常敏感，不能接受亏损</el-radio>
                <el-radio value="neutral">中性，可以接受小幅亏损</el-radio>
                <el-radio value="risk_taker">风险偏好，可以接受较大亏损</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-form>
        </div>
        <div class="fixed-btns">
          <el-button type="primary" @click="submitForm" :loading="loading">提交问卷</el-button>
          <el-button @click="resetForm">重置</el-button>
        </div>
      </el-card>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive } from 'vue'
  import { ElMessage } from 'element-plus'
  import { Document } from '@element-plus/icons-vue'
  import axios from 'axios'
  
  const formRef = ref()
  const loading = ref(false)
  
  // 表单数据
  const formData = reactive({
    age: '',
    investmentGoal: '',
    expectedReturn: '',
    redemptionNeed: '',
    riskAcceptance: '',
    incomeSource: '',
    annualIncome: '',
    investableProportion: '',
    debtStatus: '',
    investmentKnowledge: '',
    investmentExperience: '',
    investmentDuration: '',
    preferredProducts: [],
    lossTolerance: ''
  })
  
  // 表单验证规则
  const rules = {
    age: [{ required: true, message: '请选择年龄范围', trigger: 'change' }],
    investmentGoal: [{ required: true, message: '请选择投资目标', trigger: 'change' }],
    expectedReturn: [{ required: true, message: '请选择期望回报', trigger: 'change' }],
    redemptionNeed: [{ required: true, message: '请选择赎回需求', trigger: 'change' }],
    riskAcceptance: [{ required: true, message: '请选择风险接受程度', trigger: 'change' }],
    incomeSource: [{ required: true, message: '请选择收入来源', trigger: 'change' }],
    annualIncome: [{ required: true, message: '请选择年收入范围', trigger: 'change' }],
    investableProportion: [{ required: true, message: '请选择投资资产占比', trigger: 'change' }],
    debtStatus: [{ required: true, message: '请选择负债情况', trigger: 'change' }],
    investmentKnowledge: [{ required: true, message: '请选择投资知识水平', trigger: 'change' }],
    investmentExperience: [{ required: true, message: '请选择投资经验', trigger: 'change' }],
    investmentDuration: [{ required: true, message: '请选择投资期限', trigger: 'change' }],
    preferredProducts: [{ required: true, message: '请选择偏好产品', trigger: 'change' }],
    lossTolerance: [{ required: true, message: '请选择亏损容忍度', trigger: 'change' }]
  }
  
  // 提交表单
  const submitForm = async () => {
    if (!formRef.value) return
  
    try {
      await formRef.value.validate()
      
      loading.value = true
      console.log('提交的调查问卷数据:', formData)
  
      const response = await axios.post('/api/risk-profile', formData)
      
      ElMessage.success('问卷提交成功！')
      console.log('表单提交成功:', response.data)
      
      // 重置表单
      resetForm()
      
    } catch (error) {
      if (error.response) {
        ElMessage.error(`提交失败: ${error.response.data.message || '服务器错误'}`)
      } else {
        ElMessage.error('提交失败，请稍后再试！')
      }
      console.error('提交失败:', error)
    } finally {
      loading.value = false
    }
  }
  
  // 重置表单
  const resetForm = () => {
    if (!formRef.value) return
    
    formRef.value.resetFields()
    Object.keys(formData).forEach(key => {
      if (Array.isArray(formData[key])) {
        formData[key] = []
      } else {
        formData[key] = ''
      }
    })
  }
  </script>
  
<style scoped>
.risk-profile-container, .survey-card {
  width: 100%;
  min-height: 100vh;
  box-sizing: border-box;
  border-radius: 0;
  padding: 0;
  background: linear-gradient(135deg, #f0faff, #e0f7fa, #ffffff);
}
.survey-card {
  box-shadow: none;
  background: #fff;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}
.survey-form-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  width: 100%;
  min-height: 100vh;
}
.survey-form {
  width: 100%;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 32px 40px;
  padding: 32px 0 0 0;
  box-sizing: border-box;
  overflow: visible;
}
.fixed-btns {
  position: fixed;
  right: 48px;
  bottom: 48px;
  z-index: 100;
  display: flex;
  gap: 24px;
  background: rgba(255,255,255,0.95);
  border-radius: 24px;
  box-shadow: 0 4px 24px rgba(124,77,255,0.10);
  padding: 16px 32px;
}
@media (max-width: 900px) {
  .fixed-btns {
    right: 12px;
    bottom: 12px;
    padding: 10px 12px;
    gap: 12px;
  }
}
.card-header {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2.2rem;
  font-weight: 900;
  color: #512da8;
  letter-spacing: 2px;
  padding: 32px 0 18px 0;
  border-bottom: 2px solid #ede7f6;
  background: linear-gradient(90deg, #ede7f6 0%, #fff 100%);
  margin-bottom: 18px;
}
.card-header .el-icon {
  margin-right: 16px;
  color: #ff9800;
  font-size: 38px;
}
.survey-form .el-form-item {
  margin-bottom: 0;
  border-bottom: none;
  padding-bottom: 0;
}
.survey-form .el-radio-group,
.survey-form .el-checkbox-group {
  gap: 18px;
}
.survey-form .el-button {
  padding: 16px 40px;
  border-radius: 24px;
  font-size: 18px;
  box-shadow: 0 4px 16px rgba(124,77,255,0.08);
  transition: background 0.2s, box-shadow 0.2s;
}
.survey-form .el-button--primary {
  background: linear-gradient(90deg, #7c4dff, #40c4ff);
  border: none;
  color: #fff;
  font-weight: bold;
}
.survey-form .el-button--primary:hover {
  background: linear-gradient(90deg, #512da8, #00bcd4);
  box-shadow: 0 6px 24px rgba(64,196,255,0.18);
}
</style>