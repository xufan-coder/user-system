package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 *
 *  员工黑名单
 * @author               PengQiang
 * @description          DELL
 * @date                 2021/8/3 19:16
 */
@Data
public class StaffBlacklist {
    /** 黑名单id */
    private String id;

    /** 员工手机号 */
    private String mobile;

    /** 状态  BLOCK:拉黑， RELIEVE:解除*/
    private String state;

    /** 创建时间 */
    private Date createTime;

    /** 审批时间 */
    private Date approvalTime;

    /** 拉黑原因 */
    private String reason;

    /** 被拉黑用户id */
    private String userId;

    /** 提交者userId */
    private String  submitUserId;

    /** 企业id*/
    private String companyId;

    /** 流程id */
    private String processId;

    /** 流程key */
    private String processKey;

    private Date updateTime;

    /** 提交人姓名 */
    private String submitUserName;

    /** 黑名单类型：1企业内部 2外部人员 */

    private Integer type;

    /** 是否已审批/能否解除0未审批 -1 审批中，1已审批 */
    private Integer isApprove;

    /** 黑名单姓名 */
    private String userName;

    /** 身份证号码 */
    private String identityCard;

    /** 解除id */
    private String relieveId;
    /** 解除流程key */
    private String relieveKey;

    /** 视频 */
    private String video;

    @TableField(exist = false)
    private String companyName;

    @TableField(exist = false)
    private List<String> images;
}
