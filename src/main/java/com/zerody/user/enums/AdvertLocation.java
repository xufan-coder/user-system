package com.zerody.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.itcoon.common.core.enums.CodeEnum;
import com.itcoon.common.core.enums.DescribedEnum;
import com.zerody.common.constant.KeyValue;


/**
 * @author yumiaoxia
 * @since 2021-07-06
 */
public enum AdvertLocation implements CodeEnum, DescribedEnum {
    HONE(1, "首页");

    @EnumValue
    private final int code;
    private final String description;

    AdvertLocation(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public Integer value() {
        return this.code;
    }
}
