-- ======================================================================
-- seed.sql - 基于已有数据库插入测试数据（不清除现有数据）
-- 使用 INSERT IGNORE，避免重复键冲突
-- 适用于：已有表结构，仅需补充测试数据
-- ======================================================================

USE warehouse_db;

-- ----------------------------
-- 1. 用户表初始数据（BCrypt 加密: admin/admin123）
-- ----------------------------
INSERT IGNORE INTO `user` (username, password_hash, role, enabled) VALUES ('admin', '$2a$10$mUGRYSNWl7wPNp0H/hoYNeF9A9RZGEhqg2cWEXC6A6fV/ExdfjeXe', 'ADMIN', true);
INSERT IGNORE INTO `user` (username, password_hash, role, enabled) VALUES ('operator', '$2a$10$L8C09L/M/Qvp0odmYts9Mez.WGnhRunPDaK0NPVZ2j4Ur2DgvyoVG', 'OPERATOR', true);

-- ----------------------------
-- 2. 货品表测试数据
-- ----------------------------
INSERT IGNORE INTO product (sku, name, unit, enabled) VALUES ('P001', '西门子 PLC', '台', true);
INSERT IGNORE INTO product (sku, name, unit, enabled) VALUES ('P002', '工业相机', '台', true);

-- ----------------------------
-- 3. 库位表测试数据
-- ----------------------------
INSERT IGNORE INTO location (location_code, name, enabled) VALUES ('A-01', 'A库第一层', true);
INSERT IGNORE INTO location (location_code, name, enabled) VALUES ('B-02', 'B库第二层', true);

-- ----------------------------
-- 4. 库存表测试数据
-- ----------------------------
INSERT IGNORE INTO inventory (product_id, location_id, quantity, version) VALUES (1, 1, 100, 0);
INSERT IGNORE INTO inventory (product_id, location_id, quantity, version) VALUES (2, 2, 50, 0);

-- ----------------------------
-- 5. 订单表测试数据
-- ----------------------------
INSERT IGNORE INTO orders (order_no, customer_name, product_id, quantity, status, created_by) VALUES ('ORD20261117001', '客户A', 1, 10, 'PENDING', 1);

-- ----------------------------
-- 6. 出库单测试数据（DRAFT 状态，可用于验证草稿->拣选->确认流程）
-- ----------------------------
INSERT IGNORE INTO outbound (outbound_no, order_id, inventory_id, picked_qty, status, created_by) VALUES ('OUT20261117001', 1, 1, 10, 'DRAFT', 1);

-- ----------------------------
-- 完成
-- ----------------------------
SELECT 'Seed data inserted successfully' AS status;