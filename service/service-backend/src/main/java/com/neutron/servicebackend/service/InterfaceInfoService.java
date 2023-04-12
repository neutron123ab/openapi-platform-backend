package com.neutron.servicebackend.service;

import com.neutron.common.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neutron.common.model.request.AddInterfaceRequest;

/**
* @author zzs
* @description 针对表【interface_info(接口信息表)】的数据库操作Service
* @createDate 2023-04-08 16:29:54
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 上传接口
     * @param addInterfaceRequest 接口信息
     * @return 是否上传成功
     */
    Boolean addInterfaceInfo(AddInterfaceRequest addInterfaceRequest);

}
