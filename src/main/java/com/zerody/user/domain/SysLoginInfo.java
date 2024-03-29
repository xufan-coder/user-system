package com.zerody.user.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.user.domain.base.BaseStringModel;
import lombok.Data;

import java.util.Date;

/**
 * 登录信息表
 */

@Data
public class SysLoginInfo extends BaseStringModel {


    //用户名
    private String loginName;

    //密码
    private String userPwd;

    //手机号
    private String mobileNumber;

    //用户ID
    private String userId;

    //昵称
    private String nickname;

    //头像
    private String avatar;

    //最后一次登录校验短信验证码时间
    private Date lastCheckSms;

    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date loginTime;

}
