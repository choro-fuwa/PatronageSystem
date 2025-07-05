<template>
  <div class="news-section">
    <h3>最新新闻</h3>
    <el-button 
      type="text" 
      size="small" 
      @click="triggerFileUpload"
      class="upload-btn"
    >
      上传Excel
    </el-button>
    <input
      type="file"
      ref="fileInput"
      accept=".xlsx,.xls"
      @change="handleFileUpload"
      class="file-input"
    >
    <ul class="news-list">
      <li 
        v-for="(news, index) in newsList" 
        :key="index"
        class="news-item"
      >
        <div class="news-time">{{ news.time }}</div>
        <div class="news-content">{{ news.newsContent }}</div>
      </li>
    </ul>
    <div class="empty-tip" v-if="newsList.length === 0 &&!isUploading">
      请上传包含「时间」和「新闻内容」列的Excel文件
    </div>
    <div class="loading-tip" v-if="isUploading">
      <el-loading-spinner /> 正在解析文件...
    </div>
    <div class="error-tip" v-if="errorMessage">
      <el-icon><CloseCircle /></el-icon> {{ errorMessage }}
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import * as XLSX from 'xlsx'
import { ElMessage } from 'element-plus'

// 新闻数据
const newsList = ref([])
// 文件上传状态
const isUploading = ref(false)
// 文件输入框DOM
const fileInput = ref(null)
// 错误消息
const errorMessage = ref('')

// 触发文件选择框
const triggerFileUpload = () => {
  fileInput.value.click()
}

// 处理文件上传
const handleFileUpload = (e) => {
  const file = e.target.files[0]
  if (!file) return

  // 验证文件格式
  if (!file.name.endsWith('.xlsx') && !file.name.endsWith('.xls')) {
    errorMessage.value = '请上传Excel文件（.xlsx或.xls格式）'
    return
  }

  isUploading.value = true
  errorMessage.value = ''
  const reader = new FileReader()

  // 读取文件内容
  reader.onload = (event) => {
    try {
      // 解析Excel
      const data = new Uint8Array(event.target.result)
      const workbook = XLSX.read(data, { type: 'array' })
      
      // 获取第一个工作表
      const firstSheetName = workbook.SheetNames[0]
      const worksheet = workbook.Sheets[firstSheetName]
      
      // 转换为JSON（header: 1 表示第一行为表头）
      const jsonData = XLSX.utils.sheet_to_json(worksheet, { header: 1 })
      
      // 解析表头和数据
      if (jsonData.length < 2) {
        throw new Error('Excel内容为空或格式错误')
      }

      // 验证表头（必须包含“时间”和“新闻内容”）
      const headers = jsonData[0].map(h => h?.toString().trim() || '')
      const timeIndex = headers.indexOf('时间')
      const contentIndex = headers.indexOf('新闻内容')
      
      if (timeIndex === -1 || contentIndex === -1) {
        throw new Error('Excel必须包含「时间」和「新闻内容」列')
      }

      // 提取数据（从第二行开始）
      const newsData = []
      for (let i = 1; i < jsonData.length; i++) {
        const row = jsonData[i]
        if (!row[contentIndex]) continue // 跳过内容为空的行
        
        // 处理时间格式
        let timeValue = row[timeIndex] || ''
        let formattedTime = ''
        
        // 1. 如果是数字类型（Excel日期序列值）
        if (typeof timeValue === 'number' || !isNaN(Number(timeValue))) {
          const excelDate = Number(timeValue)
          // 排除极小值（可能是无效数据）
          if (excelDate > 0) {
            // Excel日期序列值转JavaScript时间戳（天数*24*60*60*1000）
            const jsDate = new Date((excelDate - 25569) * 86400000)
            formattedTime = jsDate.toLocaleString('zh-CN', {
              year: 'numeric',
              month: '2-digit',
              day: '2-digit',
              hour: '2-digit',
              minute: '2-digit'
            })
          }
        } 
        // 2. 如果是字符串类型（尝试解析常见日期格式）
        else if (typeof timeValue === 'string' && timeValue.trim()) {
          // 尝试解析ISO格式（如"2023-01-01T12:00:00"）
          const isoDate = new Date(timeValue)
          if (!isNaN(isoDate.getTime())) {
            formattedTime = isoDate.toLocaleString('zh-CN', {
              year: 'numeric',
              month: '2-digit',
              day: '2-digit',
              hour: '2-digit',
              minute: '2-digit'
            })
          } 
          // 尝试解析其他常见格式（如"2023/01/01 12:00"）
          else {
            // 简单处理：直接显示原始值
            formattedTime = timeValue
          }
        }
        
        // 添加到新闻列表
        newsData.push({
          time: formattedTime || '时间格式错误',
          newsContent: row[contentIndex].toString().trim()
        })
      }

      if (newsData.length === 0) {
        throw new Error('未找到有效新闻数据')
      }

      // 更新新闻列表
      newsList.value = newsData
      ElMessage({
        message: `解析成功，共${newsData.length}条新闻`,
        type: 'success'
      })
    } catch (error) {
      console.error('解析Excel失败：', error)
      errorMessage.value = '文件解析失败：' + error.message
    } finally {
      isUploading.value = false
      // 重置文件输入框（允许重复上传同一文件）
      fileInput.value.value = ''
    }
  }

  // 读取文件（二进制方式）
  reader.readAsArrayBuffer(file)
}
</script>

<style scoped>
.news-section {
  background-color: #2d3a4b;
  color: #fff;
  padding: 12px 16px;
  border-bottom: 1px solid #3a4a5d;
  margin-bottom: 16px;
}

.news-section h3 {
  margin: 0 0 10px 0;
  padding-bottom: 8px;
  border-bottom: 1px dashed #4a5a6d;
  font-size: 15px;
}

.file-input {
  display: none;
}

.upload-btn {
  color: #409eff;
  padding: 0;
  height: auto;
  margin-bottom: 10px;
}

.news-list {
  margin: 0;
  padding: 0;
}

.news-item {
  list-style: none;
  padding: 10px 0;
  border-bottom: 1px solid #3a4a5d;
}

.news-item:last-child {
  border-bottom: none;
}

.news-time {
  font-size: 12px;
  color: #b0c4de;
  margin-bottom: 4px;
}

.news-content {
  font-size: 14px;
  line-height: 1.5;
}

.empty-tip, .loading-tip, .error-tip {
  padding: 15px 0;
  font-size: 13px;
  text-align: center;
}

.loading-tip {
  color: #909399;
}

.error-tip {
  color: #f56c6c;
}

.error-tip .el-icon {
  margin-right: 5px;
}
</style>