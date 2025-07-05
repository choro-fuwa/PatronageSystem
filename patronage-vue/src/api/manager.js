import request from '@/utils/request'

export function getManagerList() {
  return request({
    url: '/api/fund-manager/list',
    method: 'get'
  })
}

export function getManagerById(id) {
  return request({
    url: `/api/fund-manager/${id}`,
    method: 'get'
  })
}

export function searchManagers(params) {
  return request({
    url: '/api/fund-manager/search',
    method: 'get',
    params
  })
}

export function getManagersByCompany(companyId) {
  return request({
    url: `/api/fund-manager/company/${companyId}`,
    method: 'get'
  })
} 