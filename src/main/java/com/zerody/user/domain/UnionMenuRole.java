package com.zerody.user.domain;

/**
 *
 * 菜单，角色关联表
 *
 * @author
 * @description          DELL
 * @date                 2021/1/19 14:57
 * @param
 * @return
 */
public class UnionMenuRole {
    private String id;

    /** 菜单id **/
    private String menuId;

    /** 角色id **/
    private String roleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId == null ? null : menuId.trim();
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }
}