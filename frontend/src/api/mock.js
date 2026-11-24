/**
 * 本地mock数据。request.js在USE_MOCK开启时走这里，前端不用等后端就能开发。
 * 数据参考 seed.sql，结构与 docs/API契约.md 一致。
 */

// 通用时间戳
const T = '2026-11-23T14:55:04'

const DB = {
  user: { id: 1, username: 'admin', role: 'ADMIN', enabled: true, createdAt: T, updatedAt: T },
  products: [
    { id: 1, sku: 'P001', name: '西门子 PLC', unit: '台', enabled: true, createdAt: T, updatedAt: T },
    { id: 2, sku: 'P002', name: '工业相机', unit: '台', enabled: true, createdAt: T, updatedAt: T }
  ],
  locations: [
    { id: 1, locationCode: 'A-01', name: 'A库第一层', enabled: true, createdAt: T, updatedAt: T },
    { id: 2, locationCode: 'B-02', name: 'B库第二层', enabled: true, createdAt: T, updatedAt: T }
  ],
  inventory: [
    { id: 1, productId: 1, locationId: 1, quantity: 90, version: 1, updatedAt: T },
    { id: 2, productId: 2, locationId: 2, quantity: 50, version: 0, updatedAt: T }
  ],
  orders: [
    { id: 1, orderNo: 'ORD20261123001', customerName: '测试客户', productId: 1, quantity: 5, status: 'PENDING', createdBy: 1, createdAt: T },
    { id: 2, orderNo: 'ORD20261123002', customerName: '客户B', productId: 2, quantity: 8, status: 'SHIPPED', createdBy: 1, createdAt: T }
  ],
  outbound: [
    { id: 1, outboundNo: 'OUT20261123001', orderId: 1, inventoryId: 1, pickedQty: 5, status: 'CONFIRMED', trackingNo: 'SF1234567890', createdBy: 1, createdAt: T, confirmedBy: 1, confirmedAt: T },
    { id: 2, outboundNo: 'OUT20261123002', orderId: 2, inventoryId: 2, pickedQty: null, status: 'DRAFT', trackingNo: null, createdBy: 1, createdAt: T, confirmedBy: null, confirmedAt: null }
  ],
  inbound: [
    { id: 1, inboundNo: 'IN20261123001', sourceName: '供应商A', status: 'DRAFT', createdBy: 1, createdAt: T, confirmedBy: null, confirmedAt: null }
  ],
  inboundLines: [
    { id: 1, inboundId: 1, productId: 1, locationId: 1, quantity: 20 }
  ],
  stockCounts: [
    { id: 1, countNo: 'SC20261123001', inventoryId: 1, systemQty: 90, actualQty: 88, stockVersion: 1, reason: '月度盘点', countedBy: 1, countedAt: T }
  ],
  movements: [
    { id: 1, inventoryId: 1, inboundLineId: null, outboundId: 1, countId: null, movementType: 'OUT', deltaQty: -5, beforeQty: 95, afterQty: 90, operatorId: 1, occurredAt: T },
    { id: 2, inventoryId: 1, inboundLineId: 1, outboundId: null, countId: null, movementType: 'IN', deltaQty: 20, beforeQty: 75, afterQty: 95, operatorId: 1, occurredAt: T }
  ],
  logistics: [
    { id: 1, externalEventId: 'EVT001', outboundId: 1, status: 'PICKED_UP', eventTime: T, receivedAt: T },
    { id: 2, externalEventId: 'EVT002', outboundId: 1, status: 'IN_TRANSIT', eventTime: T, receivedAt: T }
  ]
}

/**
 * 按请求url和method返回假数据（即axios adapter的返回值）。
 * 新增接口时要在这里加一条匹配规则。
 */
export function mockAdapter(config) {
  const url = config.url || ''
  const method = (config.method || 'get').toLowerCase()
  let data = null

  if (url === '/login') {
    data = { token: 'mock-token', user: DB.user }
  } else if (url === '/user/info') {
    data = DB.user
  } else if (url.indexOf('/products') === 0) {
    data = DB.products.filter(p => !config.params || !config.params.enabled || p.enabled === (config.params.enabled === 'true' || config.params.enabled === true))
  } else if (url.indexOf('/locations') === 0) {
    data = DB.locations.filter(l => !config.params || !config.params.enabled || l.enabled === (config.params.enabled === 'true' || config.params.enabled === true))
  } else if (url.indexOf('/inventory') === 0) {
    data = DB.inventory
  } else if (url.indexOf('/orders') === 0 && method === 'post') {
    data = Object.assign({ id: 99, status: 'PENDING', createdAt: T }, JSON.parse(config.data || '{}'))
  } else if (url.indexOf('/orders') === 0) {
    data = DB.orders
  } else if (url.indexOf('/outbound/draft') === 0) {
    data = Object.assign({ id: 99, status: 'DRAFT', createdAt: T }, JSON.parse(config.data || '{}'))
  } else if (url.indexOf('/outbound') === 0) {
    data = DB.outbound
  } else if (url.indexOf('/inbound') === 0 && url.indexOf('/line') >= 0) {
    data = { id: 99 }
  } else if (url.indexOf('/inbound') === 0) {
    data = DB.inbound
  } else if (url.indexOf('/stock-count') === 0 && method === 'post') {
    data = { id: 99 }
  } else if (url.indexOf('/stock-count') === 0) {
    data = DB.stockCounts
  } else if (url.indexOf('/movements') === 0) {
    data = DB.movements
  } else if (url.indexOf('/logistics/push') === 0) {
    data = { id: 99 }
  } else if (url.indexOf('/logistics/') === 0) {
    data = DB.logistics
  } else {
    data = null
  }

  return Promise.resolve({
    data: { code: 200, message: 'success', data },
    status: 200,
    statusText: 'OK',
    headers: {},
    config,
    request: {}
  })
}
