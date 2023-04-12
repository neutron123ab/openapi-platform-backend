package com.neutron.common.model.enums;

import lombok.Getter;

/**
 * @author zzs
 * @date 2023/4/11 20:22
 */
@Getter
public enum UserRoleEnum {

    /**
     * 普通用户
     */
    NORMAL("普通用户", 0),
    /**
     * 管理员
     */
    MANAGER("管理员", 1);

    private final String text;

    private final int value;

    UserRoleEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据值大小获取状态
     *
     * @param value 类型值
     * @return 状态
     */
    public static UserRoleEnum getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        UserRoleEnum[] values = UserRoleEnum.values();
        for (UserRoleEnum userRoleEnum : values) {
            if (userRoleEnum.getValue() == value) {
                return userRoleEnum;
            }
        }
        return null;
    }
}
