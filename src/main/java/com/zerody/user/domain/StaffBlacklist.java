package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

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

    /** 员工id */
    private String mobile;

    /** 状态 */
    private String state;

    /** 创建时间 */
    private Date createTime;

    /** 审批时间 */
    private Date approvalTime;

    /** 拉黑原因 */
    private String reason;

    /**  */
    private String staffId;

    /**  */
    private String applicantStaffId;
    /** 企业id*/
    private String companyId;
}