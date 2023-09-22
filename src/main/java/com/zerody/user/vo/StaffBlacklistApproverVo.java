package com.zerody.user.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.common.enums.user.StaffBlacklistApproveState;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.util.CommonUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

/**
 * @Author : xufan
 * @create 2023/9/21 11:06
 */
@Data
public class StaffBlacklistApproverVo {

    /**
     * 申请id
     */
    @TableId
    private String id;

    /**
     * 伙伴id
     */
    private String userId;

    /**
     * 伙伴姓名
     */
    private String userName;

    /**
     * 企业id
     */
    private String companyId;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 部门id
     */
    private String deptId;
    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 身份证号码
     */
    private String identityCard;

    /**
     * 角色id
     */
    private String roleId;
    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 审批时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date approveTime;

    /**
     * 黑名单类型：1企业内部 2外部人员
     */
    private Integer type;

    /**
     * 申请加入人姓名
     */
    private String submitUserName;

    /**
     * 拉黑原因
     */
    private String reason;

    public String getIdentityCard() {
        String idCard = this.identityCard;
        if (DataUtil.isEmpty(idCard)) {
                return  "";
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
