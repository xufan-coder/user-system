package com.zerody.user.pojo;

import com.zerody.user.pojo.base.BaseStringModel;
import lombok.Data;

import java.util.Date;

@Data
public class SysLoginInfo extends BaseStringModel {


    //用户名
    private String loginName;

    //密码
    private String userPwd;

    //手机号
    private String mobileNumber;

    //是否启用：类型：1启用，2企业停用 3部门停用
    private Integer activeFlag;

    //用户ID
    private String userId;

    //昵称
    private String nickname;

    //头像
    private String avatar;

    //最后一次登录校验短信验证码时间
    private Date lastChecKSms;

}