package com.zerody.user.execption;

import com.itcoon.common.exception.ex.ExceptionResponseEnum;
import com.itcoon.common.exception.ex.ServiceException;
import com.zerody.common.constant.KeyValue;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yumiaoxia
 * @since 2021-06-29
 */
public enum PlatformResponseEnum implements ExceptionResponseEnum<ServiceException> {
    INVALID_TOKEN(1, "访问令牌无效"),
    APP_VERSION_MISSING(2, "该app版本不存在"),
    LINK_URL_EMPTY(3, "链接url不能为空")
    ;

    private final int code;
    private final String message;


    PlatformResponseEnum(int code, String message) {
        this.code = code;
        this.message= message;
    }

    @Override
    public ServiceException bindException() {
        return new ServiceException(this);
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
