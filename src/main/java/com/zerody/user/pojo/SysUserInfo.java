package com.zerody.user.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.user.pojo.base.BaseModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class SysUserInfo extends BaseModel {

    //用户姓名
    private String userName;

    //性别
    private Integer gender;

    //手机号
    @NotEmpty
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





}