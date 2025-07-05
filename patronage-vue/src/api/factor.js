import request from '@/utils/request'

// 因子管理API
export const factorApi = {
  // 获取所有因子分类
  getCategories() {
    return request({
      url: '/api/factor/categories',
      method: 'get'
    })
  },

  // 获取分类树结构
  getCategoryTree() {
    return request({
      url: '/api/factor/categories/tree',
      method: 'get'
    })
  },

  // 新增因子分类
  addCategory(data) {
    return request({
      url: '/api/factor/categories',
      method: 'post',
      data
    })
  },

  // 更新因子分类
  updateCategory(data) {
    return request({
      url: '/api/factor/categories',
      method: 'put',
      data
    })
  },

  // 删除因子分类
  deleteCategory(id) {
    return request({
      url: `/api/factor/categories/${id}`,
      method: 'delete'
    })
  },

  // 获取所有因子
  getFactors() {
    return request({
      url: '/api/factor/list',
      method: 'get'
    })
  },

  // 根据分类ID获取因子
  getFactorsByCategory(categoryId) {
    return request({
      url: `/api/factor/list/category/${categoryId}`,
      method: 'get'
    })
  },

  // 条件查询因子
  searchFactors(params) {
    return request({
      url: '/api/factor/search',
      method: 'get',
      params
    })
  },

  // 获取因子详情
  getFactorDetail(id) {
    return request({
      url: `/api/factor/detail/${id}`,
      method: 'get'
    })
  },

  // 新增因子
  addFactor(data) {
    return request({
      url: '/api/factor/add',
      method: 'post',
      data
    })
  },

  // 更新因子
  updateFactor(data) {
    return request({
      url: '/api/factor/update',
      method: 'put',
      data
    })
  },

  // 删除因子
  deleteFactor(id) {
    return request({
      url: `/api/factor/delete/${id}`,
      method: 'delete'
    })
  },

  // 检查因子代码是否存在
  checkFactorCode(factorCode) {
    return request({
      url: `/api/factor/check-code/${factorCode}`,
      method: 'get'
    })
  }
} 