package com.neutron.common.exception;

import com.neutron.common.response.BaseResponse;
import com.neutron.common.response.ErrorCode;
import com.neutron.common.response.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author zzs
 * @date 2023/4/9 14:21
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public <T> BaseResponse<T> businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public <T> BaseResponse<T> businessExceptionHandler(RuntimeException e) {
        log.error("runtimeException: " + e.getMessage(), e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage());
    }

}
