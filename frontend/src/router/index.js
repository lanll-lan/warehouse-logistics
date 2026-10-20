import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue'
import Layout from '../views/Layout.vue'
import Inventory from '../views/Inventory.vue'
import Order from '../views/Order.vue'
import Outbound from '../views/Outbound.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/',
    component: Layout,
    redirect: '/inventory',
    children: [
      { path: '/inventory', name: 'Inventory', component: Inventory },
      { path: '/order', name: 'Order', component: Order },
      { path: '/outbound', name: 'Outbound', component: Outbound }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path === '/login') {
    if (token) {
      next('/')
    } else {
      next()
    }
  } else {
    if (token) {
      next()
    } else {
      next('/login')
    }
  }
})

export default router
