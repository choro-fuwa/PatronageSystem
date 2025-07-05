import request from '@/utils/request'

// 交易账户相关API
export function getTradeAccounts() {
  return request({
    url: '/api/trade/accounts',
    method: 'get'
  })
}

export function getTradeAccountById(id) {
  return request({
    url: `/api/trade/accounts/${id}`,
    method: 'get'
  })
}

export function getTradeAccountByCode(accountCode) {
  return request({
    url: `/api/trade/accounts/code/${accountCode}`,
    method: 'get'
  })
}

export function getTradeAccountsByUserId(userId) {
  return request({
    url: `/api/trade/accounts/user/${userId}`,
    method: 'get'
  })
}

export function getTradeAccountsByStatus(status) {
  return request({
    url: `/api/trade/accounts/status/${status}`,
    method: 'get'
  })
}

export function createTradeAccount(data) {
  return request({
    url: '/api/trade/accounts',
    method: 'post',
    data
  })
}

export function updateTradeAccount(id, data) {
  return request({
    url: `/api/trade/accounts/${id}`,
    method: 'put',
    data
  })
}

export function deleteTradeAccount(id) {
  return request({
    url: `/api/trade/accounts/${id}`,
    method: 'delete'
  })
}

export function updateTradeAccountStatus(id, status) {
  return request({
    url: `/api/trade/accounts/${id}/status`,
    method: 'patch',
    params: { status }
  })
}

export function searchTradeAccounts(params) {
  return request({
    url: '/api/trade/accounts/search',
    method: 'get',
    params
  })
}

// 交易订单相关API
export function getTradeOrders() {
  return request({
    url: '/api/trade/orders',
    method: 'get'
  })
}

export function getTradeOrderById(id) {
  return request({
    url: `/api/trade/orders/${id}`,
    method: 'get'
  })
}

export function getTradeOrderByCode(orderCode) {
  return request({
    url: `/api/trade/orders/code/${orderCode}`,
    method: 'get'
  })
}

export function getTradeOrdersByAccountId(accountId) {
  return request({
    url: `/api/trade/orders/account/${accountId}`,
    method: 'get'
  })
}

export function getTradeOrdersByStrategyId(strategyId) {
  return request({
    url: `/api/trade/orders/strategy/${strategyId}`,
    method: 'get'
  })
}

export function getTradeOrdersByStatus(status) {
  return request({
    url: `/api/trade/orders/status/${status}`,
    method: 'get'
  })
}

export function getTradeOrdersByBizType(orderBizType) {
  return request({
    url: `/api/trade/orders/biztype/${orderBizType}`,
    method: 'get'
  })
}

export function getTradeOrdersByTimeRange(startTime, endTime) {
  return request({
    url: '/api/trade/orders/timerange',
    method: 'get',
    params: { startTime, endTime }
  })
}

export function createTradeOrder(data) {
  return request({
    url: '/api/trade/orders',
    method: 'post',
    data
  })
}

export function updateTradeOrder(id, data) {
  return request({
    url: `/api/trade/orders/${id}`,
    method: 'put',
    data
  })
}

export function deleteTradeOrder(id) {
  return request({
    url: `/api/trade/orders/${id}`,
    method: 'delete'
  })
}

export function updateTradeOrderStatus(id, status) {
  return request({
    url: `/api/trade/orders/${id}/status`,
    method: 'patch',
    params: { status }
  })
}

// 新增：一键执行订单
export function executeTradeOrder(id) {
  return request({
    url: `/api/trade/orders/${id}/execute`,
    method: 'post'
  })
}

// 新增：一键取消订单
export function cancelTradeOrder(id, cancelReason) {
  return request({
    url: `/api/trade/orders/${id}/cancel`,
    method: 'post',
    data: { cancelReason }
  })
}

export function searchTradeOrders(params) {
  return request({
    url: '/api/trade/orders/search',
    method: 'get',
    params
  })
}

// 持仓相关API
export function getPositions() {
  return request({
    url: '/api/trade/positions',
    method: 'get'
  })
}

export function getPositionById(id) {
  return request({
    url: `/api/trade/positions/${id}`,
    method: 'get'
  })
}

export function getPositionsByAccountId(accountId) {
  return request({
    url: `/api/trade/positions/account/${accountId}`,
    method: 'get'
  })
}

export function getPositionByAccountAndFund(accountId, fundId) {
  return request({
    url: `/api/trade/positions/account/${accountId}/fund/${fundId}`,
    method: 'get'
  })
}

export function createPosition(data) {
  return request({
    url: '/api/trade/positions',
    method: 'post',
    data
  })
}

export function updatePosition(id, data) {
  return request({
    url: `/api/trade/positions/${id}`,
    method: 'put',
    data
  })
}

export function deletePosition(id) {
  return request({
    url: `/api/trade/positions/${id}`,
    method: 'delete'
  })
}

export function deletePositionByAccountAndFund(accountId, fundId) {
  return request({
    url: `/api/trade/positions/account/${accountId}/fund/${fundId}`,
    method: 'delete'
  })
}

// 获取交割单列表
export function getExecutionList(params) {
  return request({
    url: '/trade/executions',
    method: 'get',
    params
  })
} 