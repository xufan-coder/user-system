package com.zerody.user.pojo;

import com.zerody.user.pojo.base.BaseModel;

import java.util.Date;

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



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getCertificateCard() {
        return certificateCard;
    }

    public void setCertificateCard(String certificateCard) {
        this.certificateCard = certificateCard == null ? null : certificateCard.trim();
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

}