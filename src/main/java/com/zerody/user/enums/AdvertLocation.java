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
    PARTNER_HONE(1, "合伙人首页"),
    NORMAL_HOME(2, "普通用户首页"),
    PARTNER_SELF(3, "合伙人我的"),
    NORMAL_SELF(4, "普通用户我的"),
    MY_PARTNERS(5, "我的合伙人");

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
