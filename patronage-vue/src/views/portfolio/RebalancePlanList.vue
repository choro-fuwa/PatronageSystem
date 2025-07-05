<template>
  <div>
    <el-button type="primary" @click="goToCreate">新建调仓计划</el-button>
    <el-table :data="plans" style="width: 100%; margin-top: 20px;">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="planName" label="计划名称" />
      <el-table-column prop="portfolioName" label="组合名称" />
      <el-table-column prop="portfolioType" label="组合类型" />
      <el-table-column prop="status" label="状态" />
      <el-table-column label="操作" width="260">
        <template #default="scope">
          <el-button size="small" @click="viewDetail(scope.row)">详情</el-button>
          <el-button size="small" type="success" @click="dispatchPlan(scope.row)" :disabled="scope.row.status==='DISPATCHED'">下发</el-button>
          <el-button size="small" type="danger" @click="deletePlan(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useRouter } from 'vue-router';
import { getRebalancePlanList, dispatchRebalancePlan, deleteRebalancePlan } from '@/api/rebalancePlan';

const plans = ref([]);
const router = useRouter();

const fetchPlans = async () => {
  const res = await getRebalancePlanList();
  plans.value = res;
};

const goToCreate = () => {
  router.push({ name: 'RebalancePlanCreate' });
};

const viewDetail = (row) => {
  router.push({ name: 'RebalancePlanDetail', params: { id: row.id } });
};

const dispatchPlan = async (row) => {
  await dispatchRebalancePlan(row.id);
  ElMessage.success('下发成功');
  fetchPlans();
};

const deletePlan = (row) => {
  ElMessageBox.confirm('确定要删除该调仓计划吗？', '提示', { type: 'warning' })
    .then(async () => {
      await deleteRebalancePlan(row.id);
      ElMessage.success('删除成功');
      fetchPlans();
    });
};

onMounted(fetchPlans);
</script> 