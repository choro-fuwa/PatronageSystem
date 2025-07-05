<template>
  <div style="max-width: 700px; margin: 0 auto;">
    <el-descriptions title="调仓计划详情" :column="1" border>
      <el-descriptions-item label="ID">{{ plan.id }}</el-descriptions-item>
      <el-descriptions-item label="计划名称">{{ plan.planName }}</el-descriptions-item>
      <el-descriptions-item label="组合名称">{{ plan.portfolioName }}</el-descriptions-item>
      <el-descriptions-item label="组合类型">{{ plan.portfolioType }}</el-descriptions-item>
      <el-descriptions-item label="当前持仓">{{ plan.currentHoldings }}</el-descriptions-item>
      <el-descriptions-item label="状态">{{ plan.status }}</el-descriptions-item>
      <el-descriptions-item label="创建时间">{{ plan.createTime }}</el-descriptions-item>
      <el-descriptions-item label="更新时间">{{ plan.updateTime }}</el-descriptions-item>
      <el-descriptions-item label="交易指令">
        <el-input type="textarea" :rows="6" :value="prettyInstructions" readonly />
      </el-descriptions-item>
    </el-descriptions>
    <el-button style="margin-top: 20px;" @click="goBack">返回</el-button>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getRebalancePlanById } from '@/api/rebalancePlan';

const plan = ref({});
const route = useRoute();
const router = useRouter();

const fetchDetail = async () => {
  const res = await getRebalancePlanById(route.params.id);
  plan.value = res;
};

const prettyInstructions = computed(() => {
  try {
    return JSON.stringify(JSON.parse(plan.value.rebalanceInstructions || '[]'), null, 2);
  } catch {
    return plan.value.rebalanceInstructions || '';
  }
});

const goBack = () => {
  router.push({ name: 'RebalancePlanList' });
};

onMounted(fetchDetail);
</script> 