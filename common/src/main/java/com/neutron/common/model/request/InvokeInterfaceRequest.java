package com.neutron.common.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户在线调用远程接口请求实体类
 *
 * @author zzs
 * @date 2023/4/15 22:46
 */
@Data
public class InvokeInterfaceRequest implements Serializable {
    private static final long serialVersionUID = -6385154786417722492L;

    /**
     * 接口id
     */
    private Long interfaceId;

    /**
     * json形式的请求参数
     */
    private String requestParams;

}
