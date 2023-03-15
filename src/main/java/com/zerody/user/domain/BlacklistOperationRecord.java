package com.zerody.user.domain;


import lombok.Data;

import java.util.Date;

@Data
public class BlacklistOperationRecord {

    /** 内控操作记录id */
    private String id;

    /** 内控名单id */
    private String blacklistId;

    /** 操作类型（0：查询，1：编辑）*/
    private Integer type;

    /** 操作人id */
    private String createBy;

    /** 操作人姓名 */
    private String createName;

    /** 操作时间 */
    private Date createTime;

    /** 操作备注 */
    private String remarks;

    /** 操作人所属公司id */
    private String operateCompanyId;

    /** 操作人所属公司名称 */
    private String operateCompanyName;

    /** 操作人所属部门id */
    private String operateDeptId;

    /** 操作人所属部门名称 */
    private String operateDeptName;

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


}
