package com.zerody.user.enums;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author PengQiang
 * @ClassName ImStateEnum
 * @DateTime 2022/4/28_10:56
 * @Deacription TODO
 */
@AllArgsConstructor
public enum DictTypeEnum {
    IM_STATE("imState"),
    NO_CALL("noCall"),
    LEAVE_TYPE("leaveType"),
    CAUSE_COMPLAINT("causeComplaint"),
    REJECT("reject"),
    REVOKE("revoke")
    ;
    @Getter
    private String alias;

    public static boolean aliasExist(String alias) {
        if (StringUtils.isEmpty(alias)) {
            return Boolean.FALSE;
        }
        for (DictTypeEnum value : values()) {
            if (value.getAlias().equals(alias)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public static String getCodeByAlias(String alias) {
        if (StringUtils.isEmpty(alias)) {
            return null;
        }
        for (DictTypeEnum value : values()) {
            if (value.getAlias().equals(alias)) {
                return value.name();
            }
        }
        return null;
    }
}
