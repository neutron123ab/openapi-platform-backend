package com.neutron.servicebackend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.neutron.common.exception.BusinessException;
import com.neutron.common.model.dto.InterfaceInfoDTO;
import com.neutron.common.response.BaseResponse;
import com.neutron.common.response.ErrorCode;
import com.neutron.common.response.ResultUtils;
import com.neutron.servicebackend.service.InterfaceInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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


}
