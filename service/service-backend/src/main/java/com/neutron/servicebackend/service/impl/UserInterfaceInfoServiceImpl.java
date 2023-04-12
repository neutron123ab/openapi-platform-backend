package com.neutron.servicebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neutron.common.exception.BusinessException;
import com.neutron.common.model.entity.UserInterfaceInfo;
import com.neutron.common.model.mapper.UserInterfaceInfoMapper;
import com.neutron.common.response.ErrorCode;
import com.neutron.servicebackend.service.UserInterfaceInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author zzs
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service实现
* @createDate 2023-04-08 16:44:57
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

    @Override
    public Boolean getLeftInvokeNums(long interfaceId, long userId) {
        if(interfaceId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id不能小于0");
        }
        UserInterfaceInfo userInterfaceInfo = query().eq("interface_id", interfaceId)
                .eq("user_id", userId)
                .one();
        if(userInterfaceInfo == null) {
            UserInterfaceInfo addUserInterface = new UserInterfaceInfo();
            addUserInterface.setUserId(userId);
            addUserInterface.setInterfaceId(interfaceId);
            addUserInterface.setTotalNum(0);
            addUserInterface.setLeftNum(20);
            addUserInterface.setStatus(1);
            boolean save = save(addUserInterface);
            if(!save) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "插入用户调用接口信息错误");
            }
            return true;
        }
        Integer leftNum = userInterfaceInfo.getLeftNum();
        return leftNum > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addInvokeNum(long interfaceId, long userId) {
        if(interfaceId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id不能小于0");
        }
        Long count = query().eq("interface_id", interfaceId)
                .eq("user_id", userId)
                .count();
        //TODO: 后面可以使用配置中心或者创建一个管理员接口来指定默认调用次数
        //当用户第一次调用该接口时，创建一条数据，给予默认调用次数（20）
        if(count < 1) {
            UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
            userInterfaceInfo.setUserId(userId);
            userInterfaceInfo.setInterfaceId(interfaceId);
            userInterfaceInfo.setTotalNum(0);
            userInterfaceInfo.setLeftNum(20);
            boolean save = save(userInterfaceInfo);
            if (!save) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建用户调用接口关系错误");
            }
        }
        //TODO: 这里可能需要使用分布式锁
        return update().eq("interface_id", interfaceId)
                .eq("user_id", userId)
                .setSql("left_num = left_num - 1, total_num = total_num + 1")
                .update();
    }
}




