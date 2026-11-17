CREATE DATABASE IF NOT EXISTS warehouse_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE warehouse_db;

-- ======================================================================
-- 1. 用户表（user）
-- ======================================================================
CREATE TABLE `user` (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '登录账号，唯一',
    password_hash VARCHAR(255) NOT NULL COMMENT 'BCrypt 加密后的口令，禁止存储明文密码',
    role VARCHAR(20) NOT NULL COMMENT 'ADMIN 或 OPERATOR',
    enabled BOOLEAN NOT NULL DEFAULT true COMMENT '是否启用',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ======================================================================
-- 2. 货品表（product）
-- ======================================================================
CREATE TABLE `product` (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    sku VARCHAR(50) NOT NULL UNIQUE COMMENT '商品编码，唯一',
    name VARCHAR(100) NOT NULL COMMENT '商品名称',
    unit VARCHAR(20) NOT NULL COMMENT '计量单位',
    enabled BOOLEAN NOT NULL DEFAULT true COMMENT '是否启用',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ======================================================================
-- 3. 库位表（location）
-- ======================================================================
CREATE TABLE `location` (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    location_code VARCHAR(50) NOT NULL UNIQUE COMMENT '库位编码，唯一，如 A-01',
    name VARCHAR(100) NOT NULL COMMENT '库名称',
    enabled BOOLEAN NOT NULL DEFAULT true COMMENT '是否启用',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ======================================================================
-- 4. 库存表（inventory）
-- ======================================================================
CREATE TABLE `inventory` (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL COMMENT '关联商品',
    location_id BIGINT NOT NULL COMMENT '关联库位',
    quantity INT NOT NULL DEFAULT 0 CHECK (quantity >= 0) COMMENT '账面数量，>=0',
    version BIGINT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号，扣减时递增',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_inventory_product_location (product_id, location_id),
    CONSTRAINT fk_inventory_product FOREIGN KEY (product_id) REFERENCES `product`(id),
    CONSTRAINT fk_inventory_location FOREIGN KEY (location_id) REFERENCES `location`(id)
);

-- ======================================================================
-- 5. 入库单表（inbound）
-- ======================================================================
CREATE TABLE `inbound` (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    inbound_no VARCHAR(50) NOT NULL UNIQUE COMMENT '入库单号，唯一',
    source_name VARCHAR(100) NOT NULL COMMENT '入库来源',
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT' COMMENT 'DRAFT 草稿 / CONFIRMED 已确认',
    created_by BIGINT NOT NULL COMMENT '登记人',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    confirmed_by BIGINT NULL COMMENT '确认人，草稿时可空',
    confirmed_at DATETIME NULL COMMENT '确认时间，草稿时可空',
    CONSTRAINT fk_inbound_created_by FOREIGN KEY (created_by) REFERENCES `user`(id),
    CONSTRAINT fk_inbound_confirmed_by FOREIGN KEY (confirmed_by) REFERENCES `user`(id)
);

-- ======================================================================
-- 6. 入库单明细表（inbound_line）
-- ======================================================================
CREATE TABLE `inbound_line` (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    inbound_id BIGINT NOT NULL COMMENT '关联入库单',
    product_id BIGINT NOT NULL COMMENT '关联商品',
    location_id BIGINT NOT NULL COMMENT '目标库位',
    quantity INT NOT NULL CHECK (quantity > 0) COMMENT '入库数量，>0',
    UNIQUE KEY uk_inbound_line (inbound_id, product_id, location_id),
    CONSTRAINT fk_inbound_line_inbound FOREIGN KEY (inbound_id) REFERENCES `inbound`(id),
    CONSTRAINT fk_inbound_line_product FOREIGN KEY (product_id) REFERENCES `product`(id),
    CONSTRAINT fk_inbound_line_location FOREIGN KEY (location_id) REFERENCES `location`(id)
);

-- ======================================================================
-- 7. 订单表（orders）
-- ======================================================================
CREATE TABLE `orders` (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号，唯一',
    customer_name VARCHAR(100) NOT NULL COMMENT '客户名称',
    product_id BIGINT NOT NULL COMMENT '关联商品',
    quantity INT NOT NULL CHECK (quantity > 0) COMMENT '需求量，>0',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING 待发货 / SHIPPED 已发货',
    created_by BIGINT NOT NULL COMMENT '登记人',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_orders_product FOREIGN KEY (product_id) REFERENCES `product`(id),
    CONSTRAINT fk_orders_created_by FOREIGN KEY (created_by) REFERENCES `user`(id)
);

-- ======================================================================
-- 8. 出库单表（outbound）
-- ======================================================================
CREATE TABLE `outbound` (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    outbound_no VARCHAR(50) NOT NULL UNIQUE COMMENT '出库单号，唯一',
    order_id BIGINT NOT NULL UNIQUE COMMENT '关联订单，一张订单最多一张出库单',
    inventory_id BIGINT NOT NULL COMMENT '关联库存',
    picked_qty INT NULL COMMENT '实拣量，草稿时可空',
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT' COMMENT 'DRAFT 草稿 / PICKED 已拣选 / CONFIRMED 已确认',
    tracking_no VARCHAR(50) NULL UNIQUE COMMENT '模拟运单号，确认前可空',
    created_by BIGINT NOT NULL COMMENT '登记人',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    confirmed_by BIGINT NULL COMMENT '确认人',
    confirmed_at DATETIME NULL COMMENT '确认时间',
    CONSTRAINT fk_outbound_order FOREIGN KEY (order_id) REFERENCES `orders`(id),
    CONSTRAINT fk_outbound_inventory FOREIGN KEY (inventory_id) REFERENCES `inventory`(id),
    CONSTRAINT fk_outbound_created_by FOREIGN KEY (created_by) REFERENCES `user`(id),
    CONSTRAINT fk_outbound_confirmed_by FOREIGN KEY (confirmed_by) REFERENCES `user`(id)
);

-- ======================================================================
-- 9. 盘点单表（stock_count）
-- ======================================================================
CREATE TABLE `stock_count` (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    count_no VARCHAR(50) NOT NULL UNIQUE COMMENT '盘点编号，唯一',
    inventory_id BIGINT NOT NULL COMMENT '关联库存',
    system_qty INT NOT NULL COMMENT '系统账面量',
    actual_qty INT NOT NULL CHECK (actual_qty >= 0) COMMENT '实盘量，>=0',
    stock_version BIGINT NOT NULL COMMENT '盘点时的库存版本号',
    reason VARCHAR(255) NOT NULL COMMENT '盘点原因',
    counted_by BIGINT NOT NULL COMMENT '盘点人',
    counted_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_stock_count_inventory FOREIGN KEY (inventory_id) REFERENCES `inventory`(id),
    CONSTRAINT fk_stock_count_counted_by FOREIGN KEY (counted_by) REFERENCES `user`(id)
);

-- ======================================================================
-- 10. 库存流水表（stock_movement）
-- ======================================================================
CREATE TABLE `stock_movement` (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    inventory_id BIGINT NOT NULL COMMENT '关联库存',
    inbound_line_id BIGINT NULL COMMENT '入库来源，可空',
    outbound_id BIGINT NULL COMMENT '出库来源，可空',
    count_id BIGINT NULL COMMENT '盘点来源，可空',
    movement_type VARCHAR(20) NOT NULL COMMENT 'IN 入库 / OUT 出库 / COUNT 盘点',
    delta_qty INT NOT NULL CHECK (delta_qty != 0) COMMENT '变化量，非零',
    before_qty INT NOT NULL COMMENT '变动前数量',
    after_qty INT NOT NULL COMMENT '变动后数量，= before_qty + delta_qty',
    operator_id BIGINT NOT NULL COMMENT '操作人',
    occurred_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发生时间，精确到秒',
    CONSTRAINT fk_movement_inventory FOREIGN KEY (inventory_id) REFERENCES `inventory`(id),
    CONSTRAINT fk_movement_inbound_line FOREIGN KEY (inbound_line_id) REFERENCES `inbound_line`(id),
    CONSTRAINT fk_movement_outbound FOREIGN KEY (outbound_id) REFERENCES `outbound`(id),
    CONSTRAINT fk_movement_count FOREIGN KEY (count_id) REFERENCES `stock_count`(id),
    CONSTRAINT fk_movement_operator FOREIGN KEY (operator_id) REFERENCES `user`(id),
    CONSTRAINT chk_movement_source_check CHECK (
        (inbound_line_id IS NOT NULL AND outbound_id IS NULL AND count_id IS NULL)
        OR (inbound_line_id IS NULL AND outbound_id IS NOT NULL AND count_id IS NULL)
        OR (inbound_line_id IS NULL AND outbound_id IS NULL AND count_id IS NOT NULL)
    )
);

-- ======================================================================
-- 11. 物流事件表（logistics_event）
-- ======================================================================
CREATE TABLE `logistics_event` (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    external_event_id VARCHAR(64) NOT NULL UNIQUE COMMENT '外部事件号，唯一，幂等去重',
    outbound_id BIGINT NOT NULL COMMENT '关联出库单',
    status VARCHAR(20) NOT NULL COMMENT 'PICKED_UP 已揽收 / IN_TRANSIT 运输中 / DELIVERED 已签收',
    event_time DATETIME NOT NULL COMMENT '事件发生时间，精确到秒',
    received_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '系统接收时间',
    CONSTRAINT fk_logistics_outbound FOREIGN KEY (outbound_id) REFERENCES `outbound`(id)
);

-- ======================================================================
-- 初始数据（仅供开发验证，生产环境请替换为真实 BCrypt 哈希）
-- 以下密码哈希为占位符，请使用 BCrypt 工具生成 admin/admin123、operator/operator123 的真实哈希后替换
-- ======================================================================
INSERT INTO `user` (username, password_hash, role, enabled) VALUES
('admin', '$2a$10$PLACEHOLDER_FOR_BCRYPT_HASH_ADMIN', 'ADMIN', true),
('operator', '$2a$10$PLACEHOLDER_FOR_BCRYPT_HASH_OPERATOR', 'OPERATOR', true);
