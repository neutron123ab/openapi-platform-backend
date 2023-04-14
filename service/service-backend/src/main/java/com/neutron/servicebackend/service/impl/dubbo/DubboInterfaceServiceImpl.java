package com.neutron.servicebackend.service.impl.dubbo;

import cn.hutool.core.text.CharSequenceUtil;
import com.neutron.common.exception.BusinessException;
import com.neutron.common.model.entity.InterfaceInfo;
import com.neutron.common.response.ErrorCode;
import com.neutron.common.service.DubboInterfaceService;
import com.neutron.servicebackend.service.InterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author zzs
 * @date 2023/4/11 15:32
 */
@DubboService
public class DubboInterfaceServiceImpl implements DubboInterfaceService {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        if(CharSequenceUtil.hasBlank(url, method)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return interfaceInfoService.query().eq("url", url).eq("method", method).one();
    }

    @Override
    public Boolean addTotalInvoke(Long interfaceId) {
        if (interfaceId == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return interfaceInfoService.addTotalInvoke(interfaceId);
    }
}
