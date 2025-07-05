<template>
  <section class="main-section">
    <div class="map-area">
      <div class="header-area">
        <div class="rank-title">实时访问统计（1小时统计一次）</div>
        <el-button 
          type="primary" 
          class="legacy-button" 
          @click="redirectToLegacyPage"
        >
          跳转到百度地图统计页面
        </el-button>
      </div>
      <DBEcharts
        v-if="chinaGeoJson"
        :mapCityOption="{ name: 'china', geoData: chinaGeoJson }"
        :chartOptions="state.chartOptions"
        height="calc(100vh - 130px)"
      />
      <div v-else>地图数据加载中...</div>
    </div>
    <div class="rank-area">
      <div class="rank-unit">单位：次</div>
      <div v-for="(item, idx) in barData" :key="item.name" class="rank-row">
        <span :class="['rank-num', idx < 3 ? 'top' : '']">{{ idx + 1 }}</span>
        <span class="rank-name">{{ item.name }}</span>
        <div class="rank-bar-bg">
          <div
            class="rank-bar"
            :class="idx < 3 ? 'top' : ''"
            :style="{ width: (item.value / barData[0].value * 100) + '%' }"
          ></div>
        </div>
        <span class="rank-value">{{ item.value }}</span>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import * as echarts from 'echarts'
import DBEcharts from '@/components/DBEcharts.vue'
import setting from '@/setting.js'

// 省份和初始数据
const provinces = [
  '新疆','湖北','浙江','台湾','天津','江苏','甘肃','辽宁','四川','山东','澳门','广东','江西','安徽','香港','云南','黑龙江','青海','内蒙古','福建','山西','北京','广西','陕西','海南','河北','上海','重庆','吉林','贵州','西藏','宁夏','湖南','河南'
]
const chinaGeoJson = ref(null)
const barData = ref(provinces.map(name => ({
  name,
  value: Math.floor(Math.random() * 1000) + 70
})))
barData.value.sort((a, b) => b.value - a.value)

const geoCoordMap = {
  '上海': [121.4648, 31.2891], '福建': [119.4543, 25.9222], '重庆': [106.504962, 29.533155],
  '北京': [116.405285, 39.904989], '辽宁': [123.429096, 41.796767], '河北': [114.502461, 38.045474],
  '天津': [117.190182, 39.125596], '山西': [112.549248, 37.857014], '陕西': [108.948024, 34.263161],
  '甘肃': [103.823557, 36.058039], '宁夏': [106.278179, 38.46637], '青海': [101.778916, 36.623178],
  '新疆': [87.617733, 43.792818], '西藏': [91.132212, 29.660361], '四川': [104.065735, 30.659462],
  '吉林': [125.3245, 43.886841], '山东': [117.000923, 36.675807], '河南': [113.665412, 34.757975],
  '江苏': [118.767413, 32.041544], '安徽': [117.283042, 31.86119], '湖北': [114.298572, 30.584355],
  '浙江': [120.153576, 30.287459], '江西': [115.892151, 28.676493], '湖南': [112.982279, 28.19409],
  '贵州': [106.713478, 26.578343], '云南': [102.712251, 25.040609], '广东': [113.280637, 23.125178],
  '广西': [108.320004, 22.82402], '海南': [110.33119, 20.031971], '黑龙江': [126.642464, 45.756967],
  '内蒙古': [111.670801, 40.818311], '香港': [114.173355, 22.320048], '澳门': [113.54909, 22.198951],
  '台湾': [121.509062, 25.044332]
}

const state = reactive({
  mapCityOption: {
    name: 'china',
    geoData: null
  },
  chartOptions: {
    animation: true,
    backgroundColor: '#f4f6fa',
    geo: {
      map: 'china',
      label: {
        show: true,
        color: '#234fa5',
        fontSize: 10,
        fontWeight: 'bold',
        emphasis: {
          color: '#fff',
          fontWeight: 'bold'
        }
      },
      roam: false,
      left: 'left',
      right: '0',
      zoom: .60,
      scaleLimit: { min: 0, max: 1 },
      itemStyle: {
        areaColor: '#e0ecff',
        borderColor: '#234fa5',
        borderWidth: 1.5,
        emphasis: { areaColor: '#3c38e4' }
      },
      tooltip: { show: false }
    },
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(50, 50, 50, 0.8)',
      borderColor: '#234fa5',
      borderWidth: 1,
      textStyle: { color: '#fff', fontSize: 14 },
      formatter: params => {
        if (params.seriesType === 'effectScatter') {
          return params.data.label;
        }
        return params.name + ' : ' + params.value;
      }
    },
    series: [
      // 多个 effectScatter
      ...[0, 1, 2].map(i => ({
        type: 'effectScatter',
        coordinateSystem: 'geo',
        z: 2,
        data: [],
        symbolSize: 22,
        label: {
          show: true,
          formatter: p => p.data.label,
          position: 'top',
          backgroundColor: i === 0 ? 'rgba(233,63,66,.9)' : (i === 1 ? 'rgba(254,174,33,.8)' : 'rgba(55, 79, 223,.9)'),
          padding: [0, 0],
          borderRadius: 3,
          lineHeight: 32,
          color: '#fff',
          fontSize: 10,
          fontWeight: 'bold'
        },
        itemStyle: {
          color: i === 0 ? '#e93f42' : (i === 1 ? '#feae21' : '#3c38e4')
        }
      })),
      {
        type: 'map',
        map: 'china',
        geoIndex: 0,
        z: 0,
        label: {
          show: true,
          color: '#234fa5',
          fontSize: 16,
          fontWeight: 'bold'
        },
        itemStyle: {
          areaColor: '#e0ecff',
          borderColor: '#234fa5',
          borderWidth: 1.5,
          emphasis: { areaColor: '#3c38e4' }
        },
        data: barData.value
      }
    ]
  }
})

// 生成随机访客名和电话
function getTel() {
  let telstr = '1'
  for (let i = 2; i < 12; i++) {
    if (i < 3) {
      let nums
      do {
        nums = Math.floor(Math.random() * 10)
      } while ([0, 1, 2, 3, 4, 6].includes(nums))
      telstr += nums
    } else if (i > 3 && i < 8) {
      telstr += '*'
    } else {
      telstr += Math.floor(Math.random() * 10)
    }
  }
  return telstr
}
function getName() {
  const names = ['张', '刘', '李', '邓', '熊', '田', '周', '赵', '钱', '孙', '吴', '郑', '王', '冯', '陈', '杨', '朱', '秦', '许', '徐', '何', '曹', '陶', '邹', '苏', '范', '彭', '鲁', '马', '方', '唐', '顾']
  const roundnum = Math.floor(Math.random() * names.length)
  return names[roundnum] + (Math.random() > 0.5 ? '先生' : '女士')
}
function getAddress(num, type) {
  const types = ['ADMIN', '微信小程序', '官网']
  return `在${barData.value[num].name} - 访问了${types[type]}`
}

// 动态更新地图和条形图
function updateChart() {
  // 随机高亮3个省份
  const used = new Set()
  for (let i = 0; i < 3; i++) {
    let idx
    do {
      idx = Math.floor(Math.random() * barData.value.length)
    } while (used.has(idx))
    used.add(idx)
    const name = barData.value[idx].name
    const coord = geoCoordMap[name]
    if (coord) {
      state.chartOptions.series[i].data = [{
        name,
        value: coord.concat(barData.value[idx].value),
        label: `访客：${getName()}  ${getTel()}\n${getAddress(idx, i)}`
      }]
    }
  }
  // 排序条形图
  barData.value.sort((a, b) => b.value - a.value)
  state.chartOptions.series[3].data = barData.value
}

onMounted(async () => {
  // 动态加载地图 geojson
  const res = await fetch('/geo/china.json')
  const geoJson = await res.json()
  chinaGeoJson.value = geoJson
  echarts.registerMap('china', geoJson)
  state.mapCityOption.geoData = geoJson
  updateChart()
  setInterval(updateChart, 3000)
})

const redirectToLegacyPage = () => {
  if (confirm('确定要跳转到实时访问统计页面吗？')) {
    window.open('http://localhost:8080', '_blank')
  }
}
</script>

<style scoped>
.header-area {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding: 0 20px;
}

.legacy-button {
  font-size: 14px;
}

.main-section {
  display: flex;
  align-items: flex-start;
  background: #f4f6fa;
  font-family: 'PingFang SC', 'Microsoft YaHei', 'Arial', 'Helvetica Neue', Helvetica, sans-serif;
  font-size: 10px; /* 全局基础字体更小 */
}
.map-area {
  flex: 1;
}
.rank-area {
  width: 320px;
  margin-left: 16px;
  display: flex;
  flex-direction: column;
  height: 100%;
}
@media (max-width: 900px) {
  .main-section {
    flex-direction: column;
  }
  .rank-area {
    width: 100%;
    margin-left: 0;
    margin-top: 20px;
  }
}
.rank-title {
  font-size: 18px;
  font-weight: bold;
  color: #234fa5;
  margin-top: 20px;
}
.rank-unit {
  color: #8a8a8a;
  margin-bottom: 8px;
  font-size: 12px;
}
.rank-row {
  display: flex;
  align-items: center;
  margin-bottom: 2px;
}
.rank-num {
  width: 22px;
  display: inline-block;
  text-align: right;
  color: #234fa5;
  font-size: 12px;
}
.rank-num.top {
  color: #f0515e;
  font-weight: bold;
  font-size: 14px;
}
.rank-name {
  width: 40px;
  margin-left: 4px;
  font-size: 13px;
}
.rank-bar-bg {
  flex: 1;
  margin: 0 6px;
  background: #e0ecff;
  border-radius: 8px;
  height: 12px;
  position: relative;
}
.rank-bar {
  height: 100%;
  border-radius: 8px;
  background: linear-gradient(90deg,#3c38e4,#24a5cd);
}
.rank-bar.top {
  background: linear-gradient(90deg,#f0515e,#ef8554);
}
.rank-value {
  width: 32px;
  text-align: right;
  color: #234fa5;
  font-size: 12px;
}
</style>