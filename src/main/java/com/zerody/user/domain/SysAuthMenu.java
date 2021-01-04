package com.zerody.user.domain;

import com.zerody.user.domain.base.BaseModel;
import lombok.Data;

@Data
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
    private Integer menuLayer;

    //菜单排序
    private Integer menuSort;

    //菜单类型(菜单/按钮)
    private Integer menuType;

    //菜单图标
    private String menuIcon;

    //菜单uri
    private String menuUri;

    //是否启用该菜单
    private Boolean isShow;

    //菜单描述
    private String menuDesc;


}