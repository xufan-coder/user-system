package com.zerody.user.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.user.pojo.base.BaseModel;

import java.util.Date;

public class SysUserInfo extends BaseModel {

    //主键id
    private Integer id;

    //用户姓名
    private String userName;

    //性别
    private Byte gender;

    //手机号
    private String phoneNumber;

    //邮箱
    private String email;

    //微信号
    private String weiXin;

    //昵称
    private String nickname;

    //头像(相对路径)
    private String avatar;

    //出生日期
    private Date birthday;

    //证件类型
    private String certificateType;

    //证件号码
    private String certificateCard;

    //证件照正面uri
    private String certificateCardFrontUri;

    //证件照背面uri
    private String certificateCardBackUri;

    //身份证地址
    private String certificateCardAddress;

    //省市区
    private String provCityDistrict;

    //联系地址
    private String contactAddress;

    //邮政编码
    private String postalCode;

    //用户来源
    private String userFrom;

    //注册时间
    private Date registerTime;

    //名族
    private String nation;

    //籍贯
    private String ancestral;

    //备注
    private String description;

    //状态: 1.enable,0. disable ,-1 deleted
    private Integer status;


    //创建人id
    private String createId;

    //创建人
    private String createUser;

    //创建时间
    private Date createTime;

    //修改人id
    private String updateId;

    //修改人
    private String updateUser;

    //修改时间
    private Date updateTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getWeiXin() {
        return weiXin;
    }

    public void setWeiXin(String weiXin) {
        this.weiXin = weiXin == null ? null : weiXin.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
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

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType == null ? null : certificateType.trim();
    }

    public String getCertificateCard() {
        return certificateCard;
    }

    public void setCertificateCard(String certificateCard) {
        this.certificateCard = certificateCard == null ? null : certificateCard.trim();
    }

    public String getCertificateCardFrontUri() {
        return certificateCardFrontUri;
    }

    public void setCertificateCardFrontUri(String certificateCardFrontUri) {
        this.certificateCardFrontUri = certificateCardFrontUri == null ? null : certificateCardFrontUri.trim();
    }

    public String getCertificateCardBackUri() {
        return certificateCardBackUri;
    }

    public void setCertificateCardBackUri(String certificateCardBackUri) {
        this.certificateCardBackUri = certificateCardBackUri == null ? null : certificateCardBackUri.trim();
    }

    public String getCertificateCardAddress() {
        return certificateCardAddress;
    }

    public void setCertificateCardAddress(String certificateCardAddress) {
        this.certificateCardAddress = certificateCardAddress == null ? null : certificateCardAddress.trim();
    }

    public String getProvCityDistrict() {
        return provCityDistrict;
    }

    public void setProvCityDistrict(String provCityDistrict) {
        this.provCityDistrict = provCityDistrict == null ? null : provCityDistrict.trim();
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress == null ? null : contactAddress.trim();
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode == null ? null : postalCode.trim();
    }

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom == null ? null : userFrom.trim();
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }


    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation == null ? null : nation.trim();
    }

    public String getAncestral() {
        return ancestral;
    }

    public void setAncestral(String ancestral) {
        this.ancestral = ancestral == null ? null : ancestral.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}