package com.neutron.common.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求
 * @author zzs
 * @date 2023/4/9 16:02
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = -3194122942252055255L;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String password;

    /**
     * 确认密码
     */
    private String checkedPassword;
}
