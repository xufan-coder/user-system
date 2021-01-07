package com.zerody.user.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author PengQiang
 * @ClassName SysUserInfoVo
 * @DateTime 2020/12/18_15:19
 * @Deacription TODO
 */
@Data
public class SysUserInfoVo {

    /**
    *   企业名称
    */
    private String companyName;
    /**
    *   企业ID
    */
    private String companyId;

    //员工id
    private String staffId;

    //用户姓名
    private String userName;

    //性别
    private Integer gender;

    //手机号
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
    private String certificateCard;

    //身份证地址
    private String certificateCardAddress;

    //省市区
    private String provCityDistrict;

    //联系地址
    private String contactAddress;


    //注册时间
    private Date registerTime;

    //名族
    private String nation;

    //籍贯
    private String ancestral;

    //备注
    private String description;

    //状态: 用户: 1.enable,0. disable ,-1 deleted；员工:0.生效、1.离职、2.删除、3.合作
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
//    private String openId;

    //角色名
    private String roleName;

    //部门名称
    private String departName;

    //岗位名称
    private String positionName;

    /**
    *   角色ID
    */
    private String roleId;

    /**
    *   部门ID
    */
    private String departId;

    /**
    *   岗位ID
    */
    private String positionId;
}
