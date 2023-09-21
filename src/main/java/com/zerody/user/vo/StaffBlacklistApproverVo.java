package com.zerody.user.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

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
     * 企业id
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
     * 岗位id
     */
    private String postId;
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
}
