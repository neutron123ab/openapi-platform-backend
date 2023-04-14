package com.neutron.common.model.enums;

import lombok.Getter;

/**
 * @author zzs
 * @date 2023/4/14 10:25
 */
@Getter
public enum InterfaceStatusEnum {

    /**
     * 接口关闭状态
     */
    CLOSE("关闭", 0),
    /**
     * 接口开放状态
     */
    OPEN("开放", 1);

    private final String text;

    private final int value;

    InterfaceStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public static InterfaceStatusEnum getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        InterfaceStatusEnum[] values = InterfaceStatusEnum.values();
        for (InterfaceStatusEnum interfaceStatusEnum : values) {
            if (interfaceStatusEnum.getValue() == value) {
                return interfaceStatusEnum;
            }
        }
        return null;
    }
}
