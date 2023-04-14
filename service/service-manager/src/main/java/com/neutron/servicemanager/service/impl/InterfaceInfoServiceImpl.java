package com.neutron.servicemanager.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neutron.common.exception.BusinessException;
import com.neutron.common.model.dto.InterfaceInfoDTO;
import com.neutron.common.model.entity.InterfaceInfo;
import com.neutron.common.model.mapper.InterfaceInfoMapper;
import com.neutron.common.model.request.AddInterfaceRequest;
import com.neutron.common.model.request.ChangeInterfaceRequest;
import com.neutron.common.response.ErrorCode;
import com.neutron.servicemanager.service.InterfaceInfoService;
import org.springframework.stereotype.Service;

/**
 * @author zzs
 * @description 针对表【interface_info(接口信息表)】的数据库操作Service实现
 * @createDate 2023-04-08 16:29:54
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Override
    public Boolean addInterfaceInfo(AddInterfaceRequest addInterfaceRequest) {

        if (BeanUtil.isEmpty(addInterfaceRequest)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtil.copyProperties(addInterfaceRequest, interfaceInfo);

        return save(interfaceInfo);
    }

    @Override
    public Boolean changeInterfaceStatus(ChangeInterfaceRequest changeInterfaceRequest) {
        if (changeInterfaceRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtil.copyProperties(changeInterfaceRequest, interfaceInfo);

        return updateById(interfaceInfo);
    }

    @Override
    public IPage<InterfaceInfoDTO> getAllInterfaceInfo(Long current, Long pageSize) {
        if (current < 1 || pageSize < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<InterfaceInfo> page = page(new Page<>(current, pageSize));
        IPage<InterfaceInfoDTO> convert = page.convert(interfaceInfo -> BeanUtil.copyProperties(interfaceInfo, InterfaceInfoDTO.class));
        if (convert == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "管理员分页查询接口信息失败");
        }
        return convert;
    }
}




