import Vue from 'vue'
import Vuex from 'vuex'
import { getUser } from '../utils/auth'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    user: getUser()
  },
  mutations: {
    SET_USER(state, user) {
      state.user = user
    }
  },
  actions: {},
  modules: {}
})
