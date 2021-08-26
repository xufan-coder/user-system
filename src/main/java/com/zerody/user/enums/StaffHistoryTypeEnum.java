package com.zerody.user.enums;

import com.zerody.common.constant.KeyValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangpingping
 * @date 2021年08月26日 10:17
 */
@AllArgsConstructor
public enum StaffHistoryTypeEnum implements KeyValue {
    HONOR("HONOR", "荣耀"),
    PUNISHMENT("PUNISHMENT", "惩罚");
    @Getter
    @Setter
    private String value;
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
