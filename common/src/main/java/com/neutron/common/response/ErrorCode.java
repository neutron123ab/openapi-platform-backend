package com.neutron.common.response;

import lombok.Getter;

/**
 * @author zzs
 * @date 2023/4/9 10:58
 */
@Getter
public enum ErrorCode {

    /**
     * 请求成功
     */
    SUCCESS(0, "请求成功", "请求成功"),

    /**
     * 请求参数错误
     */
    PARAMS_ERROR(40000, "请求参数错误", "请求参数错误"),

    /**
     * 请求数据为空
     */
    NULL_ERROR(40001, "请求数据为空", "请求数据为空"),

    /**
     * 未登录
     */
    NOT_LOGIN(40002, "未登录", "未登录"),

    /**
     * 无权限
     */
    NO_AUTH(40003, "无权限", "无权限"),

    /**
     * 系统内部异常
     */
    SYSTEM_ERROR(50000, "系统内部异常", "系统内部异常");

    private final int code;

    private final String message;

    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }
}
