package com.zerody.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.itcoon.common.core.enums.CodeEnum;
import com.itcoon.common.core.enums.DescribedEnum;

/**
 * @author zhangpingping
 * @date 2021年10月13日 17:25
 */

public enum SystemType implements CodeEnum, DescribedEnum {
    CRMAPP(1, "CRMAPP"),
    SCRM(2, "SCRMAPP");

    @EnumValue
    private final int code;
    private final String description;

    SystemType(int code, String description) {
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