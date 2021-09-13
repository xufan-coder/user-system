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
     * 部门名称
     */
    private String departName;
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


}
