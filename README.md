# API 开放平台

## 项目介绍

一个提供 API 接口供开发者调用的平台。用户可以登录注册、开通接口调用权限、浏览接口在线调用、可以使用系统提供的 SDK 进行 API 调用；后台系统会对用户的每次调用进行统计，防止用户随意调用，管理员可以上传、下线接口，前端可以对用户调用情况做可视化。

## 业务流程

![image-20230407224955377](D:\zProject\图片\README\image-20230407224955377.png)

## 技术栈

### 前端

* Vue3
* vben-admin
* ant-design-vue
* echarts

### 后端

* spring boot
* Dubbo
* Nacos 注册中心
* Redis 缓存、分布式id
* Redisson 分布式锁
* Spring Cloud Gateway 网关
* mysql 数据库
* mybatis plus
* 开发spring-boot-starter，为调用者提供 SDK

## 数据库表设计

### 用户信息表



### 接口信息表