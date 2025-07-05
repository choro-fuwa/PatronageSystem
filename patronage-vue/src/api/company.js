import request from '@/utils/request'

export function getCompanyList() {
  return request({
    url: '/api/fund-company/list',
    method: 'get'
  })
}

export function getCompanyById(id) {
  return request({
    url: `/api/fund-company/${id}`,
    method: 'get'
  })
}

export function searchCompanies(params) {
  return request({
    url: '/api/fund-company/search',
    method: 'get',
    params
  })
} 