package com.zerody.user.enums;

import com.zerody.common.constant.KeyValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author  DaBai
 * @date  2021/11/26 15:26
 */

@AllArgsConstructor
public enum BlacklistTypeEnum implements KeyValue {
    INSIDE(1, "内部人员"),
    EXTERNAL(2, "外部人员");
    @Getter
    @Setter
    private Integer value;
    @Getter
    @Setter
    private String name;

    @Override
    public String getText() {
        return name;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
