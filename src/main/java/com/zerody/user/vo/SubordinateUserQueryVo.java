package com.zerody.user.vo;

import lombok.Data;

/**
 * @author PengQiang
 * @ClassName SubordinateUserQueryVo
 * @DateTime 2021/7/13_11:02
 * @Deacription TODO
 */
@Data
public class SubordinateUserQueryVo {

    /**
     * userId
     */
    private String userId;

    /**
     * 用户名称
     */
    private  String userName;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 部门名称
     */
    private String departName;
    /**
     * 岗位名称
     */
    private String positionName;

    /**
     * 角色
     */
    private String roleName;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 企业id
     */
    private String companyId;

    /**
     * 头衔
     */
    private String avatar;

    private Integer status;

    /** 用户类型 企业管理员:0、伙伴：1、团队长：2、副总：3*/
    private Integer userType;

}
