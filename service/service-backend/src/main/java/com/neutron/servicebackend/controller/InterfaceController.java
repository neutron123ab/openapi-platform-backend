package com.neutron.servicebackend.controller;

import com.neutron.common.exception.BusinessException;
import com.neutron.common.model.dto.UserDTO;
import com.neutron.common.model.enums.UserRoleEnum;
import com.neutron.common.model.request.AddInterfaceRequest;
import com.neutron.common.response.BaseResponse;
import com.neutron.common.response.ErrorCode;
import com.neutron.common.response.ResultUtils;
import com.neutron.servicebackend.service.InterfaceInfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.neutron.common.constants.UserConstants.USER_LOGIN_STATE;

/**
 * 接口管理模块（仅管理员可用）
 * @author zzs
 * @date 2023/4/11 20:13
 */
@RestController
@RequestMapping("/interface")
public class InterfaceController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 上传接口
     *
     * @param addInterfaceRequest 接口信息
     * @param request servlet请求
     * @return 是否上传成功
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> addInterface(@RequestBody AddInterfaceRequest addInterfaceRequest, HttpServletRequest request) {
        if (addInterfaceRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        UserDTO user = (UserDTO) request.getSession().getAttribute(USER_LOGIN_STATE);
        Integer userRoleValue = user.getUserRole();
        UserRoleEnum userRole = UserRoleEnum.getEnumByValue(userRoleValue);
        if(!userRole.equals(UserRoleEnum.MANAGER)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "该接口只有管理员能调用");
        }
        Boolean add = interfaceInfoService.addInterfaceInfo(addInterfaceRequest);
        if(Boolean.FALSE.equals(add)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "添加接口失败");
        }
        return ResultUtils.success(true, "添加接口成功");
    }

}
