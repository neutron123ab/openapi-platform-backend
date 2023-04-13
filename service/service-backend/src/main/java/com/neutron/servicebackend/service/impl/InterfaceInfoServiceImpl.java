package com.neutron.servicebackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neutron.common.model.dto.InterfaceInfoDTO;
import com.neutron.common.model.entity.InterfaceInfo;
import com.neutron.common.model.entity.User;
import com.neutron.common.model.enums.UserRoleEnum;
import com.neutron.common.model.mapper.InterfaceInfoMapper;
import com.neutron.servicebackend.service.InterfaceInfoService;
import com.neutron.servicebackend.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zzs
 * @description 针对表【interface_info(接口信息表)】的数据库操作Service实现
 * @createDate 2023-04-08 16:29:54
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Resource
    private UserService userService;

    @Override
    public List<InterfaceInfoDTO> getAllInterfaceInfo(Long userId) {

        User user = userService.query().eq("id", userId).one();
        Integer userRoleValue = user.getUserRole();
        UserRoleEnum userRole = UserRoleEnum.getEnumByValue(userRoleValue);
        //如果用户是管理员，则查询所有接口信息
        if (userRole.equals(UserRoleEnum.MANAGER)) {
            return list().stream()
                    .map(interfaceInfo -> {
                        InterfaceInfoDTO interfaceInfoDTO = new InterfaceInfoDTO();
                        BeanUtil.copyProperties(interfaceInfo, interfaceInfoDTO);
                        return interfaceInfoDTO;
                    })
                    .collect(Collectors.toList());
        }
        //如果是普通用户则只查询已上线接口
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        return list(queryWrapper).stream()
                .map(interfaceInfo -> {
                    InterfaceInfoDTO interfaceInfoDTO = new InterfaceInfoDTO();
                    BeanUtil.copyProperties(interfaceInfo, interfaceInfoDTO);
                    return interfaceInfoDTO;
                })
                .collect(Collectors.toList());
    }
}