package com.zerody.user.dto;

import lombok.Data;

/**
 * @author : chenKeFeng
 * @date : 2022/11/28 16:37
 */
@Data
public class DepartInfoDto {

    /**
     * 企业id
     */
    private String compId;

    /**
     * 部门id
     */
    private String departmentId;

    /**
     * 员工类型(企业管理员:0、 伙伴:1、 团队长:2、 副总:3)
     */
    private String userType;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 电话号码
     */
    private String phoneNumber;

}
