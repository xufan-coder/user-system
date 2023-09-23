package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import lombok.Data;

/**
 * @Author : xufan
 * @create 2023/9/21 10:50
 */
@Data
public class StaffBlacklistApproverPageDto extends PageQueryDto {

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 审批状态 APPROVAL审批中,FAIL拒绝,SUCCESS已通过
     */
    private String approveState;

    /**
     * 提交人姓名
     */
    private String submitUserName;
    /**
     * 黑名单类型：1企业内部 2外部人员
     */
    private Integer type;
    /**
     * 黑名单姓名
     */
    private String userName;

    /**
     * 拉黑企业id
     */
    private String companyId;

    /**
     * 部门id
     */
    private String deptId;
}
