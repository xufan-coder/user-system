package com.zerody.user.pojo;

import com.zerody.user.pojo.base.BaseModel;

import java.util.Date;

public class SysAuthMenu extends BaseModel {


    //系统id
    private String sysId;

    //父菜单id
    private String parentId;

    //菜单名称
    private String menuName;

    //菜单编码
    private String menuCode;

    //菜单层级(同级菜单归类)
    private Byte menuLayer;

    //菜单排序
    private Byte menuSort;

    //菜单类型(菜单/按钮)
    private Byte menuType;

    //菜单图标
    private String menuIcon;

    //菜单uri
    private String menuUri;

    //是否启用该菜单
    private Boolean isShow;

    //菜单描述
    private String menuDesc;


    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId == null ? null : sysId.trim();
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode == null ? null : menuCode.trim();
    }

    public Byte getMenuLayer() {
        return menuLayer;
    }

    public void setMenuLayer(Byte menuLayer) {
        this.menuLayer = menuLayer;
    }

    public Byte getMenuSort() {
        return menuSort;
    }

    public void setMenuSort(Byte menuSort) {
        this.menuSort = menuSort;
    }

    public Byte getMenuType() {
        return menuType;
    }

    public void setMenuType(Byte menuType) {
        this.menuType = menuType;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon == null ? null : menuIcon.trim();
    }

    public String getMenuUri() {
        return menuUri;
    }

    public void setMenuUri(String menuUri) {
        this.menuUri = menuUri == null ? null : menuUri.trim();
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public String getMenuDesc() {
        return menuDesc;
    }

    public void setMenuDesc(String menuDesc) {
        this.menuDesc = menuDesc == null ? null : menuDesc.trim();
    }
}