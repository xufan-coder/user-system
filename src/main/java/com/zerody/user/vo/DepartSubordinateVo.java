package com.zerody.user.vo;

import lombok.Data;

/**
 * 部门信息
 * @author PengQiang
 * @ClassName getDepartSubordinateVo
 * @DateTime 2021/8/2_17:29
 * @Deacription TODO
 */
@Data
public class DepartSubordinateVo {

    /** 部门id */
    private String id;

    /** 部门名称 */
    private String name;

    /** 是否有下级部门 */
    private Boolean  isSubordinate;

}
