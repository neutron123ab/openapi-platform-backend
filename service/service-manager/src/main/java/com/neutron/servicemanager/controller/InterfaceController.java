package com.neutron.servicemanager.controller;

import com.neutron.common.exception.BusinessException;
import com.neutron.common.model.dto.UserDTO;
import com.neutron.common.model.enums.UserRoleEnum;
import com.neutron.common.model.request.AddInterfaceRequest;
import com.neutron.common.model.request.ChangeInterfaceRequest;
import com.neutron.common.response.BaseResponse;
import com.neutron.common.response.ErrorCode;
import com.neutron.common.response.ResultUtils;
import com.neutron.servicemanager.service.InterfaceInfoService;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 更新接口信息
     *
     * @param changeInterfaceRequest 更新接口请求
     * @return 是否更新成功
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterface(@RequestBody ChangeInterfaceRequest changeInterfaceRequest) {
        if (changeInterfaceRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        Boolean isUpdate = interfaceInfoService.changeInterfaceStatus(changeInterfaceRequest);
        if(Boolean.FALSE.equals(isUpdate)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新接口失败");
        }
        return ResultUtils.success(true, "更新接口信息成功");
    }

    @GetMapping("/getSession")
    public BaseResponse<String> getSession(HttpServletRequest request) {
        String s = request.getSession().getAttribute(USER_LOGIN_STATE).toString();
        return ResultUtils.success(s);
    }

}
