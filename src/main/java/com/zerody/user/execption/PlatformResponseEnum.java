package com.zerody.user.execption;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yumiaoxia
 * @since 2021-06-29
 */
public enum PlatformResponseEnum  {
    INVALID_TOKEN(1, "访问令牌无效"),
    APP_VERSION_MISSING(2, "该app版本不存在"),
    LINK_URL_EMPTY(3, "链接url不能为空")
    ;
    @Getter
    @Setter
    private final int code;
    @Getter
    @Setter
    private final String message;


    PlatformResponseEnum(int code, String message) {
        this.code = code;
        this.message= message;
    }

}
