import request from '@/utils/request'

// 获取所有失败订单
export function getFailedOrders() {
  return request({
    url: '/api/trade/orders/failed',
    method: 'get'
  })
}

// 重新下发失败订单
export function reprocessOrder(id, cancelReason) {
  return request({
    url: `/api/trade/orders/${id}/reprocess`,
    method: 'put',
    data: { cancelReason }
  })
} 