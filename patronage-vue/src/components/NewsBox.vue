<template>
  <div class="layout">
    <!-- 主内容区  -->
    <div class="main-content">

      <!-- 新闻组件内容 -->
      <div class="enhanced-news-container">
        <div class="news-header-gradient">
          <div class="header-content">
            <h2 class="news-title">
              <el-icon class="title-icon"><Notification /></el-icon>
              <span class="title-text">实时财经快讯</span>
              <span class="beta-badge">LIVE</span>
            </h2>
            <div class="update-info">
              <el-button type="text" @click="loadNewsData" :loading="loading">
                <el-icon class="update-icon"><Refresh /></el-icon>
                <span class="update-text">{{ lastUpdateTime ? `最后更新: ${lastUpdateTime}` : '点击加载数据' }}</span>
              </el-button>
            </div>
          </div>
          <div class="header-wave"></div>
        </div>
        
        <div class="news-content-wrapper">
          <el-table 
            :data="paginatedNewsList" 
            class="premium-table"
            :empty-text="loading ? '数据加载中...' : '暂无数据'"
            v-loading="loading"
            :row-class-name="tableRowClassName">
            <el-table-column prop="time" label="时间" width="130">
              <template #header>
                <div class="table-header-cell">
                  <el-icon><Clock /></el-icon>
                  <span>发布时间</span>
                </div>
              </template>
              <template #default="{ row }">
                <div class="time-cell-badge">
                  <span class="time-bubble">{{ formatExcelTime(row.time) }}</span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="content" label="新闻内容">
              <template #header>
                <div class="table-header-cell">
                  <el-icon><Document /></el-icon>
                  <span>新闻内容</span>
                </div>
              </template>
              <template #default="{ row }">
                <div class="content-card">
                  <div class="content-text" v-html="highlightKeywords(row.content)"></div>
                  <div class="news-tags">
                    <el-tag 
                      v-for="tag in detectTags(row.content)" 
                      :key="tag"
                      :type="getTagType(tag)"
                      size="small"
                      effect="plain">
                      {{ tag }}
                    </el-tag>
                  </div>
                </div>
              </template>
            </el-table-column>
          </el-table>
          
          <div class="pagination-container" v-if="totalNews > itemsPerPage">
            <el-pagination
              v-model:current-page="currentPage"
              :page-size="itemsPerPage"
              :total="totalNews"
              :page-sizes="[5, 10, 15]"
              layout="total, sizes, prev, pager, next, jumper"
              background
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </div>
        
        <div class="market-indicator">
          <div class="indicator-item rise">
            <span class="indicator-label">上证指数</span>
            <span class="indicator-value">3,245.67</span>
            <span class="indicator-change">+1.2%</span>
          </div>
          <div class="indicator-item fall">
            <span class="indicator-label">深证成指</span>
            <span class="indicator-value">11,430.15</span>
            <span class="indicator-change">-0.8%</span>
          </div>
          <div class="indicator-item rise">
            <span class="indicator-label">恒生指数</span>
            <span class="indicator-value">19,876.23</span>
            <span class="indicator-change">+2.1%</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import * as XLSX from 'xlsx'
import { 
  Notification, Clock, Refresh, Document, 
  UserFilled, ArrowDown
} from '@element-plus/icons-vue'

// 导入xlsx文件
import newsXlsx from '/news.xlsx'

const router = useRouter()
const loading = ref(false)

// 新闻数据相关
const newsList = ref([])
const itemsPerPage = ref(5)
const currentPage = ref(1)

// 计算属性
const totalNews = computed(() => newsList.value.length)
const paginatedNewsList = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value
  const end = start + itemsPerPage.value
  return newsList.value.slice(start, end)
})

const lastUpdateTime = ref('')

// 高亮关键词
const highlightKeywords = (content) => {
  if (!content) return ''
  const keywords = ['涨', '跌', '创新高', '创新低', '商务部', '特斯拉', 'A股', '港股', '美股']
  let highlighted = content
  keywords.forEach(keyword => {
    const regex = new RegExp(keyword, 'g')
    highlighted = highlighted.replace(regex, `<span class="highlight">${keyword}</span>`)
  })
  return highlighted
}

// 时间格式转换
const formatExcelTime = (excelTime) => {
  if (excelTime === null || excelTime === undefined || excelTime === '') {
    return '--:--:--'
  }
  
  if (typeof excelTime === 'number') {
    try {
      const totalSeconds = Math.round(excelTime * 86400)
      const hours = Math.floor(totalSeconds / 3600) % 24
      const minutes = Math.floor((totalSeconds % 3600) / 60)
      const seconds = totalSeconds % 60
      
      return [
        hours.toString().padStart(2, '0'),
        minutes.toString().padStart(2, '0'),
        seconds.toString().padStart(2, '0')
      ].join(':')
    } catch {
      return '--:--:--'
    }
  }
  
  if (typeof excelTime === 'string') {
    const timeMatch = excelTime.match(/^(\d{1,2}):(\d{1,2}):(\d{1,2})$/)
    if (timeMatch) {
      return excelTime
    }
  }
  
  return '--:--:--'
}

// 标签检测
const detectTags = (content) => {
  const tagKeywords = {
    '债券': ['债券', '发行利率', '投标倍数'],
    '宏观经济': ['GDP', 'CPI', 'PPI', '经济增长', '用电量', '出口', '钢铁出口', '离岸人民币', '港股IPO', '保险股', '基金产品', '电力需求'],
    '公司动态': ['MUJI', '中国通号', '卡斯柯', '乐高乐园', '特斯拉', '比亚迪', '融创', '财报'],
    '政策法规': ['重庆市委宣传部', '市电影局', '市财政局', '山东省农业农村厅', '上交所', '科创板', '1+6政策', '国办', '信用修复', '中汽协', '工信部', '国务院国资委', '中国足协'],
    '国际市场': ['欧洲央行', '美联储', '道琼斯', '迪拜', '法国', '马克龙', '日本', '离岸人民币']
  }
  
  const foundTags = []
  for (const [tag, keywords] of Object.entries(tagKeywords)) {
    if (keywords.some(keyword => content.includes(keyword))) {
      foundTags.push(tag)
    }
  }
  return foundTags.slice(0, 3)
}

// 标签类型
const getTagType = (tag) => {
  const typeMap = {
    '债券': 'info',
    '宏观经济': 'primary',
    '公司动态': 'success',
    '政策法规': 'warning',
    '国际市场': 'danger'
  }
  return typeMap[tag] || ''
}

// 表格行样式
const tableRowClassName = ({ rowIndex }) => {
  return rowIndex % 2 === 0 ? 'even-row' : 'odd-row'
}

// 加载新闻数据
const loadNewsData = () => {
  try {
    loading.value = true
    
    const xhr = new XMLHttpRequest()
    xhr.open('GET', '/news.xlsx', true)
    xhr.responseType = 'arraybuffer'
    
    xhr.onload = function() {
      if (xhr.status === 200) {
        const arrayBuffer = xhr.response
        const workbook = XLSX.read(arrayBuffer)
        const firstSheet = workbook.Sheets[workbook.SheetNames[0]]
        const jsonData = XLSX.utils.sheet_to_json(firstSheet)
        
        // 处理数据
        const processedData = jsonData.map(item => {
          const timeKey = Object.keys(item).find(k => 
            k.toLowerCase().includes('time') || k.includes('时间')
          )
          const contentKey = Object.keys(item).find(k => 
            k.toLowerCase().includes('content') || k.includes('内容')
          )
          
          return {
            time: timeKey ? item[timeKey] : null,
            content: contentKey ? item[contentKey] : ''
          }
        }).filter(item => {
          return item.time !== null && item.time !== undefined && item.time !== ''
        })
        
        newsList.value = processedData.reverse()
        lastUpdateTime.value = new Date().toLocaleString('zh-CN', {
          year: 'numeric',
          month: '2-digit',
          day: '2-digit',
          hour: '2-digit',
          minute: '2-digit',
          second: '2-digit'
        })
        
        console.log('成功加载数据量:', newsList.value.length)
        loading.value = false
      }
    }
    
    xhr.onerror = function() {
      console.error('加载新闻数据失败')
      lastUpdateTime.value = `更新失败: ${new Date().toLocaleString('zh-CN')}`
      loading.value = false
    }
    
    xhr.send()
  } catch (error) {
    console.error('加载新闻数据失败:', error)
    lastUpdateTime.value = `更新失败: ${new Date().toLocaleString('zh-CN')}`
    loading.value = false
  }
}

// 分页控制
const handleSizeChange = (val) => {
  itemsPerPage.value = val
  currentPage.value = 1
}

const handleCurrentChange = (val) => {
  currentPage.value = val
}

// 用户信息相关
const userInfo = ref({})
const handleCommand = async (command) => {
  if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定退出登录？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      router.push('/login')
    } catch {}
  }
}

// 初始化
onMounted(() => {
  const userInfoStr = localStorage.getItem('userInfo')
  if (userInfoStr) userInfo.value = JSON.parse(userInfoStr)
  // 初始加载一次数据
  loadNewsData()
})
</script>

<style scoped>
/* 基础布局样式 */
.layout {
  display: flex;
  height: 100vh;
}

.main-content {
  flex: 1;
  padding: 20px;
  background: #f5f7fa;
  overflow: auto;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

/* 用户信息样式 */
.user-dropdown {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 8px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-dropdown:hover {
  background-color: #f5f7fa;
}

.breadcrumb {
  font-size: 14px;
}

/* 新闻组件样式 */
.enhanced-news-container {
  font-family: 'Segoe UI', 'PingFang SC', 'Microsoft YaHei', sans-serif;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.news-header-gradient {
  background: linear-gradient(135deg, #1a2a6c 0%, #3a7bd5 100%);
  color: white;
  position: relative;
  padding-bottom: 20px;
}

.header-content {
  padding: 20px 30px 15px;
  position: relative;
  z-index: 2;
}

.header-wave {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 20px;
  background: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1200 120" preserveAspectRatio="none"><path d="M0,0V46.29c47.79,22.2,103.59,32.17,158,28,70.36-5.37,136.33-33.31,206.8-37.5C438.64,32.43,512.34,53.67,583,72.05c69.27,18,138.3,24.88,209.4,13.08,36.15-6,69.85-17.84,104.45-29.34C989.49,25,1113-14.29,1200,52.47V0Z" opacity=".25" fill="%23ffffff"></path><path d="M0,0V15.81C13,36.92,27.64,56.86,47.69,72.05,99.41,111.27,165,111,224.58,91.58c31.15-10.15,60.09-26.07,89.67-39.8,40.92-19,84.73-46,130.83-49.67,36.26-2.85,70.9,9.42,98.6,31.56,31.77,25.39,62.32,62,103.63,73,40.44,10.79,81.35-6.69,119.13-24.28s75.16-39,116.92-43.05c59.73-5.85,113.28,22.88,168.9,38.84,30.2,8.66,59,6.17,87.09-7.5,22.43-10.89,48-26.93,60.65-49.24V0Z" opacity=".5" fill="%23ffffff"></path><path d="M0,0V5.63C149.93,59,314.09,71.32,475.83,42.57c43-7.64,84-23.61,127-26.67,59.73-5.85,113.28,22.88,168.9,38.84,30.2,8.66,59,6.17,87.09-7.5,22.43-10.89,48-26.93,60.65-49.24V0Z" fill="%23ffffff"></path></svg>');
  background-size: cover;
}

.news-title {
  display: flex;
  align-items: center;
  margin: 0;
  font-size: 22px;
  font-weight: 600;
  letter-spacing: 0.5px;
}

.title-icon {
  font-size: 24px;
  margin-right: 12px;
}

.title-text {
  position: relative;
  top: 1px;
}

.beta-badge {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 4px;
  padding: 2px 8px;
  font-size: 12px;
  margin-left: 12px;
  font-weight: 500;
  letter-spacing: 1px;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% { opacity: 0.8; }
  50% { opacity: 1; }
  100% { opacity: 0.8; }
}

.update-info {
  display: flex;
  align-items: center;
  margin-top: 8px;
  font-size: 14px;
  opacity: 0.9;
}

.update-icon {
  margin-right: 8px;
  font-size: 16px;
}

.news-content-wrapper {
  padding: 20px 25px;
}

.premium-table {
  --el-table-border-color: rgba(0, 0, 0, 0.05);
  --el-table-header-bg-color: #f8fafd;
  --el-table-row-hover-bg-color: #f5f9ff;
}

:deep(.premium-table) .el-table__header-wrapper {
  border-radius: 8px 8px 0 0;
  overflow: hidden;
}

:deep(.premium-table) .el-table__body-wrapper {
  border-radius: 0 0 8px 8px;
  overflow: hidden;
}

:deep(.premium-table .el-table__row) {
  transition: all 0.3s ease;
}

:deep(.premium-table .el-table__row.even-row) {
  background-color: #ffffff;
}

:deep(.premium-table .el-table__row.odd-row) {
  background-color: #f8fafd;
}

:deep(.premium-table .el-table__row:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.table-header-cell {
  display: flex;
  align-items: center;
  font-weight: 600;
  color: #2c3e50;
}

.table-header-cell .el-icon {
  margin-right: 8px;
  font-size: 16px;
}

.time-cell-badge {
  display: flex;
  justify-content: center;
}

.time-bubble {
  background: #e8f4ff;
  color: #1a73e8;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 13px;
  font-weight: 500;
  font-family: 'Courier New', monospace;
}

.content-card {
  padding: 12px 0;
  border-bottom: 1px dashed #eaeef5;
}

.content-text {
  line-height: 1.6;
  color: #333;
  font-size: 15px;
}

.news-tags {
  margin-top: 10px;
  display: flex;
  gap: 8px;
}

.pagination-container {
  padding: 20px 0;
  display: flex;
  justify-content: center;
}

:deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
  background-color: #1a73e8;
  color: white;
  border-radius: 6px;
}

:deep(.el-pagination.is-background .btn-prev),
:deep(.el-pagination.is-background .btn-next),
:deep(.el-pagination.is-background .el-pager li) {
  border-radius: 6px;
  margin: 0 4px;
}

.market-indicator {
  display: flex;
  background: #f8fafd;
  padding: 15px 25px;
  border-top: 1px solid #eaeef5;
}

.indicator-item {
  flex: 1;
  display: flex;
  align-items: center;
  padding: 10px 15px;
  border-radius: 6px;
  margin-right: 15px;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.03);
}

.indicator-item:last-child {
  margin-right: 0;
}

.indicator-label {
  font-size: 13px;
  color: #666;
  margin-right: 10px;
}

.indicator-value {
  font-weight: 600;
  margin-right: 10px;
}

.indicator-change {
  font-size: 13px;
  font-weight: 500;
}

.rise .indicator-change {
  color: #f56c6c;
}

.fall .indicator-change {
  color: #67c23a;
}

.highlight {
  background: linear-gradient(120deg, #f6f9ff 0%, #e3eeff 100%);
  padding: 0 4px;
  border-radius: 3px;
  font-weight: 600;
  color: #1a73e8;
  border-left: 2px solid #1a73e8;
}

:deep(.el-loading-mask) {
  background-color: rgba(255, 255, 255, 0.7);
}
</style>