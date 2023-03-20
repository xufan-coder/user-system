package com.zerody.user.domain;

import lombok.Data;

import java.util.Date;

/**
 * 伙伴入职申请记录(跨公司)
 *
 * @author kuang
 * @date 2022/11/30 9:56
 */
@Data
public class UserInductionSplitRecord {


    private String id;

    /**离职伙伴id*/
    private String leaveUserId;

    /**离职伙伴名称*/
    private String leaveUserName;

    /**身份证号*/
    private String certificateCard;

    /**手机号码*/
    private String mobile;

    private String oldCompanyId;

    private String oldCompanyName;

    /**所属部门*/
    private String oldDeptName;

    private String oldDeptId;

    /**所属角色*/
    private String oldRoleName;

    private String oldRoleId;

    /**离职时间*/
    private Date leaveTime;

    /**离职原因*/
    private String leaveReason;

    /**签约时间*/
    private Date signTime;

    /**签约部门*/
    private String signDept;

    /**签约部门id*/
    private String signDeptId;

    /**签约角色*/
    private String signRole;

    /**签约角色*/
    private String signRoleId;

    /**签约原因*/
    private String signReason;

    /**企业id*/
    private String signCompanyId;

    private String signCompanyName;

    /**申请状态*/
    private String approveState;

    /**流程id*/
    private String processId;

    /**流程key*/
    private String processKey;

    /**伙伴id*/
    private String userId;

    /**
     * 是否删除(0存在 1删除)
     */
    private Integer deleted;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateTime;
}