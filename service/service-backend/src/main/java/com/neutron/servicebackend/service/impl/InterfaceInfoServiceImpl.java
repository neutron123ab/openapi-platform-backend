package com.neutron.servicebackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neutron.common.exception.BusinessException;
import com.neutron.common.model.dto.InterfaceInfoDTO;
import com.neutron.common.model.entity.InterfaceInfo;
import com.neutron.common.model.entity.Params;
import com.neutron.common.model.enums.InterfaceStatusEnum;
import com.neutron.common.model.mapper.InterfaceInfoMapper;
import com.neutron.common.response.ErrorCode;
import com.neutron.servicebackend.service.InterfaceInfoService;
import com.neutron.servicebackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zzs
 * @description 针对表【interface_info(接口信息表)】的数据库操作Service实现
 * @createDate 2023-04-08 16:29:54
 */
@Slf4j
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Resource
    private UserService userService;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public IPage<InterfaceInfoDTO> getOpenInterface(Long current, Long pageSize) {
        if (current < 1 || pageSize < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //只查询已上线的接口
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", InterfaceStatusEnum.OPEN.getValue());
        Page<InterfaceInfo> page = page(new Page<>(current, pageSize), queryWrapper);
        IPage<InterfaceInfoDTO> convert = page.convert(interfaceInfo -> {
            Gson gson = new Gson();
            InterfaceInfoDTO interfaceInfoDTO = new InterfaceInfoDTO();
            BeanUtil.copyProperties(interfaceInfo, interfaceInfoDTO);
            String requestParams = interfaceInfo.getRequestParams();
            String responseParamsJson = interfaceInfo.getResponseParams();
            List<Params> params = gson.fromJson(requestParams, new TypeToken<List<Params>>() {
            }.getType());
            List<Params> responseParams = gson.fromJson(responseParamsJson, new TypeToken<List<Params>>() {
            }.getType());
            interfaceInfoDTO.setParamsList(params);
            interfaceInfoDTO.setResponseParamsList(responseParams);
            return interfaceInfoDTO;
        });
        if (convert == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "分页查询已上线接口失败");
        }
        return convert;
    }

    @Override
    public Boolean addTotalInvoke(Long interfaceId) {
        if (interfaceId == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        RLock lock = redissonClient.getLock("openapi:interface:addTotal");
        try {
            while(true) {
                //不断循环尝试获取锁
                if (lock.tryLock(0, TimeUnit.SECONDS)) {
                    //获取到锁执行修改数据
                    QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", interfaceId);
                    boolean isUpdate = update().eq("id", interfaceId)
                            .setSql("total = total + 1")
                            .update();
                    if (!isUpdate) {
                        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "增加接口调用次数方法失败");
                    }
                    return true;
                }
            }
        } catch (InterruptedException e) {
            log.error("修改接口表失败",e);
            return false;
        } finally {
            if(lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}