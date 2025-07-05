<template>
  <el-card style="max-width: 600px; margin: 40px auto;">
    <template #header>
      <span>新建调仓计划</span>
    </template>
    <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" status-icon>
      <el-form-item label="计划名称" prop="planName">
        <el-input v-model="form.planName" placeholder="请输入计划名称" />
      </el-form-item>
      <el-form-item label="组合名称" prop="portfolioName">
        <el-input v-model="form.portfolioName" placeholder="请输入组合名称" />
      </el-form-item>
      <el-form-item label="组合类型" prop="portfolioType">
        <el-select v-model="form.portfolioType" placeholder="请选择组合类型">
          <el-option label="FOF" value="FOF" />
          <el-option label="组合" value="组合" />
          <el-option label="定制" value="定制" />
        </el-select>
      </el-form-item>
      <el-form-item label="当前持仓" prop="currentHoldings">
        <el-input v-model="form.currentHoldings" type="textarea" :rows="4" placeholder='如{"A股":100000,"债券":50000}' />
        <div style="color: #888; font-size: 12px; margin-top: 4px;">请输入JSON格式，如：{"A股":100000,"债券":50000}</div>
      </el-form-item>
      <el-form-item>
        <div style="width:100%;text-align:center;">
          <el-button type="primary" :loading="loading" @click="submit">提交</el-button>
          <el-button @click="goBack">返回</el-button>
        </div>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { createRebalancePlanWithInstructions } from '@/api/rebalancePlan';

const form = ref({
  planName: '',
  portfolioName: '',
  portfolioType: '',
  currentHoldings: ''
});
const rules = {
  planName: [{ required: true, message: '请输入计划名称', trigger: 'blur' }],
  portfolioName: [{ required: true, message: '请输入组合名称', trigger: 'blur' }],
  portfolioType: [{ required: true, message: '请选择组合类型', trigger: 'change' }],
  currentHoldings: [
    { required: true, message: '请输入当前持仓', trigger: 'blur' },
    { validator: (rule, value, callback) => {
        try {
          JSON.parse(value)
          callback()
        } catch {
          callback(new Error('请输入合法的JSON格式'))
        }
      }, trigger: 'blur' }
  ]
};
const formRef = ref();
const loading = ref(false);
const router = useRouter();

const submit = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return;
    loading.value = true;
    try {
      await createRebalancePlanWithInstructions(form.value);
      ElMessage.success('创建成功');
      form.value.planName = '';
      form.value.portfolioName = '';
      form.value.portfolioType = '';
      form.value.currentHoldings = '';
      router.push({ name: 'RebalancePlanList' });
    } catch (e) {
      ElMessage.error('创建失败');
    } finally {
      loading.value = false;
    }
  });
};

const goBack = () => {
  router.push({ name: 'RebalancePlanList' });
};
</script> 