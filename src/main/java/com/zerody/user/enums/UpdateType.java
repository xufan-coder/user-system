package com.zerody.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.itcoon.common.core.enums.CodeEnum;
import com.itcoon.common.core.enums.DescribedEnum;

import java.util.Arrays;

/**
 * @author yumiaoxia
 * @since 2021-06-29
 */
public enum UpdateType implements CodeEnum,DescribedEnum{
    FORCE(1, "强制更新"),
    TIPS(2, "提示建议更新"),
    NONE(3, "不提示更新")
    ;

    @EnumValue
    private final int code;
    private final String description;

    UpdateType(int code, String description) {
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

    @JsonCreator
    public static CodeEnum convert(Integer value) {
        UpdateType[] updateTypes = UpdateType.values();
        return Arrays.stream(updateTypes).filter(updateType -> updateType.code == value).findAny().orElse(null);
    }

}
