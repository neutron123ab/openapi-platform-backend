package com.neutron.servicebackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neutron.common.model.dto.InterfaceInfoDTO;
import com.neutron.common.model.entity.InterfaceInfo;

import java.util.List;

/**
 * @author zzs
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 获取所有接口信息
     * @param userId 用户id
     * @return 接口信息列表
     */
    List<InterfaceInfoDTO> getAllInterfaceInfo(Long userId);

}