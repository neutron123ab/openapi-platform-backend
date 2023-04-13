package com.neutron.servicemanager.interceptor;

import com.neutron.common.exception.BusinessException;
import com.neutron.common.model.dto.UserDTO;
import com.neutron.common.model.enums.UserRoleEnum;
import com.neutron.common.response.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.neutron.common.constants.UserConstants.USER_LOGIN_STATE;

/**
 * 请求权限拦截器
 * @author zzs
 * @date 2023/4/12 15:58
 */
@Component
public class RoleInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserDTO loginUser = (UserDTO) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Integer userRoleValue = loginUser.getUserRole();
        UserRoleEnum userRole = UserRoleEnum.getEnumByValue(userRoleValue);
        if(!userRole.equals(UserRoleEnum.MANAGER)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "该接口只有管理员能够调用");
        }
        return true;
    }
}
