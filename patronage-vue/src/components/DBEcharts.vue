<template>
    <div class="chart-container" :style="{ height, width }" ref="chartContainer"></div>
  </template>
  
  <script setup>
  import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
  import * as echarts from 'echarts'
  
  const props = defineProps({
    chartOptions: {
      type: Object,
      default: () => ({}),
    },
    mapCityOption: {
      type: Object,
      default: () => ({}),
    },
    width: {
      type: String,
      default: '100%',
    },
    height: {
      type: String,
      default: '300px',
    },
  })
  
  const chartContainer = ref(null)
  let chartInstance = null
  
  const setOptions = (options) => {
    if (chartInstance) {
      chartInstance.setOption(options)
    }
  }
  
  onMounted(() => {
    nextTick(() => {
      chartInstance = echarts.init(chartContainer.value)
      // 注册地图
      if (props.mapCityOption && props.mapCityOption.name && props.mapCityOption.geoData) {
        echarts.registerMap(props.mapCityOption.name, props.mapCityOption.geoData)
      }
      setOptions(props.chartOptions)
      window.addEventListener('resize', handleResize)
    })
  })
  
  watch(
    () => props.chartOptions,
    (newVal) => {
      setOptions(newVal)
    },
    { deep: true }
  )
  
  onUnmounted(() => {
    window.removeEventListener('resize', handleResize)
    if (chartInstance) {
      chartInstance.dispose()
      chartInstance = null
    }
  })
  
  function handleResize() {
    if (chartInstance) {
      chartInstance.resize()
    }
  }
  </script>
  
  <style scoped>
  .chart-container {
    width: 100%;
    height: 100%;
  }
  </style>