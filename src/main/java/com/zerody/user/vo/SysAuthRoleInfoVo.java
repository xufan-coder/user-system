package com.zerody.user.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author PengQiang
 * @ClassName SysAuthRoleInfoVo
 * @DateTime 2020/12/16_14:26
 * @Deacription TODO
 */
@Data
public class SysAuthRoleInfoVo {

    private String id;

    //企业id
    private String compId;

    //角色名称
    private String roleName;

    //角色编码
    private String roleCode;

    //角色类型: 0.超级管理员、1.运营商、2.企业管理员、3.普通员工
    private String roleType;

    //角色标签
    private String roleTag;

    //角色描述
    private String roleDesc;

    //角色状态
    private Integer status;
}
