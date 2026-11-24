import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue'
import Layout from '../views/Layout.vue'
import Inventory from '../views/Inventory.vue'
import Order from '../views/Order.vue'
import Outbound from '../views/Outbound.vue'
import Placeholder from '../views/Placeholder.vue'
import { getToken } from '../utils/auth'

Vue.use(VueRouter)

/**
 * 路由骨架已配好。WLS-12/13 实施时，把对应 children 的 component 从
 * Placeholder 换成真正的页面即可（如 Logistics.vue、Inbound.vue）。
 */
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/inventory',
    children: [
      { path: '/inventory', name: 'Inventory', component: Inventory, meta: { title: '库存管理' } },
      { path: '/order', name: 'Order', component: Order, meta: { title: '订单管理' } },
      { path: '/outbound', name: 'Outbound', component: Outbound, meta: { title: '出库管理' } },
      // 以下为骨架占位，各任务实现后替换 component
      { path: '/inbound', name: 'Inbound', component: Placeholder, meta: { title: '入库管理' } },        // WLS-13
      { path: '/stock-count', name: 'StockCount', component: Placeholder, meta: { title: '盘点管理' } }, // WLS-13
      { path: '/movement', name: 'Movement', component: Placeholder, meta: { title: '库存流水' } },      // WLS-13
      { path: '/basic-data', name: 'BasicData', component: Placeholder, meta: { title: '基础资料' } },   // WLS-13
      { path: '/logistics', name: 'Logistics', component: Placeholder, meta: { title: '物流轨迹' } }     // WLS-12
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

router.beforeEach((to, from, next) => {
  const token = getToken()
  if (to.path === '/login') {
    token ? next('/') : next()
  } else {
    token ? next() : next('/login')
  }
})

export default router
