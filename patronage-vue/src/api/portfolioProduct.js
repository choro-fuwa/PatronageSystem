import request from '@/utils/request'

// 组合产品API接口
export const portfolioProductApi = {
  // 获取所有产品
  getAllProducts() {
    return request({
      url: '/api/portfolio-product/list',
      method: 'get'
    })
  },

  // 根据ID获取产品
  getProductById(id) {
    return request({
      url: `/api/portfolio-product/${id}`,
      method: 'get'
    })
  },

  // 根据产品代码获取产品
  getProductByCode(productCode) {
    return request({
      url: `/api/portfolio-product/code/${productCode}`,
      method: 'get'
    })
  },

  // 根据状态获取产品
  getProductsByStatus(productStatus) {
    return request({
      url: `/api/portfolio-product/status/${productStatus}`,
      method: 'get'
    })
  },

  // 根据产品类型获取产品
  getProductsByType(productType) {
    return request({
      url: `/api/portfolio-product/type/${productType}`,
      method: 'get'
    })
  },

  // 根据风险等级获取产品
  getProductsByRiskLevel(riskLevel) {
    return request({
      url: `/api/portfolio-product/risk/${riskLevel}`,
      method: 'get'
    })
  },

  // 搜索产品
  searchProducts(keyword) {
    return request({
      url: '/api/portfolio-product/search',
      method: 'get',
      params: { keyword }
    })
  },

  // 创建产品
  createProduct(data) {
    return request({
      url: '/api/portfolio-product/create',
      method: 'post',
      data
    })
  },

  // 更新产品
  updateProduct(data) {
    return request({
      url: '/api/portfolio-product/update',
      method: 'put',
      data
    })
  },

  // 删除产品
  deleteProduct(id) {
    return request({
      url: `/api/portfolio-product/${id}`,
      method: 'delete'
    })
  },

  // 更新产品状态
  updateProductStatus(id, productStatus) {
    return request({
      url: `/api/portfolio-product/${id}/status`,
      method: 'put',
      params: { productStatus }
    })
  },

  // 审核产品
  reviewProduct(id, reviewStatus, reviewerId, reviewComment) {
    return request({
      url: `/api/portfolio-product/${id}/review`,
      method: 'put',
      params: { reviewStatus, reviewerId, reviewComment }
    })
  },

  // 获取产品业绩数据
  getProductPerformance(id) {
    return request({
      url: `/api/portfolio-product/${id}/performance`,
      method: 'get'
    })
  },

  // 根据日期范围获取产品业绩数据
  getProductPerformanceByDateRange(id, startDate, endDate) {
    return request({
      url: `/api/portfolio-product/${id}/performance/range`,
      method: 'get',
      params: { startDate, endDate }
    })
  },

  // 获取产品最新业绩
  getLatestPerformance(id) {
    return request({
      url: `/api/portfolio-product/${id}/performance/latest`,
      method: 'get'
    })
  },

  // 添加业绩数据
  addPerformance(id, data) {
    return request({
      url: `/api/portfolio-product/${id}/performance`,
      method: 'post',
      data
    })
  },

  // 批量添加业绩数据
  batchAddPerformance(id, data) {
    return request({
      url: `/api/portfolio-product/${id}/performance/batch`,
      method: 'post',
      data
    })
  },

  // 获取产品文档
  getProductDocuments(id) {
    return request({
      url: `/api/portfolio-product/${id}/documents`,
      method: 'get'
    })
  },

  // 根据类型获取产品文档
  getProductDocumentsByType(id, documentType) {
    return request({
      url: `/api/portfolio-product/${id}/documents/${documentType}`,
      method: 'get'
    })
  },

  // 添加产品文档
  addDocument(id, data) {
    return request({
      url: `/api/portfolio-product/${id}/documents`,
      method: 'post',
      data
    })
  },

  // 删除产品文档
  deleteDocument(documentId) {
    return request({
      url: `/api/portfolio-product/documents/${documentId}`,
      method: 'delete'
    })
  },

  // 更新文档状态
  updateDocumentStatus(documentId, isActive) {
    return request({
      url: `/api/portfolio-product/documents/${documentId}/status`,
      method: 'put',
      params: { isActive }
    })
  }
} 