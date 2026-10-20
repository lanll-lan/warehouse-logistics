# 仓库物流管理系统

基于 SpringBoot 2.7.18 + Vue 2 + Element UI + MySQL 8 的仓储物流管理系统。

## 环境清单

| 工具 | 版本 |
|------|------|
| Node.js | 16.20.0 |
| JDK | 17 |
| Maven | 3.9+ |
| MySQL | 8.0+ |

## 数据库导入

1. 登录 MySQL，执行初始化脚本：

```bash
mysql -u root -p < backend/sql/init.sql
```

2. 修改 `backend/src/main/resources/application.yml` 中的数据库密码：

```yaml
spring:
  datasource:
    password: your_mysql_password
```

## 后端启动

```bash
cd backend
mvn spring-boot:run
```

后端运行在 http://localhost:8888

## 前端启动

```bash
cd frontend
npm install
npm run serve
```

前端运行在 http://localhost:8080

## 访问地址

- 前端页面：http://localhost:8080
- 默认账号：admin
- 默认密码：admin123

## 接口说明

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/login | POST | 登录，返回 token |
| /api/user/info | GET | 获取用户信息 |
| /api/inventory/list | GET | 库存列表 |
| /api/inventory/save | POST | 新增/编辑库存 |
| /api/inventory/{id} | DELETE | 删除库存 |
| /api/orders/list | GET | 订单列表 |
| /api/orders/save | POST | 新增/编辑订单 |
| /api/orders/{id} | DELETE | 删除订单 |
| /api/outbound/list | GET | 出库列表 |
| /api/outbound/save | POST | 新增/编辑出库 |
| /api/outbound/{id} | DELETE | 删除出库 |

## 项目结构

```
warehouse-logistics/
├── backend/                 # SpringBoot 后端
│   ├── sql/init.sql         # 数据库初始化脚本
│   └── src/main/java/com/warehouse/
│       ├── config/          # 配置类（跨域、拦截器）
│       ├── common/          # 统一返回、全局异常
│       ├── controller/      # 控制器
│       ├── entity/          # 实体类
│       ├── interceptor/     # JWT 拦截器
│       ├── mapper/          # MyBatis Mapper
│       ├── service/         # 业务层
│       └── util/            # 工具类（JWT）
└── frontend/                # Vue2 前端
    └── src/
        ├── router/          # 路由配置
        ├── views/           # 页面组件
        └── main.js          # 入口文件
```
