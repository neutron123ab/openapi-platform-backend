package com.neutron.common.service;

import com.neutron.common.model.entity.InterfaceInfo;

/**
 * @author zzs
 * @date 2023/4/11 15:30
 */
public interface DubboInterfaceService {

    /**
     * 根据请求路径和请求方法查询接口信息
     *
     * @param url 请求路径
     * @param method 请求方法
     * @return 接口信息
     */
    InterfaceInfo getInterfaceInfo(String url, String method);

    /**
     * 接口总调用次数加1
     *
     * @param interfaceId 接口id
     * @return 是否修改成功
     */
    Boolean addTotalInvoke(Long interfaceId);

}
