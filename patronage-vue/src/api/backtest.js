import request from '@/utils/request'

export const backtestApi = {
  runBacktest(params) {
    return request({
      url: '/api/backtest/run',
      method: 'get'
    })
  }
}