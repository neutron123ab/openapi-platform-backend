package com.neutron.servicemanager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.neutron.common.model.dto.InterfaceInfoDTO;
import com.neutron.common.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neutron.common.model.request.AddInterfaceRequest;
import com.neutron.common.model.request.ChangeInterfaceRequest;

/**
 * @author zzs
 * @description 针对表【interface_info(接口信息表)】的数据库操作Service
 * @createDate 2023-04-08 16:29:54
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 上传接口
     *
     * @param addInterfaceRequest 接口信息
     * @return 是否上传成功
     */
    Boolean addInterfaceInfo(AddInterfaceRequest addInterfaceRequest);

    /**
     * 修改接口信息
     *
     * @param changeInterfaceRequest 修改接口请求
     * @return 是否修改成功
     */
    Boolean changeInterfaceStatus(ChangeInterfaceRequest changeInterfaceRequest);

    /**
     * 管理员分页查询所有接口信息
     * @param current 当前页
     * @param pageSize 页大小
     * @return 页面信息
     */
    IPage<InterfaceInfoDTO> getAllInterfaceInfo(Long current, Long pageSize);
}
