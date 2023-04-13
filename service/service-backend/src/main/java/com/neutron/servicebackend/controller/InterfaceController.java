package com.neutron.servicebackend.controller;

import com.neutron.common.exception.BusinessException;
import com.neutron.common.model.dto.InterfaceInfoDTO;
import com.neutron.common.model.dto.UserDTO;
import com.neutron.common.response.BaseResponse;
import com.neutron.common.response.ErrorCode;
import com.neutron.common.response.ResultUtils;
import com.neutron.servicebackend.service.InterfaceInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.neutron.common.constants.UserConstants.USER_LOGIN_STATE;

/**
 * 接口调用模块
 *
 * @author zzs
 * @date 2023/4/13 14:34
 */
@RestController
@RequestMapping("/interface")
public class InterfaceController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 获取所有接口（管理员获取全部接口，普通用户获取已上线接口）
     *
     * @param request servlet请求
     * @return 接口信息
     */
    @GetMapping("/getAllInterface")
    public BaseResponse<List<InterfaceInfoDTO>> getAllInterface(HttpServletRequest request) {
        UserDTO loginUser = (UserDTO) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Long userId = loginUser.getId();
        List<InterfaceInfoDTO> allInterfaceInfo = interfaceInfoService.getAllInterfaceInfo(userId);
        if (allInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询接口信息失败");
        }
        return ResultUtils.success(allInterfaceInfo, "请求成功");
    }


}
