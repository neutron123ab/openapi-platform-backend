package com.neutron.servicebackend.controller;

import cn.hutool.core.bean.BeanUtil;
import com.neutron.common.exception.BusinessException;
import com.neutron.common.model.dto.UserDTO;
import com.neutron.common.model.request.UserLoginRequest;
import com.neutron.common.model.request.UserRegisterRequest;
import com.neutron.common.model.vo.KeysVO;
import com.neutron.common.response.BaseResponse;
import com.neutron.common.response.ErrorCode;
import com.neutron.common.response.ResultUtils;
import com.neutron.servicebackend.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.neutron.common.constants.UserConstants.USER_LOGIN_STATE;

/**
 * 用户模块
 * @author zzs
 * @date 2023/4/9 16:52
 */
@RestController
@RequestMapping("/backend/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册接口
     *
     * @param userRegisterRequest 注册请求
     * @return 是否注册成功
     */
    @PostMapping("/register")
    public BaseResponse<Boolean> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if(userRegisterRequest == null || BeanUtil.hasNullField(userRegisterRequest)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        Boolean flag = userService.userRegister(userRegisterRequest);
        if(Boolean.FALSE.equals(flag)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败");
        }
        return ResultUtils.success(true, "注册成功");
    }

    /**
     * 用户登录接口
     *
     * @param userLoginRequest 登录请求
     * @param request servlet
     * @return 登录用户的信息
     */
    @PostMapping("/login")
    public BaseResponse<UserDTO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null || BeanUtil.hasNullField(userLoginRequest)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        UserDTO userDTO = userService.userLogin(userLoginRequest, request);
        if(userDTO == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "登录失败");
        }
        return ResultUtils.success(userDTO, "登录成功");
    }

    /**
     * 退出登录接口
     *
     * @param request servlet
     * @return 是否退出登录成功
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return ResultUtils.success(true);
    }

    /**
     * 用户申请密钥
     *
     * @return 密钥对
     */
    @GetMapping("/getSecretKey")
    public BaseResponse<KeysVO> getSecretKey(HttpServletRequest request) {
        UserDTO loginUser = (UserDTO) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Long userId = loginUser.getId();
        KeysVO keysVO = userService.getKeys(userId);
        return ResultUtils.success(keysVO);
    }

}
