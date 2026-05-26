# 外卖订餐系统后端

本项目是一个完整的外卖订餐系统后端服务，采用Spring Boot框架开发，支持菜品管理、套餐管理、订单处理、用户管理等功能。系统同时提供管理端和用户端接口。

## 技术栈

- **后端框架**: Spring Boot 2.x
- **持久层**: MyBatis + MySQL
- **缓存**: Redis
- **认证**: JWT
- **实时通信**: WebSocket
- **文件存储**: 阿里云OSS
- **支付**: 微信支付
- **构建工具**: Maven

## 项目结构

```text
sky-take-out/
├── sky-common/                # 公共模块
│   └── src/main/java/com/sky/
│       ├── constant/          # 常量定义
│       ├── context/           # 上下文
│       ├── enumeration/       # 枚举
│       ├── exception/         # 异常类
│       ├── json/              # JSON处理
│       ├── properties/        # 配置属性
│       ├── result/            # 统一响应结果
│       └── utils/             # 工具类
├── sky-pojo/                  # 数据对象模块
│   └── src/main/java/com/sky/
│       ├── dto/               # 数据传输对象
│       ├── entity/            # 实体类
│       └── vo/                # 视图对象
└── sky-server/                # 主应用模块
    └── src/main/java/com/sky/
        ├── aspect/            # AOP切面
        ├── config/            # 配置类
        ├── controller/        # 控制器
        │   ├── admin/         # 管理端接口
        │   ├── user/          # 用户端接口
        │   └── notify/        # 回调通知
        ├── interceptor/       # 拦截器
        ├── mapper/            # 数据访问层
        ├── service/           # 业务逻辑层
        ├── task/              # 定时任务
        └── websocket/         # WebSocket

## 主要功能
### 管理端接口
| 模块 | 功能 |
| ---- | ---- |
| 员工管理 | 登录登出、新增、启用禁用、编辑查询 |
| 分类管理 | 新增、编辑、删除、启禁用、分类查询 |
| 菜品管理 | 新增、编辑、删除、启禁用、套餐关联 |
| 套餐管理 | 新增、编辑、删除、启禁用 |
| 订单管理 | 搜索、接单、拒单、派送、完成、取消 |
| 数据统计 | 营业额、订单量、用户分析、销量排行 |

### 用户端接口
| 模块 | 功能 |
| ---- | ---- |
| 用户 | 微信登录 |
| 分类 | 分类列表查询 |
| 菜品 | 菜品列表查询 |
| 套餐 | 套餐列表、套餐菜品查询 |
| 购物车 | 添加、查询、清空 |
| 订单 | 下单、支付、历史订单、催单、再来一单 |
| 地址 | 地址管理、设为默认 |

## 快速开始
### 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+
- Redis 3.2+

### 配置说明
在 `application.yml` 中配置以下关键参数：
```yaml
server:
  port: 8080

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/sky_take_out
    username: root
    password: your_password
  redis:
    host: localhost
    port: 6379

sky:
  jwt:
    admin-secret-key: your_admin_key
    admin-ttl: 7200000
    user-secret-key: your_user_key
    user-ttl: 604800000
  alioss:
    endpoint: oss-cn-hangzhou.aliyuncs.com
    access-key-id: your_access_key
    access-key-secret: your_secret
    bucket-name: your_bucket
  wechat:
    appid: your_appid
    appsecret: your_secret

运行项目
# 编译项目
mvn clean package

# 运行
java -jar sky-server/target/sky-server.jar

也可直接在 IDE 中运行 SkyApplication 启动类。
接口文档
启动项目后访问 Swagger 文档：
管理端: http://localhost:8080/doc.html
用户端: http://localhost:8080/doc.html
核心接口示例
1. 员工登录
POST /admin/employee/login
Content-Type: application/json

{
    "username": "admin",
    "password": "123456"
}

2. 用户微信登录
POST /user/login
Content-Type: application/json

{
    "code": "微信授权码"
}

3. 用户下单
POST /user/order/submit
Authorization: Bearer {token}
Content-Type: application/json

{
    "addressBookId": 1,
    "remark": "少放盐",
    "payMethod": 1
}

4. 订单支付
PUT /user/order/payment
Authorization: Bearer {token}
Content-Type: application/json

{
    "orderNumber": "ORDER123456",
    "payMethod": 1
}

定时任务
订单超时处理：每分钟检查并取消超时的未支付订单
自动签收：每天凌晨 1 点自动完成已派送的订单
WebSocket 推送：每 5 秒向客户端推送新订单消息
特性说明
自动填充：使用 AOP 自动填充创建时间、更新时间、创建人、修改人
分布式缓存: Redis 缓存菜品和套餐数据，支持自动更新
微信支付：集成微信 JSAPI 支付和退款功能
百度地图 API: 距离检查，判断配送地址是否在范围内
WebSocket 实时推送：新订单实时推送给管理端

License
本项目仅供学习交流使用。
