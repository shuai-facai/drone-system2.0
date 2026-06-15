# 无人机信息管理系统

基于 Spring Boot + Vue 3 的前后端分离架构无人机信息管理系统，支持无人机信息的录入、查询、删除和修改，具备 AI 自动生成无人机属性数据功能。

## 项目简介

本系统是一个完整的无人机信息管理平台，采用前后端分离架构设计，提供友好的 Web 界面和强大的后端服务。系统支持多数据库（SQLite/MySQL），具备完善的用户认证机制和灵活的数据管理功能。

## 技术栈

### 后端技术
- **框架**: Spring Boot 2.2.13.RELEASE
- **持久层**: MyBatis 3.5.6
- **数据库连接池**: Druid 1.2.6
- **安全框架**: Apache Shiro 1.7.1
- **数据库**: SQLite（默认）/ MySQL
- **数据验证**: Hibernate Validation 6.0.x

### 前端技术
- **框架**: Vue 3 (Composition API)
- **UI 框架**: Bootstrap 4.6.0
- **HTTP 客户端**: Axios
- **构建工具**: Maven

## 功能特性

### 核心功能
- ✅ 用户登录认证（基于 Shiro）
- ✅ 无人机信息的增删改查
- ✅ 多条件搜索（按型号、编号）
- ✅ AI 自动生成无人机数据
- ✅ 数据库自动初始化
- ✅ 请求日志拦截
- ✅ 跨域支持（CORS）

### 业务功能
- 无人机基础信息管理（编号、型号、制造商）
- 无人机性能参数管理（载重、高度、续航、速度、翼展、自重）
- 无人机状态管理（正常/停用）
- 数据来源标记（手动录入/AI生成）

## 快速开始

### 环境要求
- JDK 1.8 或更高版本
- Maven 3.6 或更高版本
- Node.js（可选，用于前端开发）

### 安装步骤

1. **克隆仓库**
```bash
git clone https://github.com/shuai-facai/drone-system2.0.git
cd drone-system2.0
```

2. **编译打包**
```bash
mvn clean package -DskipTests
```

3. **运行应用**
```bash
java -jar target/drone-management-1.0.0.jar
```

4. **访问系统**
打开浏览器访问：`http://localhost:8080`

### 默认账号
- 管理员：`admin` / `123`
- 普通用户：`user` / `user`

## 项目结构

```
drone-system2.0/
├── src/main/
│   ├── java/com/example/drone/
│   │   ├── config/              # 配置类
│   │   │   ├── DatabaseInitConfig.java    # 数据库初始化
│   │   │   ├── ShiroConfig.java           # Shiro安全配置
│   │   │   └── WebMvcConfig.java          # Web MVC配置
│   │   ├── controller/          # 控制器
│   │   │   ├── DroneController.java       # 无人机管理接口
│   │   │   └── LoginController.java       # 登录认证接口
│   │   ├── entity/              # 实体类
│   │   │   └── Drone.java                # 无人机实体
│   │   ├── interceptor/          # 拦截器
│   │   │   └── RequestLogInterceptor.java # 请求日志拦截器
│   │   ├── mapper/              # 数据访问层接口
│   │   │   └── DroneMapper.java           # 无人机Mapper
│   │   ├── service/             # 服务层
│   │   │   ├── DroneService.java          # 无人机服务接口
│   │   │   └── impl/
│   │   │       └── DroneServiceImpl.java  # 无人机服务实现
│   │   └── DroneManagementApplication.java # 启动类
│   └── resources/
│       ├── mapper/              # MyBatis映射文件
│       │   └── DroneMapper.xml
│       ├── static/              # 前端静态资源
│       │   ├── index.html       # 主页面
│       │   └── js/
│       │       └── app.js       # Vue应用主逻辑
│       ├── application.yml      # 应用配置
│       ├── schema-sqlite.sql    # SQLite建表脚本
│       └── schema-mysql.sql     # MySQL建表脚本
├── pom.xml                      # Maven配置
└── README.md                    # 项目说明
```

## 配置说明

### 数据库配置

系统支持两种数据库，通过 `application.yml` 中的 `spring.profiles.active` 切换：

#### SQLite（默认）
```yaml
spring:
  profiles:
    active: sqlite
```

#### MySQL
```yaml
spring:
  profiles:
    active: mysql
```

需要先创建 MySQL 数据库：
```sql
CREATE DATABASE drone_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 端口配置
默认端口为 8080，可在 `application.yml` 中修改：
```yaml
server:
  port: 8080
```

## API 文档

### 登录认证
- **POST** `/api/login` - 用户登录
- **POST** `/api/logout` - 用户退出

### 无人机管理
- **GET** `/api/drones` - 获取无人机列表（支持搜索）
  - 参数：`keyword`（关键词）、`type`（搜索类型：model/uavCode）
- **POST** `/api/drones` - 添加无人机
- **PUT** `/api/drones/{id}` - 更新无人机
- **DELETE** `/api/drones/{id}` - 删除无人机
- **POST** `/api/drones/ai/generate` - AI生成无人机数据
  - 参数：`count`（生成数量）

## AI 生成功能

系统内置规则引擎，可自动生成符合规范的无人机数据：

- 自动生成无人机编号（格式：UAV-XXXX）
- 随机选择型号和制造商
- 根据型号智能生成性能参数
- 自动设置数据来源标记

## 开发指南

### 添加新功能
1. 在 `entity` 包中创建实体类
2. 在 `mapper` 包中创建 Mapper 接口
3. 在 `service` 包中创建服务接口和实现
4. 在 `controller` 包中创建控制器
5. 在 `resources/mapper` 中创建 MyBatis 映射文件

### 代码规范
- 所有类和方法都包含详细的 Javadoc 注释
- 遵循 RESTful API 设计规范
- 使用统一的响应格式
- 前端使用 Vue 3 Composition API

## 常见问题

### Q: 如何切换数据库？
A: 修改 `application.yml` 中的 `spring.profiles.active` 配置为 `sqlite` 或 `mysql`

### Q: 如何修改端口？
A: 修改 `application.yml` 中的 `server.port` 配置

### Q: 数据库初始化失败？
A: 检查数据库连接配置，确保数据库服务正常运行

### Q: 前端页面无法访问？
A: 确认后端服务已启动，检查端口是否被占用

## 版本历史

### v1.0.0 (2024-06-15)
- 初始版本发布
- 实现基础的无人机信息管理功能
- 支持 SQLite 和 MySQL 数据库
- 集成 AI 自动生成功能
- 完善的用户认证机制

## 贡献指南

欢迎提交 Issue 和 Pull Request！

## 许可证

本项目采用 MIT 许可证。

## 联系方式

- 项目地址：https://github.com/shuai-facai/drone-system2.0
- 问题反馈：[GitHub Issues](https://github.com/shuai-facai/drone-system2.0/issues)

---

**注意**: 本系统仅供学习和研究使用，请勿用于生产环境。