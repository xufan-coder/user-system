package com.zerody.user.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author  DaBai
 * @date  2021/9/7 14:44
 */

@Data
public class StaffInfoByCompanyVo {

    /** user id */
    private String id;

    /**
     * 员工ID
     */
    private String staffId;

    /** 用户姓名 */
    private String userName;

    /** 头像 */
    private String avatar;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 企业ID
     */
    private String compId;

    /**
     * 企业名称
     */
    private String companyName;


    /**
     * 岗位名称
     */
    private String positionName;

    /**
     * 部门名称
     */
    private String departName;

    /**
     * 角色
     */
    private String roleName;
}
