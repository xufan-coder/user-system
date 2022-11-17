package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.user.constant.CheckCompare;
import com.zerody.user.domain.base.BaseModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 *
 *
 * @author               PengQiang
 * @description          DELL
 * @date                 2021/1/19 14:28
 * @param
 * @return
 */
@Data
public class  SysUserInfo extends BaseModel {

    /** 用户名 **/
    @NotEmpty(message = "用户名不能为空")
    @CheckCompare(value = "userName",name = "名称")
    private String userName;

    /** 性别 **/
    @CheckCompare(value = "gender",name = "性别")
    private Integer gender;

    /** 手机号 **/
    @NotEmpty(message = "手机号不能为空")
    @Size(max = 11, min = 11, message = "手机号必须为11位")
    private String phoneNumber;

    /** 邮箱 **/
    @CheckCompare(value = "email",name = "邮箱")
    private String email;


    /** 昵称 **/
    @CheckCompare(value = "nickname",name = "昵称")
    private String nickname;

    /** 头像(相对路径) **/
    @CheckCompare(value = "avatar",name = "头像")
    private String avatar;

    /** 出生日期 **/
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @CheckCompare(value = "birthday",name = "出生日期")
    private Date birthday;


    /** 证件号码 **/
    @CheckCompare(value = "certificateCard",name = "证件号码")
    private String certificateCard;


    /** 身份证地址 **/
    @CheckCompare(value = "certificateCardAddress",name = "身份证地址")
    private String certificateCardAddress;

    /** 省市区 **/
    private String provCityDistrict;

    /** 联系地址 **/
    @CheckCompare(value = "contactAddress",name = "联系地址")
    private String contactAddress;

    /** 注册时间 **/
    private Date registerTime;

    /** 民族 **/
    @CheckCompare(value = "nation",name = "民族")
    private String nation;

    /** 籍贯 **/
    @CheckCompare(value = "ancestral",name = "籍贯")
    private String ancestral;

    /** 备注 **/
    @CheckCompare(value = "description",name = "备注")
    private String description;

    /** 状态: 1.enable,0. disable ,-1 deleted **/
    @CheckCompare(value = "status",name = "状态")
    private Integer status;

    /** 最高学历 **/
    @CheckCompare(value = "highestEducation",name = "最高学历")
    private String highestEducation;

    /** 毕业院校 **/
    @CheckCompare(value = "graduatedFrom",name = "毕业院校")
    private String graduatedFrom;

    /** 所学专业 **/
    @CheckCompare(value = "major",name = "所学专业")
    private String major;

    /** 婚姻状态 **/
    @CheckCompare(value = "maritalStatus",name = "婚姻状态")
    private Integer maritalStatus;

    /** crmOpenId **/
    private String crmOpenId;

    /** scrmOpenId **/
    private String scrmOpenId;

    /**
    *   微信unionId
    */
    private String unionId;

    /**
    *   紧急联系人姓名
    */
    @CheckCompare(value = "urgentName",name = "紧急联系人姓名")
    private String urgentName;

    /**
    *   '紧急联系人关系'
    */
    @CheckCompare(value = "urgentRelation",name = "紧急联系人关系")
    private String urgentRelation;

    /**
    *   '紧急联系人电话'
    */
    @CheckCompare(value = "urgentPhone",name = "紧急联系人电话")
    private String urgentPhone;

    /**
    *   '家庭成员姓名'
    */
    @CheckCompare(value = "familyName",name = "家庭成员姓名")
    private String familyName;

    /**
    *   '家庭成员关系'
    */
    @CheckCompare(value = "familyName",name = "家庭成员姓名")
    private String familyRelation;

    /**
    *   '家庭成员电话'
    */
    @CheckCompare(value = "familyPhone",name = "家庭成员电话")
    private String familyPhone;

    /**
    *   '家庭成员职业'
    */
    @CheckCompare(value = "familyJob",name = "家庭成员职业")
    private String familyJob;

    /**
    *   '家庭成员地址'
    */
    private String familyAddr;

    /** 业绩查看密码 */
    private String performanceShowPassword;

    /** 是否有签单 */
    private Integer isSignOrder;

    /** 角色名称 */
    @TableField(exist = false)
    private String roleName;

    private Date avatarUpdateTime;

    private Integer isDeleted;

    /** 是否修改用户名称名称(1.是，0.否) */
    private Integer isUpdateName;

    private Integer isEdit;

    private String imState;

    /**顾问关联同步(0-未同步 1-已同步)*/
    private Integer adviserPush;

    /**生日月*/
    private Integer birthdayMonth;
    /**生日天*/
    private Integer birthdayDay;

    /**
     * 用户状态是否修改(1.是、0.否)
     */
    private Integer statusEdit;
}
