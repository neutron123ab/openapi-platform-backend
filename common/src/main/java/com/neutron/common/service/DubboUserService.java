package com.neutron.common.service;

import com.neutron.common.model.entity.User;

/**
 * @author zzs
 * @date 2023/4/10 21:44
 */
public interface DubboUserService {

    /**
     * 通过用户标识查询用户信息
     *
     * @param accessKey 用户标识
     * @return 用户信息
     */
    User queryUser(String accessKey);

    /**
     * 向redis中存储nonce
     *
     * @param nonce 防重放标志
     * @return 是否添加成功
     */
    Boolean addNonce(String nonce);

}
