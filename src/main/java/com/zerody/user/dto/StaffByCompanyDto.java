package com.zerody.user.dto;

import lombok.Data;

/**
 * @author zhangpingping
 * @date 2021年09月25日 10:48
 */
@Data
public class StaffByCompanyDto {
    /**
     * 企业ID
     */
    private String companyId;
    /**
     * 部门ID
     */
    private String departmentId;
    /**
     * 是否部门（0 是部门 1团队）
     */
    private Integer isDepartment;
}
