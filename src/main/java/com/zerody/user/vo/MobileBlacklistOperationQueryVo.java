package com.zerody.user.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author : xufan
 * @create 2023/3/13 14:38
 */
@Data
public class MobileBlacklistOperationQueryVo {

    /** 内控操作记录id */
    private String id;

    /** 内控名单id */
    private String blacklistId;

    /** 加入内控的用户id */
    private String blackUserId;

    /** 加入内控的用户姓名 */
    private String blackName;

    /** 加入内控的手机号 */
    private String mobile;

    /** 加入内控的身份证 */
    private String identityCard;

    /** 加入内控日期 */
    private Date blackTime;

    /** 内控公司id */
    private String blackCompanyId;

    /** 内控公司名称 */
    private String blackCompanyName;

    /** 内控部门id */
    private String blackDeptId;

    /** 内控部门名称 */
    private String blackDeptName;

    /** 加入内控的原因 */
    private String blackReason;

    /** 是否存在 0:否；1是**/
    private Integer isBlack;
}
