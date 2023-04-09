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

```sql
create table user
(
    id          bigint                             not null
        primary key,
    username    varchar(256)                       null comment '用户名',
    account     varchar(256)                       not null comment '账号',
    password    int                                null comment '密码',
    create_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint  default 0                 not null comment '是否删除',
    user_role   tinyint  default 0                 not null comment '用户角色（0-普通用户，1-管理员）',
    constraint user_pk2
        unique (account)
)
    comment '用户表';
```



### 接口信息表

```java
create table interface_info
(
    id              bigint                             not null
        primary key,
    name            varchar(256)                       not null comment '接口名称',
    description     varchar(256)                       null comment '接口描述',
    url             varchar(256)                       not null comment '接口地址',
    request_header  text                               null comment '请求头',
    response_header text                               null comment '响应头',
    status          tinyint  default 0                 not null comment '接口状态（0-关闭，1-开启）',
    method          varchar(256)                       not null comment '请求类型',
    user_id         bigint                             not null comment '创建人',
    create_time     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete       tinyint  default 0                 not null comment '是否删除（0-未删除，1-已删除）',
    constraint interface_info_pk2
        unique (url)
)
    comment '接口信息表';
```



### 用户调用接口信息表

记录用户调用的每个接口，并保存用户能够调用的最大次数

```sql
create table user_interface_info
(
    id           bigint auto_increment
        primary key,
    user_id      bigint                             not null comment '用户id',
    interface_id bigint                             not null comment '接口id',
    total_num    int      default 0                 not null comment '该用户可调用次数',
    left_num     int      default 0                 not null comment '该用户剩余调用次数',
    status       tinyint  default 0                 not null comment '接口调用状态（0-正常，1-禁止）',
    create_time  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete    tinyint  default 0                 not null comment '是否删除（0-未删除，1-已删除）'
)
    comment '用户调用接口关系表';
```

## 接口设计

### 1、登录

### 2、注册

注册时通过 redis 生成分布式id

### 3、