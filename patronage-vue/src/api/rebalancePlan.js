import request from '@/utils/request'

// 获取调仓计划列表
export function getRebalancePlanList() {
  return request({
    url: '/api/trade/rebalance/plan',
    method: 'get'
  })
}

// 获取调仓计划详情
export function getRebalancePlanById(id) {
  return request({
    url: `/api/trade/rebalance/plan/${id}`,
    method: 'get'
  })
}

// 新建调仓计划
export function createRebalancePlanWithInstructions(data) {
  return request({
    url: '/api/trade/rebalance/plan/with-instructions',
    method: 'post',
    data
  })
}

// 下发调仓计划
export function dispatchRebalancePlan(id) {
  return request({
    url: `/api/trade/rebalance/plan/${id}/dispatch`,
    method: 'post'
  })
}

// 删除调仓计划
export function deleteRebalancePlan(id) {
  return request({
    url: `/api/trade/rebalance/plan/${id}`,
    method: 'delete'
  })
} 