package com.neutron.common.model.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neutron.common.model.entity.UserInterfaceInfo;
import com.neutron.common.model.service.UserInterfaceInfoService;
import com.neutron.common.model.mapper.UserInterfaceInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author zzs
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service实现
* @createDate 2023-04-08 16:44:57
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

}




