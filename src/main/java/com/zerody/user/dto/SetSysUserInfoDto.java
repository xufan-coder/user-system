package com.zerody.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.user.domain.CommonFile;
import com.zerody.user.domain.FamilyMember;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.domain.UserResume;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
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
    @NotBlank(message = "请选择企业！")
    private String companyId;

    private String companyName;

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
     * 员工状态(状态  0合约中、 1已解约、 3合作中')
     */
    private Integer status;

    /**
     * 头像
     */
    private String avatar;
    /**
     * 头像
     */
    private String staffAvatar;


    /**
     * 员工评价
     */
    private String evaluate;

    /**
     * 员工简历url
     */
    private String resumeUrl;

    /**
     * 离职类型
     */
    private String leaveType;

    /**
     * 离职原因
     */
    private String leaveReason;

    /**
     * 入职时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date dateJoin;

    /**
     * 工作年限
     */
    private Integer workingYears;

    /**
     * 荣耀
     */
    private List<StaffHistoryDto> staffHistoryHonor;

    /**
     * 惩罚
     */
    private List<StaffHistoryDto> staffHistoryPunishment;

    /**
     * 企业内部关系信息
     */
    private List<SysStaffRelationDto> staffRelationDtoList;

    /**
     * 家庭成员
     */
    private List<FamilyMember> familyMembers;

    /**
     * 推荐人id
     */
    private String recommendId;

    /**
     * 推荐类型 0:公司社招,1伙伴介绍
     */
    @NotNull(message = "请选择推荐类型")
    private Integer recommendType;

    /**
     * 积分
     */
    private Integer integral;

    /**
     * 离职时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date dateLeft;

    /**
     * 是否钻石会员（0-否 1-是）
     */
    private Integer isDiamondMember;

    /**
     * 企业id  多个
     */
    private List<String> companyIds;

    /**
     * 个人履历
     */
    private List<UserResume> userResumes;

    /**
     * 身份证照片国徽面(正面)
     */
    private String idCardFront;

    /**
     * 身份证照片人像面(反面)
     */
    private String idCardReverse;

    /**
     * 合规承诺书
     */
    private List<String> complianceCommitments;

    /**
     * 学历证书
     */
    private List<String> diplomas;

    /**
     * 合作申请表
     */
    private List<CommonFile> cooperationFiles;

    /**
     * 合作申请图片
     */
    private List<String> cooperationImages;

    /**
     * 终端
     */
    private String terminals;

    /** 是否预备高管 0表示否 1表示是 2表示退学*/
    private Integer isPrepareExecutive;

    private String mobile;

    /**
     *  合约结束时间
     **/
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date expireTime;

    /** 是否唐叁藏顾问 (0.否 1.是) */
    private Integer isTszAdvisor;

    private Boolean filter = true;

}
