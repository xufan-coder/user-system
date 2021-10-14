package com.zerody.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.itcoon.common.core.enums.CodeEnum;
import com.itcoon.common.core.enums.DescribedEnum;

/**
 * @author zhangpingping
 * @date 2021年10月13日 17:24
 */

public enum OsType implements CodeEnum, DescribedEnum {
    IOS(1, "IOS"),
    ANDROID(2, "安卓");

    @EnumValue
    private final int code;
    private final String description;

    OsType(int code, String description) {
        this.code = code;
        this.description = description;
    }
    @Override
    public String description() {
        return description;
    }

    @Override
    public Integer value() {
        return this.code;
    }
}
