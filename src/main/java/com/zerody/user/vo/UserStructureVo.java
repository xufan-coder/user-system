package com.zerody.user.vo;

import lombok.Data;

/**
 * @author PengQiang
 * @ClassName UserStructureVo
 * @DateTime 2021/3/31_16:47
 * @Deacription TODO
 */
@Data
public class UserStructureVo {

    /** 企业id */
    private String companyId;

    /** 企业名称 */
    private String companyName;

    /** 类型 1.为企业 2.部门 3.用户 */
    private Integer type;

    /** 部门id */
    private String departId;

    /** 部门名称 */
    private String departName;

    /** 用户id */
    private String userId;

    /** 用户名称 */
    private String userName;
}
