package com.zerody.user.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author kuang
 */
@Data
public class UserInductionSplitDto {

    private String leaveUserId;

    /**签约时间*/
    private Date signTime;

    /**签约部门id*/
    private String signDeptId;

    /**签约角色*/
    private String signRoleId;

    /**签约原因*/
    private String signReason;

    /**企业id*/
    private String signCompanyId;

    /**
     * 创建人
     */
    private String userId;

}
