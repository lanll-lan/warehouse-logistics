import axios from 'axios'
import { Message } from 'element-ui'
import { getToken, clearAuth } from '../utils/auth'

/**
 * 统一 axios 实例：
 * - baseURL 指向后端 /api
 * - 请求拦截器自动附加 JWT（Bearer）
 * - 响应拦截器拆包后端 Result{code, message, data}：
 *   code=200 时直接返回 data，否则弹出错误并 reject
 * - HTTP 401 时清除登录态并跳转登录页
 *
 * 注意：后端 GlobalExceptionHandler 返回的是 HTTP 200 + Result{code:500}，
 * 因此必须检查业务 code，而不能只看 HTTP 状态码。
 */
const request = axios.create({
  baseURL: 'http://localhost:8888/api',
  timeout: 10000
})

request.interceptors.request.use(
  config => {
    const token = getToken()
    if (token) {
      config.headers.Authorization = 'Bearer ' + token
    }
    return config
  },
  error => Promise.reject(error)
)

request.interceptors.response.use(
  response => {
    const res = response.data
    if (res && typeof res.code === 'number') {
      if (res.code === 200) {
        return res.data
      }
      const message = res.message || '操作失败'
      Message.error(message)
      return Promise.reject(new Error(message))
    }
    return res
  },
  error => {
    const status = error.response && error.response.status
    if (status === 401) {
      clearAuth()
      Message.error('登录已失效，请重新登录')
      window.location.href = '/login'
    } else {
      const msg =
        (error.response && error.response.data && error.response.data.message) ||
        error.message ||
        '请求失败'
      Message.error(msg)
    }
    return Promise.reject(error)
  }
)

export default request
