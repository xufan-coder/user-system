package com.zerody.user.domain;

import lombok.Data;

/**
 * @author  DaBai
 * @date  2021/1/4 18:15
 */
@Data
public class UnionPlatformRoleStaff {
    private String id;

    //员工id
    private String staffId;

    //角色id
    private String platformRoleId;

}