package com.neutron.common.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zzs
 * @date 2023/4/15 11:10
 */
@Data
public class Params implements Serializable {
    private static final long serialVersionUID = -5744924520617907219L;

    /**
     * 参数名
     */
    private String name;

    /**
     * 该参数是否必填
     */
    private Boolean isRequired;

    /**
     * 参数类型
     */
    private String type;

    /**
     * 参数说明
     */
    private String description;
}
