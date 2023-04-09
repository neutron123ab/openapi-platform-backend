package com.neutron.common.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求实体
 * @author zzs
 * @date 2023/4/9 15:03
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -3959684532900946754L;
    /**
     * 用户账号（长度6-20）
     */
    private String userAccount;

    /**
     * 密码（长度6-20）
     */
    private String password;

}
