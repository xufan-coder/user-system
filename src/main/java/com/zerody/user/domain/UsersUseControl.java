package com.zerody.user.domain;

import lombok.Data;

/**
 * 伙伴登录控制表
 *
 * @author  DaBai
 * @date  2022/3/1 13:57
 */

@Data
public class UsersUseControl {
    private String id;

    /**
     * 伙伴id
     */
    private String userId;

    /**
     * 企业id
     */
    private String companyId;

    /**
     * 伙伴名称
     */
    private String userName;

    /**
     * 所属部门id
     */
    private String deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 手机号码
     */
    private String mobile;

    /**
    *   禁止1/允许2类型
    */
    private Integer type;

}
