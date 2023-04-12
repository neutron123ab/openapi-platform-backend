package com.neutron.servicebackend.service.impl.dubbo;

import com.neutron.common.service.DubboUserInterfaceService;
import com.neutron.servicebackend.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author zzs
 * @date 2023/4/11 15:55
 */
@DubboService
public class DubboUserInterfaceServiceImpl implements DubboUserInterfaceService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public Boolean getLeftInvokeNums(long interfaceId, long userId) {
        return userInterfaceInfoService.getLeftInvokeNums(interfaceId, userId);
    }

    @Override
    public Boolean addInvokeNum(long interfaceId, long userId) {
        return userInterfaceInfoService.addInvokeNum(interfaceId, userId);
    }
}
