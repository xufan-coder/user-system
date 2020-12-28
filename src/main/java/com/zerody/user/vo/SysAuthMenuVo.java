package com.zerody.user.vo;

import lombok.Data;

import java.util.List;

/**
 * @author PengQiang
 * @ClassName SysAuthenuVo
 * @DateTime 2020/12/20_16:50
 * @Deacription TODO
 */
@Data
public class SysAuthMenuVo {

    //菜单id
    private String id;

    //系统id
    private String sysId;

    //父菜单id
    private String parentId;

    //菜单名称
    private String menuName;

    //菜单编码
    private String menuCode;


    //菜单排序
    private Integer menuSort;

    //菜单类型(菜单/按钮)
    private Integer menuType;

    //菜单图标
    private String menuIcon;

    //菜单uri
    private String menuUri;

    //是否被是否拥有该菜单
    private boolean checked;

    private List<SysAuthMenuVo> childrens;

}
