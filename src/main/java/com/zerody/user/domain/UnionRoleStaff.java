package com.zerody.user.domain;

/**
 *
 * 员工角色关联表
 *
 * @author
 * @description          DELL
 * @date                 2021/1/19 14:57
 * @param
 * @return
 */
public class UnionRoleStaff {
    /** id **/
    private String id;

    /** 员工id **/
    private String staffId;

    /** 角色id **/
    private String roleId;

    /** 角色名 **/
    private String roleName;

    public UnionRoleStaff(){
        super();
    }
    public UnionRoleStaff(String staffId,String roleId){
        this.roleId=roleId;
        this.staffId=staffId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId == null ? null : staffId.trim();
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }
}