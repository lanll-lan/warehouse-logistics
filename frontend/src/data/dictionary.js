/**
 * 本地数据字典。
 *
 * 背景：后端当前只暴露了 user / inventory / orders / outbound 四类接口，
 * product（商品）与 location（库位）尚未提供查询接口。因此这里用本地字典
 * 维护 ID -> 名称的映射（数据与后端 sql/seed.sql 保持一致），供表格列与
 * 下拉框使用。待后端补充 /api/product、/api/location 后，可将本文件替换为
 * 接口调用，组件侧无需改动。
 */

export const PRODUCT_OPTIONS = [
  { value: 1, label: 'P001 西门子 PLC', unit: '台' },
  { value: 2, label: 'P002 工业相机', unit: '台' }
]

export const LOCATION_OPTIONS = [
  { value: 1, label: 'A-01 A库第一层' },
  { value: 2, label: 'B-02 B库第二层' }
]

export function productLabel(id) {
  const item = PRODUCT_OPTIONS.find(p => p.value === id)
  return item ? item.label : ('#' + id)
}

export function locationLabel(id) {
  const item = LOCATION_OPTIONS.find(l => l.value === id)
  return item ? item.label : ('#' + id)
}

export const ORDER_STATUS = {
  PENDING: { label: '待发货', type: 'warning' },
  SHIPPED: { label: '已发货', type: 'success' }
}

export const OUTBOUND_STATUS = {
  DRAFT: { label: '草稿', type: 'info' },
  PICKED: { label: '已拣选', type: 'warning' },
  CONFIRMED: { label: '已确认', type: 'success' }
}

export const ORDER_STATUS_OPTIONS = [
  { value: 'PENDING', label: '待发货' },
  { value: 'SHIPPED', label: '已发货' }
]

export const OUTBOUND_STATUS_OPTIONS = [
  { value: 'DRAFT', label: '草稿' },
  { value: 'PICKED', label: '已拣选' },
  { value: 'CONFIRMED', label: '已确认' }
]

export function orderStatus(value) {
  return ORDER_STATUS[value] || { label: value || '-', type: 'info' }
}

export function outboundStatus(value) {
  return OUTBOUND_STATUS[value] || { label: value || '-', type: 'info' }
}
