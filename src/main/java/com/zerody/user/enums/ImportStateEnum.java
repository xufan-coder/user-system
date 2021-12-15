package com.zerody.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author PengQiang
 * @ClassName ClewImportState
 * @DateTime 2021/7/22_15:47
 * @Deacription TODO
 */
@AllArgsConstructor
public enum ImportStateEnum {
    NOT("未处理"),
    UNDERWAY("处理中"),
    DELETED("已删除"),
    ACCOMPLISH("已完成")
    ;

    @Getter
    private String desc;

    public static String getDesc(String code){
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        try {
            return ImportStateEnum.valueOf(code).getDesc();
        } catch (Exception e) {
            return null;
        }

    }
}
