package com.neutron.servicebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neutron.common.model.entity.User;
import com.neutron.common.model.mapper.UserMapper;
import com.neutron.servicebackend.service.UserService;
import org.springframework.stereotype.Service;

/**
* @author zzs
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2023-04-08 16:27:06
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

}




