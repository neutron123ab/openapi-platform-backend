package com.neutron.servicebackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neutron.common.model.dto.InterfaceInfoDTO;
import com.neutron.common.model.entity.InterfaceInfo;

/**
 * @author zzs
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 分页查询已上线接口
     * @param current 当前页
     * @param pageSize 页大小
     * @return 接口信息列表
     */
    IPage<InterfaceInfoDTO> getOpenInterface(Long current, Long pageSize);

    /**
     * 接口总调用次数加1
     *
     * @param interfaceId 接口id
     * @return 是否修改成功
     */
    Boolean addTotalInvoke(Long interfaceId);

}