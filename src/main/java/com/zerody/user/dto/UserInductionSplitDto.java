package com.zerody.user.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author kuang
 */
@Data
public class UserInductionSplitDto {

    private String leaveUserId;

    /**身份证号*/
    private String certificateCard;

    /**签约时间*/
    private Date signTime;

    /**签约部门id*/
    private String signDeptId;

    private String signDept;

    /**签约角色*/
    private String signRoleId;

    private String signRole;

    /**签约原因*/
    private String signReason;

    /**企业id*/
    private String signCompanyId;

    private String signCompanyName;

    /**
     * 创建人
     */
    private String userId;

    /**流程id*/
    private String processId;

    /**流程key*/
    private String processKey;
}
