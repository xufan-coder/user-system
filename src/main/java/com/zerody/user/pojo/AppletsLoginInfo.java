package com.zerody.user.pojo;

import com.zerody.user.pojo.base.BaseStringModel;
import lombok.Data;

import java.util.Date;

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

}