package com.zerody.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.common.enums.user.StaffBlacklistApproveState;
import com.zerody.common.utils.DataUtil;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author PengQiang
 * @ClassName FrameworkBlacListQueryPageVo
 * @DateTime 2021/8/4_11:25
 * @Deacription TODO
 */
@Data
public class FrameworkBlacListQueryPageVo {

    /** userId */
    private String id;

    /** 员工id */
    private String staffId;

    /** 员工/用户 名称 */
    private String userName;

    /** 企业名称 */
    private String companyName;

    /** 岗位名称 */
    private String jobName;

    /** 角色名称 */
    private String roleName;

    /** 申请人名称 */
    private String  applicantName;

    /** 申请人角色 */
    private String  applicantRoleName;

    /** 原因 */
    private String reason;

    /** 员工照片 */
    private String avatar;

    /** 员工手机号码 */
    private String mobile;

    /** 部门名称 */
    private String departName;

    /** 创建时间 */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date approvalTime;

    /** 状态 */
    private String state;

    /** 流程id */
    private String processId;

    /** 流程key */
    private String processKey;

    /** 黑名单类型：1企业内部 2外部人员*/
    private Integer type;

    /** 身份证号码 */
    private String identityCard;

    /** 身份证号码 2*/
    private String identityCard2;

    private List<String> images;

    public String getStateSting() {
        if (StringUtils.isEmpty(this.state)) {
            return null;
        }
        return StaffBlacklistApproveState.getTextByCode(this.state);
    }
    public String getIdentityCard() {
        if (DataUtil.isEmpty(this.identityCard)) {
            return identityCard2;
        }
        return this.identityCard;
    }
}
