package com.zerody.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author kuang
 */
@Data
public class UserInductionGroupRecordVo {


    private String id;

    /**申请名称*/
    private String approveName;

    /**伙伴电话*/
    private String mobile;

    /**签约部门*/
    private String signDept;

    /**签约角色*/
    private String signRole;

    /**申请时间*/
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date approveTime;

    /**申请状态*/
    private String approveState;

    private String processKey;

    private String processId;

}
