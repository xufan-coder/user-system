package com.zerody.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;


/**
 * @author yumiaoxia
 * @since 2021-06-29
 */
public enum UpdateType {
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


}
