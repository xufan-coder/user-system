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
     * 用户ID
     */
    private String userId;
    /**
     * 负责人名称
     */
    private String userName;


}
