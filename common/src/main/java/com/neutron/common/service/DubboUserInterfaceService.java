package com.neutron.common.service;

/**
 * @author zzs
 * @date 2023/4/11 15:53
 */
public interface DubboUserInterfaceService {

    /**
     * 判断用户是否还有该接口的调用次数
     *
     * @param interfaceId 接口id
     * @param userId      用户id
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
