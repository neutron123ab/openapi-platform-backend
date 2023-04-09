package com.neutron.common.response;

/**
 * @author zzs
 * @date 2023/4/9 13:50
 */
public class ResultUtils {

    private ResultUtils() {
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "请求成功");
    }

    public static <T> BaseResponse<T> success(T data, String description) {
        return new BaseResponse<>(0, data, "请求成功", description);
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode, String description) {
        return new BaseResponse<>(errorCode, description);
    }

    public static <T> BaseResponse<T> error(int code, String message, String description) {
        return new BaseResponse<>(code, message, description);
    }
}
