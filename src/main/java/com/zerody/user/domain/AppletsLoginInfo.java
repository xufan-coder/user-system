package com.zerody.user.domain;

import com.zerody.user.domain.base.BaseStringModel;
import lombok.Data;

@Data
public class AppletsLoginInfo extends BaseStringModel {

    //用户名
    private String loginUserName;

    //密码
    private String userPwd;

    //手机号
    private String phoneNumber;

    //小程序用户id
    private String userId;

    //备注
    private String remark;

}