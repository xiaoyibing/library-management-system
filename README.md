# 图书管理系统

基于 Spring Boot + Vue 3 开发的前后端分离图书管理系统，
实现管理员与普通用户两类角色的图书管理、借阅管理及数据统计等功能。

## 技术栈

### 后端
- Spring Boot
- Spring Security
- JWT
- MyBatis-Plus
- MySQL

### 前端
- Vue 3
- Vite
- Element Plus
- Pinia
- Axios

## 功能模块

### 管理员
- 用户与读者管理
- 图书管理
- 图书分类管理
- 书架管理
- 借阅记录管理
- 数据统计

### 普通用户
- 图书查询与浏览
- 图书借阅
- 图书归还
- 借阅记录查看
- 个人信息管理

## 项目结构

library-management-system/
├── frontend/        # Vue3 前端
├── src/             # Spring Boot 后端
├── pom.xml
└── README.md

## 核心实现

- 使用 Spring Security + JWT 实现登录认证和接口权限控制
- 使用 MyBatis-Plus 完成数据库访问及分页查询
- 使用 Axios 完成前后端接口通信
- 根据用户角色实现管理员端与普通用户端的页面及权限区分
- 借阅时校验图书库存，完成借阅、归还及库存状态更新
- 对借阅数据进行统计并在管理端进行可视化展示

## 项目截图

<img width="2549" height="1340" alt="image" src="https://github.com/user-attachments/assets/08fe9c97-84f9-4bfb-9a62-28045a38800b" />
<img width="2549" height="1340" alt="image" src="https://github.com/user-attachments/assets/90684322-df73-45ed-b32a-ade1545987dc" />
<img width="2549" height="1340" alt="image" src="https://github.com/user-attachments/assets/e9cfdf49-1dc0-4a97-b4d5-5ceb795a7e89" />
<img width="2549" height="1340" alt="image" src="https://github.com/user-attachments/assets/3df7fb1f-4453-4f7a-aec8-a7868784afb0" />
<img width="2549" height="1340" alt="image" src="https://github.com/user-attachments/assets/0f40ad1b-de66-42ed-9ec0-5d81f5ef3f7d" />
<img width="2549" height="1340" alt="image" src="https://github.com/user-attachments/assets/a58e6524-1ebf-46eb-8924-dbcaba490fce" />



## 本地运行

### 后端

1. 创建 MySQL 数据库
2. 配置数据库连接信息
3. 启动 Spring Boot 项目

### 前端

```bash
cd frontend
npm install
npm run dev
