<template>
  <el-card style="max-width: 1200px; margin: 40px auto;">
    <template #header>
      <span>差错处理列表</span>
    </template>
    <el-table :data="orders" style="width: 100%;">
      <el-table-column prop="orderCode" label="订单编号" width="150" />
      <el-table-column prop="accountId" label="账户ID" width="100" />
      <el-table-column prop="fundId" label="基金ID" width="100" />
      <el-table-column prop="orderType" label="订单类型" width="100" />
      <el-table-column prop="orderStatus" label="订单状态" width="120" />
      <el-table-column prop="orderPrice" label="委托价格" width="120" />
      <el-table-column prop="orderQuantity" label="委托数量" width="120" />
      <el-table-column prop="cancelReason" label="失败原因" width="200">
        <template #default="scope">
          <el-input v-model="scope.row.cancelReason" size="small" placeholder="请输入失败原因" />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button size="small" type="primary" @click="reprocess(scope.row)">重新下发</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { getFailedOrders, reprocessOrder } from '@/api/errorHandling';

const orders = ref([]);

const fetchOrders = async () => {
  const res = await getFailedOrders();
  orders.value = res;
};

const reprocess = async (row) => {
  await reprocessOrder(row.id, row.cancelReason);
  ElMessage.success('已重新下发');
  fetchOrders();
};

onMounted(fetchOrders);
</script> 