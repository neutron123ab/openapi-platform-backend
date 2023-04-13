package com.neutron.servicebackend.interceptor;

import com.neutron.common.exception.BusinessException;
import com.neutron.common.model.dto.UserDTO;
import com.neutron.common.response.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.neutron.common.constants.UserConstants.USER_LOGIN_STATE;

/**
 * 用户登录校验拦截器
 * @author zzs
 * @date 2023/4/12 16:14
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserDTO loginUser = (UserDTO) request.getSession().getAttribute(USER_LOGIN_STATE);
        if(loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return true;
    }
}
