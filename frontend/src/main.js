import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

Vue.use(ElementUI)
Vue.config.productionTip = false

/*
 * axios 相关配置已迁移到 src/api/request.js，
 * 业务组件统一从 src/api/index.js 引入接口调用，不再使用 this.$axios。
 */
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
