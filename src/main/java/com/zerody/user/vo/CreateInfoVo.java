package com.zerody.user.vo;

import lombok.Data;

/**
 * @Author : xufan
 * @create 2023/3/13 15:43
 */

@Data
public class CreateInfoVo {

    /** 操作人所属公司id */
    private String operateCompanyId;

    /** 操作人所属公司名称 */
    private String operateCompanyName;

    /** 操作人所属部门id */
    private String operateDeptId;

    /** 操作人所属部门名称 */
    private String operateDeptName;

}
