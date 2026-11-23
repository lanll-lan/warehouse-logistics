# API契约

前后端按此文档并行开发。前端开着mock就能先做界面，后端照着契约填方法，最后联调时关掉mock即可。

## 通用约定

- 基础地址：`http://localhost:8888/api`
- 认证：除登录外，所有接口请求头带 `Authorization: Bearer <token>`
- 统一返回结构：`{ "code": 200, "message": "success", "data": ... }`
  - `code=200` 成功，`code=500` 失败（失败信息在 `message`）
  - 注意：后端全局异常也是 HTTP 200 + `code:500`，前端必须检查 `code` 字段
- 参数传递：后端用 `Map<String,String>` 接收的，数值一律以字符串传
- id 全部为 BIGINT，时间在 JSON 里是字符串（如 `2026-11-23T14:55:04`）

---

## 一、登录与用户

### POST /login
请求：
```json
{ "username": "admin", "password": "admin123" }
```
返回 `data`：
```json
{ "token": "eyJhbGciOi...", "user": { "id": 1, "username": "admin", "role": "ADMIN", "enabled": true, "createdAt": "...", "updatedAt": "..." } }
```
注：user里不含 passwordHash。

### GET /user/info
返回 `data`：User对象（同上，不含 passwordHash）。

---

## 二、库存（已完成，勿改）

### GET /inventory
可选参数：`productId`、`locationId`
返回 `data`：Inventory数组
```json
[ { "id": 1, "productId": 1, "locationId": 1, "quantity": 90, "version": 1, "updatedAt": "..." } ]
```

---

## 三、订单（已完成，勿改）

### GET /orders
可选参数：`orderNo`（模糊）、`status`（PENDING/SHIPPED）
返回 `data`：Order数组
```json
[ { "id": 1, "orderNo": "ORD...", "customerName": "客户A", "productId": 1, "quantity": 10, "status": "PENDING", "createdBy": 1, "createdAt": "..." } ]
```

### POST /orders
请求：`{ "orderNo": "...", "customerName": "...", "productId": "1", "quantity": "10", "createdBy": "1" }`
返回 `data`：新建的Order对象。

---

## 四、出库（基础已完成，WLS-02/03在此基础上扩展）

### GET /outbound
可选参数：`outboundNo`（模糊）、`status`（DRAFT/PICKED/CONFIRMED）
返回 `data`：Outbound数组
```json
[ { "id": 1, "outboundNo": "OUT...", "orderId": 1, "inventoryId": 1, "pickedQty": 10, "status": "CONFIRMED", "trackingNo": "SF...", "createdBy": 1, "createdAt": "...", "confirmedBy": 1, "confirmedAt": "..." } ]
```

### POST /outbound/draft
请求：`{ "outboundNo": "...", "orderId": "1", "inventoryId": "1", "createdBy": "1" }`
返回 `data`：新建的Outbound对象（status=DRAFT）。

### PUT /outbound/{id}/pick
请求：`{ "pickedQty": "10" }`
返回 `data`：null（WLS-09会加实拣≠需求的核对，属前端逻辑）。

### POST /outbound/{id}/confirm
请求：`{ "trackingNo": "...", "confirmedBy": "1" }`（WLS-02后运单号改后端生成，前端可不传）
返回 `data`：null。
后端做：校验状态→扣库存→写OUT流水→出库单CONFIRMED→订单SHIPPED→生成运单号。

### POST /outbound/{id}/withdraw  【WLS-03新增】
请求：无请求体
返回 `data`：null。
后端做：仅PICKED→DRAFT，清空pickedQty。

---

## 五、商品与库位  【WLS-04】

### GET /products
可选参数：`enabled`（true/false，不传返回全部）
返回 `data`：Product数组
```json
[ { "id": 1, "sku": "P001", "name": "西门子 PLC", "unit": "台", "enabled": true, "createdAt": "...", "updatedAt": "..." } ]
```

### GET /locations
可选参数：`enabled`
返回 `data`：Location数组
```json
[ { "id": 1, "locationCode": "A-01", "name": "A库第一层", "enabled": true, "createdAt": "...", "updatedAt": "..." } ]
```

---

## 六、入库  【WLS-05】

### GET /inbound
可选参数：`status`（DRAFT/CONFIRMED）
返回 `data`：Inbound数组
```json
[ { "id": 1, "inboundNo": "IN...", "sourceName": "供应商A", "status": "DRAFT", "createdBy": 1, "createdAt": "...", "confirmedBy": null, "confirmedAt": null } ]
```

### POST /inbound/draft
请求：`{ "inboundNo": "...", "sourceName": "...", "createdBy": "1" }`
返回 `data`：新建的Inbound对象（status=DRAFT）。

### POST /inbound/{id}/line
请求：`{ "productId": "1", "locationId": "1", "quantity": "10" }`
返回 `data`：新建的InboundLine对象。
约束：同一入库单同一货品同一库位只能有一条明细。

### DELETE /inbound/{id}/line/{lineId}
返回 `data`：null。

### POST /inbound/{id}/confirm
请求：无请求体
返回 `data`：null。
后端做（同一事务）：按明细累加库存→每条明细写一条IN流水→单据CONFIRMED并记确认人。

---

## 七、盘点  【WLS-06】

### GET /stock-count
可选参数：`inventoryId`
返回 `data`：StockCount数组
```json
[ { "id": 1, "countNo": "SC...", "inventoryId": 1, "systemQty": 90, "actualQty": 88, "stockVersion": 1, "reason": "月度盘点", "countedBy": 1, "countedAt": "..." } ]
```

### POST /stock-count
请求：`{ "countNo": "...", "inventoryId": "1", "actualQty": "88", "reason": "...", "countedBy": "1" }`
返回 `data`：新建的StockCount对象。
后端做：取当前库存数量填入systemQty，当前版本填入stockVersion。

### POST /stock-count/{id}/submit
请求：无请求体
返回 `data`：null。
后端做：比对stock_count.stock_version与inventory.version，不一致拒绝并提示刷新；一致则按actualQty调整库存、写COUNT流水。

---

## 八、库存流水  【WLS-07】

### GET /movements
可选参数：`inventoryId`、`movementType`（IN/OUT/COUNT）
返回 `data`：StockMovement数组
```json
[ { "id": 1, "inventoryId": 1, "inboundLineId": null, "outboundId": 1, "countId": null, "movementType": "OUT", "deltaQty": -10, "beforeQty": 100, "afterQty": 90, "operatorId": 1, "occurredAt": "..." } ]
```

---

## 九、物流轨迹  【WLS-07】

### GET /logistics/{outboundId}
返回 `data`：LogisticsEvent数组（按eventTime倒序）
```json
[ { "id": 1, "externalEventId": "EVT...", "outboundId": 1, "status": "IN_TRANSIT", "eventTime": "...", "receivedAt": "..." } ]
```

### POST /logistics/push
请求：`{ "externalEventId": "EVT...", "outboundId": "1", "status": "PICKED_UP", "eventTime": "..." }`
返回 `data`：新建或已存在的LogisticsEvent对象。
后端做：按externalEventId幂等去重；状态按PICKED_UP→IN_TRANSIT→DELIVERED推进，不改历史。

---

## 十、实体字段速查（建实体类用）

| 实体 | 字段（驼峰） |
|---|---|
| Product | id, sku, name, unit, enabled, createdAt, updatedAt |
| Location | id, locationCode, name, enabled, createdAt, updatedAt |
| Inbound | id, inboundNo, sourceName, status, createdBy, createdAt, confirmedBy, confirmedAt |
| InboundLine | id, inboundId, productId, locationId, quantity |
| StockCount | id, countNo, inventoryId, systemQty, actualQty, stockVersion, reason, countedBy, countedAt |
| StockMovement | id, inventoryId, inboundLineId, outboundId, countId, movementType, deltaQty, beforeQty, afterQty, operatorId, occurredAt |
| LogisticsEvent | id, externalEventId, outboundId, status, eventTime, receivedAt |

日期字段统一用 `java.time.LocalDateTime`，布尔字段用 `Boolean`，数值用 `Long`/`Integer`。
