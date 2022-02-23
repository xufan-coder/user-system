package com.zerody.user.vo;

import lombok.Data;

/**
 * 推荐人信息Vo
 * @author PengQiang
 * @ClassName RecommendInfoVo
 * @DateTime 2021/11/11_9:56
 * @Deacription TODO
 */
@Data
public class RecommendInfoVo {

    /** 员工id(推荐人员工id) */
    private String staffId;

    /** 用户id(推荐人用户id) */
    private String userId;

    /** 用户名称(推荐人名称) */
    private String userName;

    /** 部门id */
    private String departId;

    /** 部门名称 */
    private String departName;

    /** 企业id */
    private String companyId;

    /** 企业名称 */
    private String companyName;

    /** 推荐人id */
    private String recommendId;

    /** 推荐人类型*/
    private Integer recommendType;
    /**岗位名称*/
    private String positionName;
}
