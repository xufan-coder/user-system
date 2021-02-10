package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.common.bean.PageInfo;
import lombok.Data;

/**
 * @author PengQiang
 * @ClassName SysStaffInfoPageDto
 * @DateTime 2020/12/18_8:42
 * @Deacription TODO
 */
@Data
public class SysStaffInfoPageDto extends PageQueryDto {

    /**
     *    企业ID
     */
    private String companyId;


    /**
     *    角色ID
     */
    private String roleId;

    /**
     *    部门id
     */
    private String departId;

    /**
     *    岗位id
     */
    private String jobId;

    /**
    *    员工姓名
    */
    private String staffName;

    /**
    *    员工联系电话
    */
    private String phoneNumber;

    /** 是否模糊查询(模糊查询 获取下级部门的员工(包括本级)) */
    private boolean showSubordinates;

}
