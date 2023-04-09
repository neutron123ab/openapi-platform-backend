package com.neutron.servicebackend.service;

import com.neutron.common.model.dto.UserDTO;
import com.neutron.common.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neutron.common.model.request.UserLoginRequest;
import com.neutron.common.model.request.UserRegisterRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zzs
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2023-04-08 16:27:06
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录接口
     *
     * @param userLoginRequest 用户登录请求实体
     * @param request servlet请求
     * @return 用户信息
     */
    UserDTO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    /**
     * 用户注册接口
     *
     * @param userRegisterRequest 用户注册请求
     * @return 是否注册成功
     */
    Boolean userRegister(UserRegisterRequest userRegisterRequest);

}
