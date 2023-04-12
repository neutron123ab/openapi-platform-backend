package com.neutron.servicebackend.service.impl.dubbo;

import cn.hutool.core.util.StrUtil;
import com.neutron.common.exception.BusinessException;
import com.neutron.common.model.entity.User;
import com.neutron.common.response.ErrorCode;
import com.neutron.common.service.DubboUserService;
import com.neutron.servicebackend.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

/**
 * @author zzs
 * @date 2023/4/10 21:45
 */
@DubboService
public class DubboUserServiceImpl implements DubboUserService {

    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String OPENAPI_USER_NONCE = "openapi:user:nonce";

    @Override
    public User queryUser(String accessKey) {
        if(StrUtil.isEmptyIfStr(accessKey)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return userService.query().eq("access_key", accessKey).one();
    }

    @Override
    public Boolean addNonce(String nonce) {
        Long add = stringRedisTemplate.opsForSet().add(OPENAPI_USER_NONCE, nonce);
        return add != null && add != 0;
    }
}
