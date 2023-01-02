package com.zerody.user.vo;

import lombok.Data;

@Data
public class InternalControlVo {

    /** 用户id **/
    private String userId;

    /** 伙伴id **/
    private String staffId;

    /** 是否存在 0:否；1是**/
    private Integer isInternalControl;

    /** 用户名称 **/
    private String userName;

    /** 手机号 **/
    private String phone;

    /** 公司名称 **/
    private String companyName;

    /** 部门名称 **/
    private String departName;

    /** 角色名 **/
    private String roleName;

    /** 证件号码 **/
    private String certificateCard;

}
