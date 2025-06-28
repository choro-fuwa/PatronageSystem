import request from '@/utils/request'

// 策略管理相关API
export const strategyApi = {
  // 获取策略列表
  getStrategyList(params) {
    return request({
      url: '/strategy/list',
      method: 'get',
      params
    })
  },

  // 获取策略详情
  getStrategyDetail(id) {
    return request({
      url: `/strategy/detail/${id}`,
      method: 'get'
    })
  },

  // 创建策略
  createStrategy(data) {
    return request({
      url: '/strategy/create',
      method: 'post',
      data
    })
  },

  // 更新策略
  updateStrategy(data) {
    return request({
      url: '/strategy/update',
      method: 'put',
      data
    })
  },

  // 删除策略
  deleteStrategy(id) {
    return request({
      url: `/strategy/delete/${id}`,
      method: 'delete'
    })
  },

  // 启动策略
  startStrategy(id) {
    return request({
      url: `/strategy/start/${id}`,
      method: 'post'
    })
  },

  // 暂停策略
  pauseStrategy(id) {
    return request({
      url: `/strategy/pause/${id}`,
      method: 'post'
    })
  },

  // 停止策略
  stopStrategy(id) {
    return request({
      url: `/strategy/stop/${id}`,
      method: 'post'
    })
  },

  // 获取策略统计信息
  getStrategyStatistics() {
    return request({
      url: '/strategy/statistics',
      method: 'get'
    })
  },

  // 执行回测
  runBacktest(id, params) {
    return request({
      url: `/api/v1/strategies/${id}/backtest`,
      method: 'post',
      data: params
    })
  },

  // 启动模拟运行
  startSimulation(id) {
    return request({
      url: `/api/v1/strategies/${id}/simulate`,
      method: 'post'
    })
  },

  // 获取策略持仓
  getStrategyPositions(id) {
    return request({
      url: `/api/v1/strategies/${id}/positions`,
      method: 'get'
    })
  },

  // 执行再平衡
  executeRebalance(id, type) {
    return request({
      url: `/api/v1/strategies/${id}/rebalance`,
      method: 'post',
      params: { adjustmentType: type }
    })
  },

  // 手动调整持仓
  adjustPositions(id, data) {
    return request({
      url: `/api/v1/strategies/${id}/adjust`,
      method: 'post',
      data
    })
  },

  // 获取调仓历史
  getAdjustmentHistory(id) {
    return request({
      url: `/api/v1/strategies/${id}/adjustments`,
      method: 'get'
    })
  },

  // 获取回测历史
  getBacktestHistory(id) {
    return request({
      url: `/api/v1/strategies/${id}/backtest-history`,
      method: 'get'
    })
  },

  // 获取策略监控数据
  getStrategyMonitorData(id) {
    return request({
      url: `/monitor/strategy/${id}`,
      method: 'get'
    })
  },

  // 获取策略风险指标
  getStrategyRiskMetrics(id) {
    return request({
      url: `/api/v1/strategies/${id}/risk-metrics`,
      method: 'get'
    })
  },

  // 设置预警规则
  setStrategyAlertRules(id, rules) {
    return request({
      url: `/api/v1/strategies/${id}/alert-rules`,
      method: 'post',
      data: rules
    })
  },

  // 复制策略
  copyStrategy(id, name) {
    return request({
      url: `/api/v1/strategies/${id}/copy`,
      method: 'post',
      data: { name }
    })
  },

  // 导出策略配置
  exportStrategyConfig(id) {
    return request({
      url: `/api/v1/strategies/${id}/export`,
      method: 'get'
    })
  },

  // 导入策略配置
  importStrategyConfig(data) {
    return request({
      url: '/api/v1/strategies/import',
      method: 'post',
      data
    })
  },

  // 策略审核
  approveStrategy(id, data) {
    return request({
      url: `/api/v1/strategies/${id}/approve`,
      method: 'post',
      data
    })
  },

  // 策略拒绝
  rejectStrategy(id, data) {
    return request({
      url: `/api/v1/strategies/${id}/reject`,
      method: 'post',
      data
    })
  }
}

// 监控相关API
export const monitorApi = {
  // 获取监控概览
  getMonitorOverview() {
    return request({
      url: '/api/v1/monitor/overview',
      method: 'get'
    })
  },

  // 获取预警列表
  getAlertList(params) {
    return request({
      url: '/monitor/alert/list',
      method: 'get',
      params
    })
  },

  // 获取预警历史列表
  getAlertHistoryList(params) {
    return request({
      url: '/monitor/alert/history',
      method: 'get',
      params
    })
  },

  // 获取绩效分析
  getPerformanceAnalysis(params) {
    return request({
      url: '/api/v1/monitor/analysis',
      method: 'get',
      params
    })
  },

  // 获取收益归因
  getReturnAttribution(strategyId, params) {
    return request({
      url: `/api/v1/monitor/strategies/${strategyId}/attribution`,
      method: 'get',
      params
    })
  },

  // 获取风险归因
  getRiskAttribution(strategyId) {
    return request({
      url: `/api/v1/monitor/strategies/${strategyId}/risk-attribution`,
      method: 'get'
    })
  },

  // 创建预警规则
  createAlert(data) {
    return request({
      url: '/monitor/alert/create',
      method: 'post',
      data
    })
  },

  // 更新预警规则
  updateAlert(data) {
    return request({
      url: '/monitor/alert/update',
      method: 'put',
      data
    })
  },

  // 删除预警规则
  deleteAlert(alertId) {
    return request({
      url: `/monitor/alert/delete/${alertId}`,
      method: 'delete'
    })
  },

  // 启用/禁用预警规则
  toggleAlert(alertId, enabled) {
    return request({
      url: `/monitor/alert/toggle/${alertId}`,
      method: 'post',
      params: { enabled }
    })
  },

  // 处理预警
  processAlert(alertHistoryId, processBy, processRemark) {
    return request({
      url: `/monitor/alert/process/${alertHistoryId}`,
      method: 'post',
      params: { processBy, processRemark }
    })
  },

  // 获取监控看板数据
  getMonitorDashboardData() {
    return request({
      url: '/monitor/dashboard',
      method: 'get'
    })
  },

  // 检查策略预警
  checkStrategyAlerts(strategyId) {
    return request({
      url: `/monitor/check/${strategyId}`,
      method: 'post'
    })
  }
}

// 回测相关API
export const backtestApi = {
  // 获取回测数据
  getBacktestData(id) {
    return request({
      url: `/api/v1/backtest/${id}`,
      method: 'get'
    })
  },

  // 获取回测报告
  getBacktestReport(id) {
    return request({
      url: `/api/v1/backtest/${id}/report`,
      method: 'get'
    })
  },

  // 压力测试
  performStressTest(strategyId, scenarios) {
    return request({
      url: `/api/v1/backtest/stress-test`,
      method: 'post',
      data: {
        strategyId,
        scenarios
      }
    })
  },

  // 执行回测
  executeBacktest(params) {
    return request({
      url: '/backtest/execute',
      method: 'post',
      params
    })
  },

  // 获取回测进度
  getBacktestProgress(backtestId) {
    return request({
      url: `/backtest/progress/${backtestId}`,
      method: 'get'
    })
  },

  // 停止回测
  stopBacktest(backtestId) {
    return request({
      url: `/backtest/stop/${backtestId}`,
      method: 'post'
    })
  },

  // 获取回测结果列表
  getBacktestResultList(params) {
    return request({
      url: '/backtest/list',
      method: 'get',
      params
    })
  },

  // 获取回测结果详情
  getBacktestResultDetail(backtestId) {
    return request({
      url: `/backtest/detail/${backtestId}`,
      method: 'get'
    })
  },

  // 获取回测图表数据
  getBacktestChartData(backtestId) {
    return request({
      url: `/backtest/chart/${backtestId}`,
      method: 'get'
    })
  },

  // 删除回测结果
  deleteBacktestResult(backtestId) {
    return request({
      url: `/backtest/delete/${backtestId}`,
      method: 'delete'
    })
  }
}

// 导出所有API
export default {
  ...strategyApi,
  ...monitorApi,
  ...backtestApi
}