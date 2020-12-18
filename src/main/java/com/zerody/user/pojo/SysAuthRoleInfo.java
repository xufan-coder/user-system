package com.zerody.user.pojo;

import com.zerody.user.pojo.base.BaseModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class SysAuthRoleInfo extends BaseModel {


    //企业id
    private String compId;

    //角色名称
    @NotEmpty(message = "角色名不能为空")
    private String roleName;

    //角色编码
    private String roleCode;

    //角色类型: 0.超级管理员、1.运营商、2.企业管理员、3.普通员工
    @NotEmpty
    private String roleType;

    //角色标签
    private String roleTag;

    //角色有效期
    private Date roleValidityPeriod;

    //父级角色id
    @NotEmpty
    private String parentId;

    //角色有效期起
    private Date roleValidityStart;

    //角色描述
    private String roleDesc;


}