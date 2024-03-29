package com.zerody.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author kuang
 */
@Data
public class UserInductionRecordVo {


    private String id;

    /**申请名称*/
    private String approveName;

    /**申请时间*/
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date approveTime;

    /**申请状态*/
    private String approveState;

    private String processKey;

    private String processId;

}
