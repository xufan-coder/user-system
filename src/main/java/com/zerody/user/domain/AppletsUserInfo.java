package com.zerody.user.domain;

import com.zerody.user.domain.base.BaseModel;
import lombok.Data;

import java.util.Date;

@Data
public class AppletsUserInfo extends BaseModel {



    //用户姓名
    private String userName;

    //手机号
    private String phoneNumber;

    //头像(相对路径)
    private String avatar;

    //出生日期
    private Date birthday;

    //身份证件号码
    private String certificateCard;

    //openId
    private String openId;

    //备注
    private String remark;

}