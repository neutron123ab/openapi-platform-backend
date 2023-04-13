package com.neutron.common.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zzs
 * @date 2023/4/13 20:24
 */
@Data
public class KeysVO implements Serializable {
    private static final long serialVersionUID = 6136725028935654647L;

    /**
     * 用户标识
     */
    private String accessKey;

    /**
     * 秘钥
     */
    private String secretKey;
}
