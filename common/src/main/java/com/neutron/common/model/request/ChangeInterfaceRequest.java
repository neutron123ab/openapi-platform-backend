package com.neutron.common.model.request;

import com.neutron.common.model.entity.Params;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zzs
 * @date 2023/4/12 15:38
 */
@Data
public class ChangeInterfaceRequest implements Serializable {
    private static final long serialVersionUID = -4396566130094945137L;

    /**
     * 接口id
     */
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求参数
     */
    private List<Params> paramsList;

    /**
     * 响应参数
     */
    private List<Params> responseParamsList;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 创建人
     */
    private Long userId;
}
