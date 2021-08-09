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

    /** 负责人id */
    private String staffId;

    private String key;


}
