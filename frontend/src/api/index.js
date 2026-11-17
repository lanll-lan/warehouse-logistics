import request from './request'

/**
 * 所有接口集中在此，路径严格对应后端 Controller 的实际映射：
 *   LoginController        POST   /api/login
 *   UserController         GET    /api/user/info
 *   InventoryController    GET    /api/inventory
 *   OrderController        POST   /api/orders 、 GET /api/orders
 *   OutboundController     POST   /api/outbound/draft
 *                          PUT    /api/outbound/{id}/pick
 *                          POST   /api/outbound/{id}/confirm
 *                          GET    /api/outbound
 *
 * 后端这些接口用 Map<String,String> 接收参数，因此数值一律以字符串传递。
 */
export const authApi = {
  login: data => request.post('/login', data),
  info: () => request.get('/user/info')
}

export const inventoryApi = {
  list: params => request.get('/inventory', { params })
}

export const orderApi = {
  list: params => request.get('/orders', { params }),
  create: data => request.post('/orders', data)
}

export const outboundApi = {
  list: params => request.get('/outbound', { params }),
  draft: data => request.post('/outbound/draft', data),
  pick: (id, pickedQty) =>
    request.put('/outbound/' + id + '/pick', { pickedQty: String(pickedQty) }),
  confirm: (id, trackingNo, confirmedBy) =>
    request.post('/outbound/' + id + '/confirm', {
      trackingNo: String(trackingNo),
      confirmedBy: String(confirmedBy)
    })
}
