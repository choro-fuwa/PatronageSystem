// 前端调试工具
import { factorApi } from '@/api/factor'

// 测试因子API调用
export const debugFactorApi = async () => {
  console.log('=== 开始测试因子API ===')
  
  try {
    // 测试获取所有因子
    console.log('1. 测试获取所有因子...')
    const factorsResponse = await factorApi.getFactors()
    console.log('获取所有因子结果:', factorsResponse)
    
    // 测试获取分类
    console.log('2. 测试获取分类...')
    const categoriesResponse = await factorApi.getCategories()
    console.log('获取分类结果:', categoriesResponse)
    
    // 测试根据分类获取因子
    if (categoriesResponse.success && categoriesResponse.data.length > 0) {
      const firstCategoryId = categoriesResponse.data[0].id
      console.log('3. 测试根据分类获取因子 (categoryId:', firstCategoryId, ')...')
      const categoryFactorsResponse = await factorApi.getFactorsByCategory(firstCategoryId)
      console.log('根据分类获取因子结果:', categoryFactorsResponse)
    }
    
    // 测试搜索因子
    console.log('4. 测试搜索因子...')
    const searchResponse = await factorApi.searchFactors({})
    console.log('搜索因子结果:', searchResponse)
    
  } catch (error) {
    console.error('API测试失败:', error)
  }
  
  console.log('=== 因子API测试完成 ===')
}

// 测试网络请求
export const testNetwork = async () => {
  console.log('=== 开始测试网络连接 ===')
  
  try {
    const response = await fetch('http://localhost:8080/api/test/db')
    const data = await response.json()
    console.log('网络连接测试结果:', data)
  } catch (error) {
    console.error('网络连接测试失败:', error)
  }
  
  console.log('=== 网络连接测试完成 ===')
}

// 检查localStorage
export const checkLocalStorage = () => {
  console.log('=== 检查localStorage ===')
  console.log('token:', localStorage.getItem('token'))
  console.log('userInfo:', localStorage.getItem('userInfo'))
  console.log('=== localStorage检查完成 ===')
}

// 完整的调试函数
export const runFullDebug = async () => {
  console.log('=== 开始完整调试 ===')
  
  checkLocalStorage()
  await testNetwork()
  await debugFactorApi()
  
  console.log('=== 完整调试完成 ===')
} 