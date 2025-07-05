import request from '@/utils/request'

export function getFundList() {
  return request({
    url: '/api/fund/list',
    method: 'get'
  })
}

export function getFundById(id) {
  return request({
    url: `/api/fund/${id}`,
    method: 'get'
  })
}

export function getFundByCode(fundCode) {
  return request({
    url: `/api/fund/code/${fundCode}`,
    method: 'get'
  })
}

export function searchFunds(params) {
  return request({
    url: '/api/fund/search',
    method: 'get',
    params
  })
}

export function getFundsByCompany(companyId) {
  return request({
    url: `/api/fund/company/${companyId}`,
    method: 'get'
  })
}

export function getFundsByManager(managerId) {
  return request({
    url: `/api/fund/manager/${managerId}`,
    method: 'get'
  })
}

export function getFundTags(fundId) {
  return request({
    url: `/api/fund/${fundId}/tags`,
    method: 'get'
  })
}

export function getFundsByCompanyId(companyId) {
  return request({
    url: `/api/fund/company/${companyId}`,
    method: 'get'
  })
} 