package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.common.bean.PageInfo;
import com.zerody.user.dto.bean.UserPositionPageParam;
import lombok.Data;

/**
 * @author PengQiang
 * @ClassName SysStaffInfoPageDto
 * @DateTime 2020/12/18_8:42
 * @Deacription TODO
 */
@Data
public class SysStaffInfoPageDto extends UserPositionPageParam {

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
     *    部门id 2
     */
    private String stffDepartId;

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

    private String queryType;

    private Integer status;

    /** 是否黑名单 */
    private Integer isBlock;

    private String keyword;

    /** 用户类型 企业管理员:0、伙伴：1、团队长：2、副总：3*/
    private Integer userType;

    /**
     * 是否钻石会员（0-否 1-是）
     */
    private Integer isDiamondMember;

    /**
    *   培训班次
    */
    private String trainNo;


}
