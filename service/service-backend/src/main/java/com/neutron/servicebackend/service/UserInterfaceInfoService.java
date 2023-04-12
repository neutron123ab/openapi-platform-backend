package com.neutron.servicebackend.service;

import com.neutron.common.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zzs
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service
* @createDate 2023-04-08 16:44:58
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 判断用户是否还有该接口的调用次数
     * @param interfaceId 接口id
     * @param userId 用户id
     * @return true：还有调用次数
     */
    Boolean getLeftInvokeNums(long interfaceId, long userId);

    /**
     * 用户调用接口次数加1（即剩余调用次数减1)
     *
     * @param interfaceId 接口id
     * @param userId      用户id
     * @return 是否修改成功
     */
    Boolean addInvokeNum(long interfaceId, long userId);

}
