import { createRouter, createWebHistory } from 'vue-router'
import AllFunds from '../components/AllFunds.vue'
import FundCompanies from '../components/FundCompanies.vue'
import FundManagers from '../components/FundManagers.vue'
import FundProfile from '../components/FundProfile.vue'
import FundCombinations from '../components/FundCombinations.vue'

const routes = [
    {
        path: '/',
        redirect: '/publicFunds' // 默认重定向到公募基金页面
    },
    {
        path: '/publicFunds',
        name: 'PublicFunds',
        component: AllFunds // 使用已导入的AllFunds组件
    },
    {
        path: '/fundCompanies',
        name: 'FundCompanies',
        component: FundCompanies // 使用已导入的FundCompanies组件
    },
    {
        path: '/fundManagers',
        name: 'FundManagers',
        component: FundManagers // 使用已导入的FundManagers组件
    },
    {
        path: '/portfolio',
        name: 'MyPortfolio',
        component: FundCombinations // 使用已导入的FundCombinations组件作为"我的组合"
    },
    // 添加FundProfile组件的路由
    {
        path: '/fund/:id',
        name: 'FundProfile',
        component: FundProfile,
        props: true // 启用props传递路由参数
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router