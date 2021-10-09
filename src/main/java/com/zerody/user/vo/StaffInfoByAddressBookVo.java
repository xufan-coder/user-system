package com.zerody.user.vo;

import lombok.Data;

/**
 * @author zhangpingping
 * @date 2021年09月25日 12:00
 */
 @Data
public class StaffInfoByAddressBookVo {

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
