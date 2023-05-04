package com.zerody.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class PositionRecordListVo {
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 身份证
     */
    private String certificateCard;
    /**
     * 任职时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date positionTime;
    /**
     * 离职时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date quitTime;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 离职类型id
     */
    private String leaveType;
    /**
     * 离职类型
     */
    private String leaveTypeText;
    /**
     * 离职原因
     */
    private String quitReason;
}
