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

    private String userId;

    private  String userName;

    private String mobile;

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

    private String avatar;
}
