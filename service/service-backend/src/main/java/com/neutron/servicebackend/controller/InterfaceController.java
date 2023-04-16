package com.neutron.servicebackend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.gson.Gson;
import com.neutron.common.exception.BusinessException;
import com.neutron.common.model.dto.InterfaceInfoDTO;
import com.neutron.common.model.dto.UserDTO;
import com.neutron.common.model.entity.InterfaceInfo;
import com.neutron.common.model.entity.User;
import com.neutron.common.model.request.InvokeInterfaceRequest;
import com.neutron.common.response.BaseResponse;
import com.neutron.common.response.ErrorCode;
import com.neutron.common.response.ResultUtils;
import com.neutron.openapiclientsdk.client.OpenApiClient;
import com.neutron.openapiclientsdk.model.Request;
import com.neutron.servicebackend.service.InterfaceInfoService;
import com.neutron.servicebackend.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.neutron.common.constants.UserConstants.USER_LOGIN_STATE;

/**
 * 接口调用模块
 *
 * @author zzs
 * @date 2023/4/13 14:34
 */
@RestController
@RequestMapping("/backend/interface")
public class InterfaceController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    /**
     * 分页查询开发接口
     *
     * @param current  当前页
     * @param pageSize 页面大小
     * @return 页信息
     */
    @GetMapping("/getOpenInterface")
    public BaseResponse<IPage<InterfaceInfoDTO>> getOpenInterface(Long current, Long pageSize) {
        if (current == null || pageSize == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        IPage<InterfaceInfoDTO> openInterface = interfaceInfoService.getOpenInterface(current, pageSize);
        return ResultUtils.success(openInterface, "分页查询成功");
    }

    /**
     * 在线接口调用
     *
     * @param invokeInterfaceRequest 接口调用请求
     * @param request servlet请求
     * @return 接口调用返回值
     */
    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterface(@RequestBody InvokeInterfaceRequest invokeInterfaceRequest, HttpServletRequest request) {
        //请求不能为空
        if (invokeInterfaceRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        //接口id要大于0
        Long interfaceId = invokeInterfaceRequest.getInterfaceId();
        if (interfaceId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断接口是否存在
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(interfaceId);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "接口不存在");
        }
        UserDTO loginUser = (UserDTO) request.getSession().getAttribute(USER_LOGIN_STATE);
        //秘钥id
        String accessKey = loginUser.getAccessKey();
        //获取私钥
        User user = userService.query().eq("access_key", accessKey).one();
        String secretKey = user.getSecretKey();
        OpenApiClient openApiClient = new OpenApiClient(accessKey, secretKey);

        //使用SDK调用远程接口
        Request invokeRequest = new Request();
        invokeRequest.setUserAccount(user.getAccount());
        invokeRequest.setUrl(interfaceInfo.getUrl());
        invokeRequest.setMethod(interfaceInfo.getMethod());
        invokeRequest.setBody(invokeInterfaceRequest.getRequestParams());
        //响应
        String responseJson = openApiClient.sendRequest(invokeRequest);
        if (responseJson == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口调用失败");
        }
        Gson gson = new Gson();
        Object response = gson.fromJson(responseJson, Object.class);

        return ResultUtils.success(response, "在线调用成功");
    }


}
