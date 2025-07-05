import request from '@/utils/request'

// 查询账户持仓
export function getAccountPositions(accountId) {
  return request({
    url: `/api/trade/account/${accountId}/positions`,
    method: 'get'
  })
}

// 创建账户调仓计划
export function createAccountRebalance(data) {
  return request({
    url: '/api/trade/account/rebalance',
    method: 'post',
    data
  })
} 