package com.neutron.common.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户类脱敏
 * @author zzs
 * @date 2023/4/9 15:53
 */
@Data
public class UserDTO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 账号
     */
    private String account;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 用户标识
     */
    private String accessKey;


    /**
     * 用户角色（0-普通用户，1-管理员）
     */
    private Integer userRole;

    private static final long serialVersionUID = 1L;
}
