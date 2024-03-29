package com.zerody.user.dto;

import lombok.Data;

import java.util.List;

/**
 * @author zhangpingping
 * @date 2021年09月09日 17:18
 */
@Data
public class SysStaffRelationDto {

    /**
     * 成员id
     */
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
     * 员工UserId
     */
    private String staffUserId;
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

    private String describe;


    /**
     * 关系员工ID
     */
    private String relationStaffId;
    /**
     * 关系员工名称
     */
    private String relationStaffName;
    /**
     * 关系员工UserId
     */
    private String relationUserId;

    /**
     * 查询多个ID
     */
    private List<String> ids;
}
