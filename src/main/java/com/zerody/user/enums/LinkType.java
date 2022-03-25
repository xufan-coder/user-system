package com.zerody.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import com.itcoon.common.core.enums.CodeEnum;
import com.itcoon.common.core.enums.DescribedEnum;

/**
 * @author yumiaoxia
 * @since 2021-07-06
 */

public enum LinkType implements CodeEnum, DescribedEnum {
    NONE(1, "无跳转"),
    INNER(2, "内链"),
    OUTER(3, "外链"),
    NEWS_LINK(4,"新闻链接")
    ;

    @EnumValue
    private final int code;
    private final String description;

    LinkType(int code, String description) {
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
