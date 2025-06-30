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
      url: `/strategy/${id}`,
      method: 'get'
    })
  },

  // 创建策略
  createStrategy(data) {
    return request({
      url: '/strategy',
      method: 'post',
      data
    })
  },

  // 更新策略
  updateStrategy(data) {
    return request({
      url: `/strategy/${data.id}`,
      method: 'put',
      data
    })
  },

  // 删除策略
  deleteStrategy(id) {
    return request({
      url: `/strategy/${id}`,
      method: 'delete'
    })
  },

  // 启动策略
  startStrategy(id) {
    return request({
      url: `/strategy/${id}/start`,
      method: 'post'
    })
  },

  // 暂停策略
  pauseStrategy(id) {
    return request({
      url: `/strategy/${id}/pause`,
      method: 'post'
    })
  },

  // 停止策略
  stopStrategy(id) {
    return request({
      url: `/strategy/${id}/stop`,
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
      url: '/backtest/run',
      method: 'post',
      data: { strategyId: id, ...params }
    })
  },

  // 启动模拟运行
  startSimulation(id) {
    return request({
      url: `/strategy/${id}/simulate`,
      method: 'post'
    })
  },

  // 获取策略持仓
  getStrategyPositions(id) {
    return request({
      url: `/strategy/${id}/positions`,
      method: 'get'
    })
  },

  // 执行再平衡
  executeRebalance(id, type) {
    return request({
      url: `/strategy/${id}/rebalance`,
      method: 'post',
      params: { adjustmentType: type }
    })
  },

  // 手动调整持仓
  adjustPositions(id, data) {
    return request({
      url: `/strategy/${id}/adjust`,
      method: 'post',
      data
    })
  },

  // 获取调仓历史
  getAdjustmentHistory(id) {
    return request({
      url: `/strategy/${id}/adjustments`,
      method: 'get'
    })
  },

  // 获取回测历史
  getBacktestHistory(id) {
    return request({
      url: `/backtest/history/${id}`,
      method: 'get'
    })
  },

  // 获取策略监控数据
  getStrategyMonitorData(id) {
    return request({
      url: `/monitor/data/${id}`,
      method: 'get'
    })
  },

  // 获取策略风险指标
  getStrategyRiskMetrics(id) {
    return request({
      url: `/strategy/${id}/risk-metrics`,
      method: 'get'
    })
  },

  // 设置预警规则
  setStrategyAlertRules(id, rules) {
    return request({
      url: `/monitor/alert`,
      method: 'post',
      data: { strategyId: id, ...rules }
    })
  },

  // 复制策略
  copyStrategy(id, name) {
    return request({
      url: `/strategy/${id}/copy`,
      method: 'post',
      data: { name }
    })
  },

  // 导出策略配置
  exportStrategyConfig(id) {
    return request({
      url: `/strategy/${id}/export`,
      method: 'get'
    })
  },

  // 导入策略配置
  importStrategyConfig(data) {
    return request({
      url: '/strategy/import',
      method: 'post',
      data
    })
  },

  // 策略审核
  approveStrategy(id, data) {
    return request({
      url: `/strategy/${id}/approve`,
      method: 'post',
      data
    })
  },

  // 策略拒绝
  rejectStrategy(id, data) {
    return request({
      url: `/strategy/${id}/reject`,
      method: 'post',
      data
    })
  },

  // 调整策略
  adjustStrategy(data) {
    return request({
      url: '/strategy/adjust',
      method: 'post',
      data
    })
  },

  // 获取监控概览
  getMonitorOverview() {
    return request({
      url: '/monitor/overview',
      method: 'get'
    })
  },

  // 获取预警列表
  getAlertList(params) {
    return request({
      url: '/monitor/alerts',
      method: 'get',
      params
    })
  },

  // 获取预警历史列表
  getAlertHistoryList(params) {
    return request({
      url: '/monitor/history',
      method: 'get',
      params
    })
  },

  // 获取性能分析
  getPerformanceAnalysis(params) {
    return request({
      url: '/monitor/performance',
      method: 'get',
      params
    })
  },

  // 获取收益归因
  getReturnAttribution(strategyId, params) {
    return request({
      url: `/monitor/attribution/${strategyId}`,
      method: 'get',
      params
    })
  },

  // 获取风险归因
  getRiskAttribution(strategyId) {
    return request({
      url: `/monitor/risk-attribution/${strategyId}`,
      method: 'get'
    })
  },

  // 创建预警
  createAlert(data) {
    return request({
      url: '/monitor/alert',
      method: 'post',
      data
    })
  },

  // 更新预警
  updateAlert(data) {
    return request({
      url: `/monitor/alert/${data.id}`,
      method: 'put',
      data
    })
  },

  // 删除预警
  deleteAlert(alertId) {
    return request({
      url: `/monitor/alert/${alertId}`,
      method: 'delete'
    })
  },

  // 切换预警状态
  toggleAlert(alertId, enabled) {
    return request({
      url: `/monitor/alert/${alertId}/toggle`,
      method: 'post',
      params: { enabled }
    })
  },

  // 处理预警
  processAlert(alertHistoryId, processBy, processRemark) {
    return request({
      url: `/monitor/history/${alertHistoryId}/process`,
      method: 'post',
      params: { processRemark }
    })
  },

  // 获取监控仪表板数据
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
  },

  // 获取回测数据
  getBacktestData(id) {
    return request({
      url: `/backtest/${id}`,
      method: 'get'
    })
  },

  // 获取回测报告
  getBacktestReport(id) {
    return request({
      url: `/backtest/${id}/report`,
      method: 'get'
    })
  },

  // 执行压力测试
  performStressTest(strategyId, scenarios) {
    return request({
      url: '/backtest/stress-test',
      method: 'post',
      data: { strategyId, scenarios }
    })
  },

  // 执行回测
  executeBacktest(params) {
    return request({
      url: '/backtest/run',
      method: 'post',
      data: params
    })
  },

  // 获取回测进度
  getBacktestProgress(backtestId) {
    return request({
      url: `/backtest/${backtestId}/progress`,
      method: 'get'
    })
  },

  // 停止回测
  stopBacktest(backtestId) {
    return request({
      url: `/backtest/${backtestId}/stop`,
      method: 'post'
    })
  },

  // 获取回测结果列表
  getBacktestResultList(params) {
    return request({
      url: '/backtest/results',
      method: 'get',
      params
    })
  },

  // 获取回测结果详情
  getBacktestResultDetail(backtestId) {
    return request({
      url: `/backtest/${backtestId}`,
      method: 'get'
    })
  },

  // 获取回测图表数据
  getBacktestChartData(backtestId) {
    return request({
      url: `/backtest/${backtestId}/chart`,
      method: 'get'
    })
  },

  // 删除回测结果
  deleteBacktestResult(backtestId) {
    return request({
      url: `/backtest/${backtestId}`,
      method: 'delete'
    })
  }
}

// 导出单个函数
export const getStrategyList = (params) => strategyApi.getStrategyList(params)
export const getStrategyDetail = (id) => strategyApi.getStrategyDetail(id)
export const createStrategy = (data) => strategyApi.createStrategy(data)
export const updateStrategy = (data) => strategyApi.updateStrategy(data)
export const deleteStrategy = (id) => strategyApi.deleteStrategy(id)
export const startStrategy = (id) => strategyApi.startStrategy(id)
export const pauseStrategy = (id) => strategyApi.pauseStrategy(id)
export const stopStrategy = (id) => strategyApi.stopStrategy(id)
export const getStrategyStatistics = () => strategyApi.getStrategyStatistics()
export const runBacktest = (id, params) => strategyApi.runBacktest(id, params)
export const startSimulation = (id) => strategyApi.startSimulation(id)
export const getStrategyPositions = (id) => strategyApi.getStrategyPositions(id)
export const executeRebalance = (id, type) => strategyApi.executeRebalance(id, type)
export const adjustPositions = (id, data) => strategyApi.adjustPositions(id, data)
export const getAdjustmentHistory = (id) => strategyApi.getAdjustmentHistory(id)
export const getBacktestHistory = (id) => strategyApi.getBacktestHistory(id)
export const getStrategyMonitorData = (id) => strategyApi.getStrategyMonitorData(id)
export const getStrategyRiskMetrics = (id) => strategyApi.getStrategyRiskMetrics(id)
export const setStrategyAlertRules = (id, rules) => strategyApi.setStrategyAlertRules(id, rules)
export const copyStrategy = (id, name) => strategyApi.copyStrategy(id, name)
export const exportStrategyConfig = (id) => strategyApi.exportStrategyConfig(id)
export const importStrategyConfig = (data) => strategyApi.importStrategyConfig(data)
export const approveStrategy = (id, data) => strategyApi.approveStrategy(id, data)
export const rejectStrategy = (id, data) => strategyApi.rejectStrategy(id, data)
export const adjustStrategy = (data) => strategyApi.adjustStrategy(data)
export const getBacktestResult = (id) => strategyApi.getBacktestResultDetail(id)
export const getBacktestData = (id) => strategyApi.getBacktestData(id)
export const getBacktestReport = (id) => strategyApi.getBacktestReport(id)
export const performStressTest = (strategyId, scenarios) => strategyApi.performStressTest(strategyId, scenarios)
export const executeBacktest = (params) => strategyApi.executeBacktest(params)
export const getBacktestProgress = (backtestId) => strategyApi.getBacktestProgress(backtestId)
export const stopBacktest = (backtestId) => strategyApi.stopBacktest(backtestId)
export const getBacktestResultList = (params) => strategyApi.getBacktestResultList(params)
export const getBacktestResultDetail = (backtestId) => strategyApi.getBacktestResultDetail(backtestId)
export const getBacktestChartData = (backtestId) => strategyApi.getBacktestChartData(backtestId)
export const deleteBacktestResult = (backtestId) => strategyApi.deleteBacktestResult(backtestId)
export const getMonitorOverview = () => strategyApi.getMonitorOverview()
export const getAlertList = (params) => strategyApi.getAlertList(params)
export const getAlertHistoryList = (params) => strategyApi.getAlertHistoryList(params)
export const getPerformanceAnalysis = (params) => strategyApi.getPerformanceAnalysis(params)
export const getReturnAttribution = (strategyId, params) => strategyApi.getReturnAttribution(strategyId, params)
export const getRiskAttribution = (strategyId) => strategyApi.getRiskAttribution(strategyId)
export const createAlert = (data) => strategyApi.createAlert(data)
export const updateAlert = (data) => strategyApi.updateAlert(data)
export const deleteAlert = (alertId) => strategyApi.deleteAlert(alertId)
export const toggleAlert = (alertId, enabled) => strategyApi.toggleAlert(alertId, enabled)
export const processAlert = (alertHistoryId, processBy, processRemark) => strategyApi.processAlert(alertHistoryId, processBy, processRemark)
export const getMonitorDashboardData = () => strategyApi.getMonitorDashboardData()
export const checkStrategyAlerts = (strategyId) => strategyApi.checkStrategyAlerts(strategyId)

// 监控相关API
export const monitorApi = {
  getStrategyMonitorData: strategyApi.getStrategyMonitorData,
  getStrategyRiskMetrics: strategyApi.getStrategyRiskMetrics,
  setStrategyAlertRules: strategyApi.setStrategyAlertRules,
  getMonitorOverview: strategyApi.getMonitorOverview,
  getAlertList: strategyApi.getAlertList,
  getAlertHistoryList: strategyApi.getAlertHistoryList,
  getPerformanceAnalysis: strategyApi.getPerformanceAnalysis,
  getReturnAttribution: strategyApi.getReturnAttribution,
  getRiskAttribution: strategyApi.getRiskAttribution,
  createAlert: strategyApi.createAlert,
  updateAlert: strategyApi.updateAlert,
  deleteAlert: strategyApi.deleteAlert,
  toggleAlert: strategyApi.toggleAlert,
  processAlert: strategyApi.processAlert,
  getMonitorDashboardData: strategyApi.getMonitorDashboardData,
  checkStrategyAlerts: strategyApi.checkStrategyAlerts
} 