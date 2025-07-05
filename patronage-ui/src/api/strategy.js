import request from '@/utils/request'

// 策略管理API
export const strategyApi = {
    // 获取所有策略
    getStrategies() {
        return request({
            url: '/api/strategy/list',
            method: 'get'
        })
    },

    // 获取策略详情
    getStrategyDetail(id) {
        return request({
            url: `/api/strategy/detail/${id}`,
            method: 'get'
        })
    },

    // 条件查询策略
    searchStrategies(params) {
        return request({
            url: '/api/strategy/search',
            method: 'get',
            params
        })
    },

    // 关键词搜索策略
    searchStrategiesByKeyword(keyword) {
        return request({
            url: '/api/strategy/search/keyword',
            method: 'get',
            params: { keyword }
        })
    },

    // 新增策略
    addStrategy(data) {
        return request({
            url: '/api/strategy/add',
            method: 'post',
            data
        })
    },

    // 更新策略
    updateStrategy(data) {
        return request({
            url: '/api/strategy/update',
            method: 'put',
            data
        })
    },

    // 删除策略
    deleteStrategy(id) {
        return request({
            url: `/api/strategy/delete/${id}`,
            method: 'delete'
        })
    },

    // 检查策略代码是否存在
    checkStrategyCode(strategyCode) {
        return request({
            url: `/api/strategy/check-code/${strategyCode}`,
            method: 'get'
        })
    },

    // 获取策略统计信息
    getStrategyStats() {
        return request({
            url: '/api/strategy/stats',
            method: 'get'
        })
    },

    // 按策略类型统计
    getStrategyCountByType() {
        return request({
            url: '/api/strategy/stats/by-type',
            method: 'get'
        })
    },

    // 按风险等级统计
    getStrategyCountByRiskLevel() {
        return request({
            url: '/api/strategy/stats/by-risk-level',
            method: 'get'
        })
    },

    // 获取策略回测信息
    getStrategyBacktests(strategyId) {
        return request({
            url: `/api/strategy/backtest/${strategyId}`,
            method: 'get'
        })
    },

    // 获取策略监控信息
    getStrategyMonitors(strategyId) {
        return request({
            url: `/api/strategy/monitor/${strategyId}`,
            method: 'get'
        })
    }
}