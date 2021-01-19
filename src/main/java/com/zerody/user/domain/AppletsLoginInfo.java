package com.zerody.user.domain;

import com.zerody.user.domain.base.BaseStringModel;
import lombok.Data;

/**
 *
 *
 * @author
 * @description          DELL
 * @date                 2021/1/19 14:47
 * @param
 * @return
 */
@Data
public class AppletsLoginInfo extends BaseStringModel {

    /** 用户名 **/
    private String loginUserName;

    /** 密码 **/
    private String userPwd;

    /** 手机号 **/
    private String phoneNumber;

    /** 小程序用户id **/
    private String userId;

    /** 备注 **/
    private String remark;

}