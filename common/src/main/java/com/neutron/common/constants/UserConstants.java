package com.neutron.common.constants;

/**
 * @author zzs
 * @date 2023/4/9 15:31
 */
public final class UserConstants {

    private UserConstants() {
    }

    /**
     * 存储用户信息的redis key
     */
    public static final String USER_LOGIN_STATE = "openapi:user:login:state";

    /**
     * 用户账号最短长度
     */
    public static final Integer USER_ACCOUNT_MIN_LEN = 6;

    /**
     * 用户账号最大长度
     */
    public static final Integer USER_ACCOUNT_MAX_LEN = 20;

    /**
     * 用户密码最短长度
     */
    public static final Integer USER_PASSWORD_MIN_LEN = 6;

    /**
     * 用户密码最大长度
     */
    public static final Integer USER_PASSWORD_MAX_LEN = 20;

}
