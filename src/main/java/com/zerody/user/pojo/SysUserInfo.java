package com.zerody.user.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.user.pojo.base.BaseModel;
import lombok.Data;
import lombok.Value;
import org.mapstruct.Mapper;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class  SysUserInfo extends BaseModel {

    //用户姓名
    @NotEmpty(message = "用户名不能为空")
    private String userName;

    //性别
    private Integer gender;

    //手机号
    @NotEmpty(message = "手机号不能为空")
    @Size(max = 11, min = 11, message = "手机号必须为11位")
    private String phoneNumber;

    //邮箱
    private String email;


    //昵称
    private String nickname;

    //头像(相对路径)
    private String avatar;

    //出生日期
    private Date birthday;


    //证件号码
    @NotEmpty(message = "证件号码不能为空")
    private String certificateCard;


    //身份证地址
    @NotEmpty(message = "身份证地址不能为空")
    private String certificateCardAddress;

    //省市区
    @NotEmpty(message = "省市区不能为空")
    private String provCityDistrict;

    //联系地址
    private String contactAddress;

    //邮政编码
    private String postalCode;

    //注册时间
    private Date registerTime;

    //民族
    @NotEmpty(message = "民族不能为空")
    private String nation;

    //籍贯
    @NotEmpty(message = "籍贯不能为空")
    private String ancestral;

    //备注
    private String description;

    //状态: 1.enable,0. disable ,-1 deleted
    private Integer status;

    //最高学历
    private String highestEducation;

    //毕业院校
    private String graduatedFrom;

    //所学专业
    private String major;

    //婚姻状态
    private String maritalStatus;

    //微信openId
    private String openId;
}