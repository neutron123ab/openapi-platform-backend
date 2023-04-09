package com.neutron.common.model.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neutron.common.model.entity.InterfaceInfo;
import com.neutron.common.model.service.InterfaceInfoService;
import com.neutron.common.model.mapper.InterfaceInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author zzs
* @description 针对表【interface_info(接口信息表)】的数据库操作Service实现
* @createDate 2023-04-08 16:29:54
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{

}




