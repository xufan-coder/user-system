package com.zerody.user.vo;

import lombok.Data;

/**
 * @author zhangpingping
 * @date 2021年09月24日 17:34
 */
@Data
public class DepartInfoVo {
    /**
     * 员工统计
     */
    private Integer staffCountId;
    /**
     * 部门名称
     */
    private String departName;
    /**
     * 部门ID
     */
    private String departmentId;
    /**
     * 企业ID
     */
    private String copmId;
    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 负责人名称
     */
    private String userName;
    /**
     * 员工类型(企业管理员:0、 伙伴:1、 团队长:2、 副总:3)
     */
    private String userType;


}
