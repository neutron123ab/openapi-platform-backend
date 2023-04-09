-- 用户表
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

-- 接口信息表
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

-- 用户调用接口关系表
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