package com.zerody.user.vo;

import lombok.Data;

/**
 * @author zhangpingping
 * @date 2021年09月10日 9:50
 */
@Data
public class SysStaffRelationVo {
    private String id;
    /**
     * 员工ID
     */
    private String staffId;
    /**
     * 员工名称
     */
    private String userName;

    /**
     * 部门ID
     */
    private String departId;
    /**
     * 描述
     */
    private String desc;
    /**
     * 关系员工ID
     */
    private String relationStaffId;
    /**
     * 关系员工名称
     */
    private String relationStaffName;
    /**
     * 关系员工userID
     */
    private String relationUserId;
    /**
     * 员工userID
     */
    private String staffUserId;
    /**
     * 岗位
     */
    private String positionName;
    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 部门名称
     */
    private String departName;

    /**
     * 岗位
     */
    private String relationPositionName;
    /**
     * 企业名称
     */
    private String relationCompanyName;
    /**
     * 部门名称
     */
    private String relationDepartName;



}
