<template>
  <section>
    <div v-if="chinaGeoJson">
      <DBEcharts
        :mapCityOption="{ name: 'china', geoData: chinaGeoJson }"
        :chartOptions="chartOptions"
        height="calc(100vh - 130px)"
      />
    </div>
    <div v-else>地图数据加载中...</div>
  </section>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import setting from '@/setting.js'
import { adjustColorOpacity } from '@/utils/util.common.js'
import * as echarts from 'echarts'
import DBEcharts from '@/components/DBEcharts.vue'

const chinaGeoJson = ref(null)

const mapData = [
  { name: '北京', value: 100 }, { name: '天津', value: 80 }, { name: '上海', value: 120 }, { name: '重庆', value: 90 },
  { name: '河北', value: 60 }, { name: '山西', value: 70 }, { name: '辽宁', value: 110 }, { name: '吉林', value: 50 },
  { name: '黑龙江', value: 40 }, { name: '江苏', value: 130 }, { name: '浙江', value: 140 }, { name: '安徽', value: 75 },
  { name: '福建', value: 85 }, { name: '江西', value: 65 }, { name: '山东', value: 150 }, { name: '河南', value: 95 },
  { name: '湖北', value: 105 }, { name: '湖南', value: 115 }, { name: '广东', value: 200 }, { name: '海南', value: 30 },
  { name: '四川', value: 125 }, { name: '贵州', value: 55 }, { name: '云南', value: 45 }, { name: '陕西', value: 100 },
  { name: '甘肃', value: 35 }, { name: '青海', value: 25 }, { name: '台湾', value: 20 }, { name: '内蒙古', value: 60 },
  { name: '广西', value: 70 }, { name: '西藏', value: 10 }, { name: '宁夏', value: 15 }, { name: '新疆', value: 20 },
  { name: '香港', value: 5 }, { name: '澳门', value: 5 }
]

const chartOptions = reactive({
  title: {
    text: "中国地图",
    top: "10px",
    left: "center",
    textStyle: { color: '#b39ddb' }
  },
  tooltip: {
    trigger: 'item',
    backgroundColor: 'rgba(0, 0, 0, .6)',
    textStyle: { color: '#fff', fontSize: 12 }
  },
  series: [{
    type: 'map',
    map: 'china',
    zoom: 1.5,
    center: [104.0, 35.0],
    label: { show: true, color: '#b39ddb', fontSize: 13 },
    itemStyle: {
      areaColor: adjustColorOpacity(setting.theme.color, 10),
      borderColor: adjustColorOpacity(setting.theme.color, 80),
      emphasis: { areaColor: setting.theme.color }
    },
    data: mapData
  }]
})

onMounted(async () => {
  const res = await fetch('/geo/china.json')
  const geoJson = await res.json()
  chinaGeoJson.value = geoJson
  echarts.registerMap('china', geoJson)
})
</script>