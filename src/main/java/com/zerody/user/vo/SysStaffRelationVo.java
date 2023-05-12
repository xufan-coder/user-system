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
     * 部门ID
     */
    private String departId;
    /**
     * 公司ID
     */
    private String companyId;
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
     * 关系岗位
     */
    private String relationPositionName;
    /**
     * 关系企业名称
     */
    private String relationCompanyName;
    /**
     * 关系公司ID
     */
    private String relationCompanyId;
    /**
     * 关系部门名称
     */
    private String relationDepartName;
    /**
     * 关系部门ID
     */
    private String relationDepartId;

    /**
     * 离职类型id
     */
    private String leaveType;

}
