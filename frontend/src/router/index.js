import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue'
import Layout from '../views/Layout.vue'
import Inventory from '../views/Inventory.vue'
import Order from '../views/Order.vue'
import Outbound from '../views/Outbound.vue'
import { getToken } from '../utils/auth'

Vue.use(VueRouter)

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
      { path: '/outbound', name: 'Outbound', component: Outbound, meta: { title: '出库管理' } }
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
