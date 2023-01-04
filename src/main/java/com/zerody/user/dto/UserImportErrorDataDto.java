package com.zerody.user.dto;

import lombok.Data;

/**
 * @author PengQiang
 * @ClassName UserImportErrorDataDto
 * @DateTime 2021/12/20_10:16
 * @Deacription TODO
 */
@Data
public class UserImportErrorDataDto {

    /** 名称 */
    private String name;

    /** 手机号码 */
    private String mobile;

    /** 企业 */
    private String companyName;

    /** 部门 */
    private String departName;

    /** 岗位 */
    private String jobName;

    /** 角色 */
    private String roleName;

    private String dateJoin;

    /** 推荐人手机号码 */
    private String recommendMobile;

    /** 状态 */
    private String status;

    /** 性别(0:男，1:女，3:未知) */
    private String gender;

    /** 籍贯 */
    private String ancestral;

    /** 民族 */
    private String nation;

    /** 婚姻 */
    private String marital;

    /** 出生日期 */
    private String birthday;

    /** 身份证号码 */
    private String idCard;

    /** 户籍地址 */
    private String certificateCardAddress;

    /** 联系地址 */
    private String contactAddress;

    /** 邮箱 */
    private String email;

    /**
     * 最高学历(枚举)
     * PRIMARY_SCHOOL("小学"), JUNIOR_HIGH("初中"), TECHNICAL_SECONDARY("中专"), SENIOR_HIGH("高中"),
     * JUNIOR_COLLEGE("大专"), REGULAR_COLLEGE("本科"), MASTER("硕士"), DOCTOR("博士");
     **/
    private String highestEducation;

    /** 毕业院校 */
    private String graduatedFrom;

    /** 所学专业 */
    private String major;
    /**
     *   紧急联系人姓名
     */
    private String urgentName;

    /**
     *   '紧急联系人关系'
     */
    private String urgentRelation;

    /**
     *   '紧急联系人电话'
     */
    private String urgentPhone;

    /** 姓名 */
    private String familyName;

    /** 号码 */
    private String familyMobile;

    /** 关系 */
    private String relationship;

    /** 职位 */
    private String profession;

    /** 联系地址 */
    private String familyContactAddress;

}
