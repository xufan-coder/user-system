package com.zerody.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author kuang
 */
@Data
public class LeaveUserInfoVo {

    /**伙伴id*/
    private String userId;

    /**伙伴名称*/
    private String userName;

    /**伙伴手机号*/
    private String mobile;

    /**身份证号*/
    private String idCard;

    /**角色名称*/
    private String roleName;

    /**离职时间*/
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date leaveTime;

    /**离职原因*/
    private String leaveRemark;

    /**部门名称*/
    private String departName;
}
