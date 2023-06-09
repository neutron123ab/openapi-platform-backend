package com.neutron.common.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.apache.ibatis.type.StringTypeHandler;

import java.io.Serializable;
import java.util.Date;

/**
 * 接口信息表
 * @author zzs
 * @TableName interface_info
 */
@TableName(value ="interface_info")
@Data
public class InterfaceInfo implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 接口名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 接口描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 接口地址
     */
    @TableField(value = "url")
    private String url;

    /**
     * 请求头
     */
    @TableField(value = "request_header")
    private String requestHeader;

    /**
     * 响应头
     */
    @TableField(value = "response_header")
    private String responseHeader;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 请求类型
     */
    @TableField(value = "method")
    private String method;

    /**
     * 创建人
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 接口总调用次数
     */
    @TableField(value = "total")
    private Long total;

    /**
     * 是否删除（0-未删除，1-已删除）
     */
    @TableField(value = "is_delete")
    @TableLogic
    private Integer isDelete;

    /**
     * 请求参数
     */
    @TableField(value = "request_params", typeHandler = StringTypeHandler.class)
    private String requestParams;

    /**
     * 返回参数
     */
    @TableField(value = "response_params", typeHandler = StringTypeHandler.class)
    private String responseParams;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}