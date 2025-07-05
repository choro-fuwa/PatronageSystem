<template>
  <div class="legacy-button-container">
    <el-button type="primary" @click="redirectToLegacyPage">
      跳转到百度地图的页面
    </el-button>
  </div>
</template>

<script setup>
const redirectToLegacyPage = () => {
  window.location.href = 'http://localhost:8080'
}
</script>

<style scoped>
.legacy-button-container {
  margin: 20px;
}
</style> 