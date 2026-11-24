import request from './request'

/**
 * 所有接口集中在此，路径严格对应 docs/API契约.md。
 * 新接口的SQL/Controller由各任务负责人补，前端先用mock开关独立开发。
 */

// 登录与用户
export const authApi = {
  login: data => request.post('/login', data),
  info: () => request.get('/user/info')
}

// 库存（已完成）
export const inventoryApi = {
  list: params => request.get('/inventory', { params })
}

// 订单（已完成）
export const orderApi = {
  list: params => request.get('/orders', { params }),
  create: data => request.post('/orders', data)
}

// 出库（基础已完成，withdraw由WLS-03补后端）
export const outboundApi = {
  list: params => request.get('/outbound', { params }),
  draft: data => request.post('/outbound/draft', data),
  pick: (id, pickedQty) =>
    request.put('/outbound/' + id + '/pick', { pickedQty: String(pickedQty) }),
  confirm: (id, trackingNo, confirmedBy) =>
    request.post('/outbound/' + id + '/confirm', {
      trackingNo: String(trackingNo),
      confirmedBy: String(confirmedBy)
    }),
  withdraw: id => request.post('/outbound/' + id + '/withdraw')
}

// 商品与库位（WLS-04）
export const productApi = {
  list: params => request.get('/products', { params })
}

export const locationApi = {
  list: params => request.get('/locations', { params })
}

// 入库（WLS-05）
export const inboundApi = {
  list: params => request.get('/inbound', { params }),
  createDraft: data => request.post('/inbound/draft', data),
  addLine: (id, data) => request.post('/inbound/' + id + '/line', data),
  removeLine: (id, lineId) => request.delete('/inbound/' + id + '/line/' + lineId),
  confirm: id => request.post('/inbound/' + id + '/confirm')
}

// 盘点（WLS-06）
export const stockCountApi = {
  list: params => request.get('/stock-count', { params }),
  create: data => request.post('/stock-count', data),
  submit: id => request.post('/stock-count/' + id + '/submit')
}

// 库存流水（WLS-07）
export const movementApi = {
  list: params => request.get('/movements', { params })
}

// 物流轨迹（WLS-07）
export const logisticsApi = {
  list: outboundId => request.get('/logistics/' + outboundId),
  push: data => request.post('/logistics/push', data)
}
