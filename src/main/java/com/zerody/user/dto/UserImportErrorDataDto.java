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

    /** 状态 */
    private String status;

    /** 性别 */
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

    /** 学历 */
    private String highestEducation;

    /** 毕业院校 */
    private String graduatedFrom;

    /** 所学专业 */
    private String major;

}