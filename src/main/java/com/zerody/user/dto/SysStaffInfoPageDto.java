package com.zerody.user.dto;

import com.zerody.common.bean.PageInfo;
import lombok.Data;

/**
 * @author PengQiang
 * @ClassName SysStaffInfoPageDto
 * @DateTime 2020/12/18_8:42
 * @Deacription TODO
 */
@Data
public class SysStaffInfoPageDto extends PageInfo {

    private String companyId;

    //角色id用于查询角色下的工
    private String roleId;

    //部门id
    private String departId;

    //岗位id
    private String jobId;

    //员工姓名
    private String staffName;

    //员工联系电话
    private String phoneNumber;

    //员工状态
    private Integer status;

    //角色关键字搜索(支持模糊查询)
    private String roleKey;

    //部门关键字搜索(支持模糊查询)
    private String departmentKey;

    //岗位关键字搜索(支持模糊查循)
    private String jobKey;
}
