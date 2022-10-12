package com.zerody.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.common.enums.user.StaffBlacklistApproveState;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.util.CommonUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

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

    private String blackId;
    /** 是否已审批/能否解除0未审批 -1 审批中，1已审批 */
    private String isApprove;
    /** 解除id */
    private String relieveId;
    /** 解除流程key */
    private String relieveKey;
    /** 视频 */
    private String video;

    /** 企业id */
    private String companyId;

    private List<String> images;

    public String getStateSting() {
        if (StringUtils.isEmpty(this.state)) {
            return null;
        }
        return StaffBlacklistApproveState.getTextByCode(this.state);
    }
    public String getIdentityCard() {
        String idCard = this.identityCard;
        if (DataUtil.isEmpty(idCard)) {
            if (StringUtils.isEmpty(this.identityCard2)) {
                return  "";
            }
            idCard = identityCard2;
        }
        return CommonUtils.idEncrypt(idCard, 2, 2);
    }

    public String getMobile() {
        if (StringUtils.isEmpty(this.mobile)) {
            return "";
        }
        return this.mobile.replaceAll("(\\d{3})\\d{4}(\\w{4})", "$1****$2");
    }
}
