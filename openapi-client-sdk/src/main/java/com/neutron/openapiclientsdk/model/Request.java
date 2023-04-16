package com.neutron.openapiclientsdk.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户调用接口请求
 *
 * @author zzs
 * @date 2023/4/10 18:36
 */
@Data
public class Request implements Serializable {
    private static final long serialVersionUID = -2271992994101626473L;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求体
     */
    private Object body;
}
