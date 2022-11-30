package com.zerody.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author kuang
 */
@Data
public class UserInductionRecordInfoVo {

    private String id;

    /**离职伙伴id*/
    private String leaveUserId;

    /**签约时间*/
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signTime;

    private String companyId;

    /**部门id*/
    private String departId;

    /**部门名称*/
    private String departName;

    /**角色id*/
    private String roleId;

    /**角色名称*/
    private String roleName;

    /**签约原因*/
    private String  signReason;

    /**流程id*/
    private String processId;

    /**流程key*/
    private String processKey;

    /**离职伙伴信息*/
    private LeaveUserInfoVo leaveInfo;
}
