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

    /** 是否预备高管 0表示否 1表示是 2表示退学*/
    private Integer isPrepareExecutive;
}
