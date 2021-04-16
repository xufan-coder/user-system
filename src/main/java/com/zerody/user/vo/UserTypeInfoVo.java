package com.zerody.user.vo;

import lombok.Data;

/**
 * @author PengQiang
 * @ClassName UserTypeInfoVo
 * @DateTime 2021/4/1_1:31
 * @Deacription TODO
 */
@Data
public class UserTypeInfoVo {

    /** 用户类型 */
    private Integer userType;

    /** 用户部门名称 */
    private String departName;

}
