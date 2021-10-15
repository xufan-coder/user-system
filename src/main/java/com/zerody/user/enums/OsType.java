package com.zerody.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author zhangpingping
 * @date 2021年10月13日 17:24
 */

public enum OsType  {
    IOS(1, "IOS"),
    ANDROID(2, "安卓");

    @EnumValue
    private final int code;
    private final String description;

    OsType(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
