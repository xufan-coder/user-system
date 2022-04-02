package com.zerody.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import com.itcoon.common.core.enums.CodeEnum;
import com.itcoon.common.core.enums.DescribedEnum;

/**
 * @author yumiaoxia
 * @since 2021-07-06
 */
public enum AdvertType implements CodeEnum, DescribedEnum {

    CAROUSEL(1, "轮播"),
    BULLET_FRAME(2, "弹框"),
    ADVERTISEMENT(3, "广告图")
    ;

    @EnumValue
    private final int code;
    private final String description;

    AdvertType(int code, String description) {
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
