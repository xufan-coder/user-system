package com.zerody.user.pojo;

import com.zerody.user.pojo.base.BaseModel;
import lombok.Data;

import java.util.Date;

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


}