package com.zerody.user.dto;

import com.zerody.user.domain.SysUserInfo;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author PengQiang
 * @ClassName setSysUserInfoDto
 * @DateTime 2020/12/22_15:41
 * @Deacription TODO
 */
@Data
public class SetSysUserInfoDto extends SysUserInfo {

    /**
     * 企业ID
     */
    @NotEmpty(message = "请选择企业！")
    private String companyId;

    /**
     * 员工id
     */
    private String staffId;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 岗位id
     */
    private String positionId;

    /**
     * 部门id
     */
    private String departId;

    /**
     * 员工状态
     */
    private Integer status;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 员工评价
     */
    private String evaluate;

    /**
     * 员工简历url
     */
    private String resumeUrl;

    /** 离职原因 */
    private String leaveReason;


    /**
     * 荣耀
     */
    private List<StaffHistoryDto> staffHistoryHonor;
    /**
     * 惩罚
     */
    private List<StaffHistoryDto> staffHistoryPunishment;
    /**
     * 关系
     */
    private List<SysStaffRelationDto> relations;


}
