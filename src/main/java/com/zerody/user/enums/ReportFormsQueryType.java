package com.zerody.user.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author PengQiang
 * @ClassName ReportFormsQueryType
 * @DateTime 2022/1/7_9:36
 * @Deacription TODO
 */

public enum ReportFormsQueryType {
    COMPAY,
    DEPART,
    TEAM,
    INDIVIDUAL;

    public static boolean getExist(String code) {
        if (StringUtils.isEmpty(code)) {
            return Boolean.FALSE;
        }
        try {
            ReportFormsQueryType.valueOf(code);
            return Boolean.TRUE;
        } catch (IllegalArgumentException e) {
            return Boolean.FALSE;
        }
    }
}
