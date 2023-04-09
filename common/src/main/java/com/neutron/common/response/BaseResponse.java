package com.neutron.common.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zzs
 * @date 2023/4/9 10:52
 */
@Data
public class BaseResponse<T> implements Serializable {

    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 状态码信息
     */
    private String message;

    /**
     * 响应描述
     */
    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.description = errorCode.getDescription();
    }

    public BaseResponse(ErrorCode errorCode, String description) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.description = description;
    }

    public BaseResponse(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

}
