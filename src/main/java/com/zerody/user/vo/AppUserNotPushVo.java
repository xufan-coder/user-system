package com.zerody.user.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author PengQiang
 * @ClassName AppUserNotPushVo
 * @DateTime 2022/6/7_18:37
 * @Deacription TODO
 */
@Data
public class AppUserNotPushVo {

    /** 用户id */
    private String userId;

    /** 企业id */
    private String companyId;

    /** 伙伴名称 */
    private String userName;

    /**头像*/
    private String avatar;

    /** 负责人部门id */
    private String departmentId;

    /**上级部门id*/
    private String parentId;

    /**用户部门id*/
    private String userDepartmentId;

    /** 用户部门名称 */
    private String userDepartmentName;

    /**入职时间*/
    private Date dateJoin;

    /*** 入职年限*/
    private Integer year;

    /*** 签单数*/
    private Integer signOrderNum;

    /*** 放款数*/
    private Integer loansNum;

    /*** 放款金额*/
    private String loansMoney;

    /*** 录入客户数*/
    private Integer importCustomerNum;

}
