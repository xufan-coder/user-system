package com.zerody.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author kuang
 */
@Data
public class UserInductionVerificationVo {

    /**用户名称*/
    private String leaveUserName;

    /**用户id*/
    private String leaveUserId;

    /**身份证号*/
    private String certificateCard;

    /**脱敏身份证号*/
    private String certificateCardHide;

    /**离职原因*/
    private String leaveReason;

    /**离职时间*/

    private String leaveTime;

    /**手机号*/
    private String mobile;

    /**脱敏手机号码*/
    private String mobileHide;

    /**所属企业*/
    private String oldCompanyName;

    /**部门*/
    private String oldDeptName;

    /**角色*/
    private String oldRoleName;

    /**消息提示内容*/
    private String message;

    /**校验状态 0-校验通过  1-存在同公司数据  2-存在跨企业数据*/
    private Integer verificationState;

}
