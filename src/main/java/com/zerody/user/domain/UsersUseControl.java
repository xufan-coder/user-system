package com.zerody.user.domain;

import lombok.Data;
/**
 * @author  DaBai
 * @date  2022/3/1 13:57
 */

@Data
public class UsersUseControl {
    private String id;

    private String userId;

    private String companyId;

    private String userName;

    private String deptId;

    private String deptName;

    private String mobile;

    /**
    *   禁止1/允许2类型
    */
    private Integer type;

}
