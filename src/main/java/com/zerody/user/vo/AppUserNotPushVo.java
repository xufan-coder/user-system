package com.zerody.user.vo;

import lombok.Data;

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

    /** 负责人部门id */
    private String departmentId;

    /**上级部门id*/
    private String parentId;

    /**用户部门id*/
    private String userDepartmentId;

    /** 用户部门名称 */
    private String userDepartmentName;

}
