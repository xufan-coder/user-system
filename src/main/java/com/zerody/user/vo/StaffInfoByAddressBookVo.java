package com.zerody.user.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author zhangpingping
 * @date 2021年09月25日 12:00
 */
@Data
public class StaffInfoByAddressBookVo {

    /**
     * user id
     */
    private String id;

    /**
     * 员工ID
     */
    private String staffId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 头像
     */
    private String avatar;
    /**
     * 头像
     */
    private String userAvatar;

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

    /**
     * 员工类型(企业管理员:0、 伙伴:1、 团队长:2、 副总:3)
     */
    private String userType;

    /**伙伴状态*/
    private Integer status;

    /** 是否预备高管 0表示否 1表示是 2表示退学*/
    private Integer isPrepareExecutive;

    /**
     * 是否钻石会员（0-否 1-是）
     */
    private Integer isDiamondMember;

    private Boolean isSecondContract =false;

    private Date beginTime;

    private Date endTime;

}
