package com.zerody.user.pojo;

import com.zerody.user.pojo.base.BaseModel;

import java.util.Date;

public class SysAuthRoleInfo extends BaseModel {


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

    //角色有效期
    private Date roleValidityPeriod;

    //父级角色id
    private String parentId;

    //角色有效期起
    private Date roleValidityStart;

    //角色描述
    private String roleDesc;


    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId == null ? null : compId.trim();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode == null ? null : roleCode.trim();
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType == null ? null : roleType.trim();
    }

    public String getRoleTag() {
        return roleTag;
    }

    public void setRoleTag(String roleTag) {
        this.roleTag = roleTag == null ? null : roleTag.trim();
    }

    public Date getRoleValidityPeriod() {
        return roleValidityPeriod;
    }

    public void setRoleValidityPeriod(Date roleValidityPeriod) {
        this.roleValidityPeriod = roleValidityPeriod;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public Date getRoleValidityStart() {
        return roleValidityStart;
    }

    public void setRoleValidityStart(Date roleValidityStart) {
        this.roleValidityStart = roleValidityStart;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc == null ? null : roleDesc.trim();
    }
}