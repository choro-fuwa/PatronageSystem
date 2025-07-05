import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Layout from '../views/Layout.vue'
import FundList from '../views/fund/FundList.vue'
import FundDetail from '../views/fund/FundDetail.vue'
import CompanyList from '../views/company/CompanyList.vue'
import ManagerList from '../views/manager/ManagerList.vue'
import FactorList from '../views/factor/FactorList.vue'
import FactorDetail from '../views/factor/FactorDetail.vue'
import FactorCategory from '../views/factor/FactorCategory.vue'
import StrategyList from '../views/strategy/StrategyList.vue'
import ProductList from '../views/portfolio/ProductList.vue'
import AccountList from '../views/trade/AccountList.vue'
import OrderList from '../views/trade/OrderList.vue'

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: Login
    },
    {
        path: '/',
        component: Layout,
        redirect: '/fund',
        children: [
            {
                path: '/fund',
                name: 'FundList',
                component: FundList,
                meta: { title: '基金研究', icon: 'search' }
            },
            {
                path: '/fund/:id',
                name: 'FundDetail',
                component: FundDetail,
                meta: { title: '基金详情' }
            },
            {
                path: '/company',
                name: 'CompanyList',
                component: CompanyList,
                meta: { title: '基金公司', icon: 'office-building' }
            },
            {
                path: '/manager',
                name: 'ManagerList',
                component: ManagerList,
                meta: { title: '基金经理', icon: 'user' }
            },
            {
                path: '/factor',
                name: 'FactorList',
                component: FactorList,
                meta: { title: '因子管理', icon: 'cpu' }
            },
            {
                path: '/factor/:id',
                name: 'FactorDetail',
                component: FactorDetail,
                meta: { title: '因子详情' }
            },
            {
                path: '/factor-category',
                name: 'FactorCategory',
                component: FactorCategory,
                meta: { title: '因子分类', icon: 'list' }
            },
            {
                path: '/strategy',
                name: 'StrategyList',
                component: StrategyList,
                meta: { title: '策略管理', icon: 'trend-charts' }
            },
            {
                path: '/portfolio-product',
                name: 'ProductList',
                component: ProductList,
                meta: { title: '组合产品管理', icon: 'shopping-cart' }
            },
            {
                path: '/trade',
                name: 'Trade',
                meta: { title: '交易管理', icon: 'money' },
                children: [
                    {
                        path: '/trade/accounts',
                        name: 'TradeAccounts',
                        component: AccountList,
                        meta: { title: '交易账户管理', icon: 'wallet' }
                    },
                    {
                        path: '/trade/orders',
                        name: 'TradeOrders',
                        component: OrderList,
                        meta: { title: '交易订单管理', icon: 'document' }
                    }
                ]
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')
    if (to.path !== '/login' && !token) {
        next('/login')
    } else {
        next()
    }
})

export default router