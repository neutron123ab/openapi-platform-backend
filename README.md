# API 开放平台

## 项目介绍

一个提供 API 接口供开发者调用的平台。用户可以登录注册、开通接口调用权限、浏览接口在线调用、可以使用系统提供的 SDK 进行 API 调用；后台系统会对用户的每次调用进行统计，防止用户随意调用，管理员可以上传、下线接口，前端可以对用户调用情况做可视化。

## 业务流程

![image-20230407224955377](D:\zProject\图片\README\image-20230407224955377.png)

## 技术栈

### 前端

* Vue3
* pure-admin
* element-plus
* echarts
* tailwindcss
* json-editor-vue

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

用户注册的时候要同时完成密钥对的创建

### 3、管理员上传接口

### 4、管理员修改接口信息

### 5、获取所有已上线的接口信息（分页查询）

### 6、管理员获取所有接口信息

### 7、用户申请签名

在用户使用SDK调用接口时，需要再请求中携带签名。为了确保安全，需要保证该签名只能被生成一次，用户下一次调用该接口时会覆盖掉以前的前面，同时修改数据库中的内容。

### 8、用户在线调试接口

用户在请求中传递请求参数、调用的接口，在后台进行一些校验，然后有后台去调用接口。

需要校验用户是否还有调用次数，如果没有，则不对用户的在线请求进行调用。

**校验步骤：**

1. 查数据库，判断该接口是否存在
2. 判断接口当前是否为开启状态
3. 使用SDK调用远程接口（用户是否还有调用次数在网关层校验）

### 9、统计接口总调用次数

使用redisson分布式锁

## SDK 开发

为了方便用户调用线上接口，这里开发了SDK，只需一行代码即可完成对数据的签名和接口的调用。用户调用相应方法后会得到json形式的响应结果，用户需要自己根据接口文档进行json解析。

## 网关

使用 spring cloud gateway 网关过滤所有的用户请求，在进行路由转发前校验请求的签名，核验用户秘钥，若验证不通过则拦截该用户请求。此外，在网关层增加跨域、接口调用计数等操作，统计用户接口调用次数，当超量时限制用户调用或进行计费操作。

### 校验内容

#### 1、请求头校验

* 根据accessKey查询是否存在该用户
* 从数据库中查询秘钥生成签名，与请求头携带的签名比对
* nonce，防重放，在请求时判断redis中是否存在该nonce，若不存在则放行，并将nonce存入redis
* body，请求体，用于生成签名
* timestamp，校验签名是否过期

#### 2、接口信息校验

上面的校验都通过后，在到数据库中查询用户请求的接口是否存在

#### 3、用户查询次数校验

查询用户接口信息调用表，校验用户是否还有调用该接口的次数

### 统一日志处理

设置两个全局过滤器，一个用于对所有经过网关的请求进行校验并在其中打印请求日志，另一个用于打印响应日志同时完成用户接口调用计数的操作

### 分布式session共享

在登录之后，系统会在redis中存储session，但只有登录功能所在的那个模块能够使用这个session，要想让其他模块也能共享这个session，就要在每个模块中都添加一个启动session共享的配置文件。

**服务模块**

```java
@Configuration
@EnableRedisHttpSession
public class SessionConfig {
}
```

**网关模块**

由于在spring cloud gateway中使用的是非阻塞的WebFlux，所以需要使用`@EnableRedisWebSession`注解，并且需要使用Base64编码覆盖WebSession中读取sessionId的写法，否则sessionId传到下游时会不一致。

```java
@Slf4j
@Configuration
@EnableRedisWebSession
public class SessionConfig {
    @Bean
    public WebSessionIdResolver webSessionIdResolver() {
        return new CustomWebSessionIdResolver();
    }

    private static class CustomWebSessionIdResolver extends CookieWebSessionIdResolver {
        // 重写resolve方法 对SESSION进行base64解码
        @Override
        public List<String> resolveSessionIds(ServerWebExchange exchange) {
            MultiValueMap<String, HttpCookie> cookieMap = exchange.getRequest().getCookies();
            // 获取SESSION
            List<HttpCookie> cookies = cookieMap.get(getCookieName());
            if (cookies == null) {
                return Collections.emptyList();
            }
            return cookies.stream().map(HttpCookie::getValue).map(this::base64Decode).collect(Collectors.toList());
        }

        private String base64Decode(String base64Value) {
            try {
                byte[] decodedCookieBytes = Base64.getDecoder().decode(base64Value);
                return new String(decodedCookieBytes);
            } catch (Exception ex) {
                log.debug("Unable to Base64 decode value: " + base64Value);
                return null;
            }
        }
    }
}
```
