package com.zerody.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.common.enums.customer.EducationBackgroundEnum;
import com.zerody.user.domain.CommonFile;
import com.zerody.user.domain.FamilyMember;
import com.zerody.user.domain.UserResume;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 伙伴信息类
 *
 * @author PengQiang
 * @ClassName SysUserInfoVo
 * @DateTime 2020/12/18_15:19
 * @Deacription TODO
 */
@Data
public class SysUserInfoVo {

    /**
     * userId
     */
    private String id;
    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 企业ID
     */
    private String companyId;

    /**
     * 员工id
     */
    private String staffId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 性别(0:男，1:女，3:未知)
     */
    private Integer gender;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 敏感电话号码
     */
    private String sensitivePhone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像(相对路径)
     */
    private String avatar;

    /**
     * 出生日期
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date birthday;


    /**
     * 脱敏身份证 证件号码
     */
    private String certificateCard;

    /**
     * 身份证 证件号码
     */
    private String identityCardNum;

    /**
     * 身份证地址
     */
    private String certificateCardAddress;

    /**
     * 省市区
     */
    private String provCityDistrict;

    /**
     * 联系地址
     */
    private String contactAddress;


    /**
     * 注册时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registerTime;

    /**
     * 名族
     */
    private String nation;

    /**
     * 籍贯
     */
    private String ancestral;

    /**
     * 备注
     */
    private String description;

    /**
     * 状态: 用户: 1.enable,0. disable ,-1 deleted；
     * 员工:0.生效、1.离职、2.删除、3.合作
     */
    private Integer status;

    /**
     * 最高学历(枚举)
     * PRIMARY_SCHOOL("小学"), JUNIOR_HIGH("初中"), TECHNICAL_SECONDARY("中专"), SENIOR_HIGH("高中"),
     * JUNIOR_COLLEGE("大专"), REGULAR_COLLEGE("本科"), MASTER("硕士"), DOCTOR("博士");
     **/
    private String highestEducation;

    /**
     * 毕业院校
     */
    private String graduatedFrom;

    /**
     * 所学专业
     */
    private String major;

    /**
     * 婚姻状态
     */
    private Integer maritalStatus;

    /**
     * crmOpenId
     **/
    private String crmOpenId;

    /**
     * scrmOpenId
     **/
    private String scrmOpenId;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 部门名称
     */
    private String departName;

    /**
     * 岗位名称
     */
    private String positionName;

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 部门ID
     */
    private String departId;

    /**
     * 岗位ID
     */
    private String positionId;

    /**
     * 紧急联系姓名
     */
    private String urgentName;

    /**
     * 紧急联系人关系
     */
    private String urgentRelation;

    /**
     * 紧急联系人电话
     */
    private String urgentPhone;

    /**
     * 家庭成员姓名
     */
    private String familyName;

    /**
     * 家庭成员关系
     */
    private String familyRelation;

    /**
     * 家庭成员电话
     */
    private String familyPhone;

    /**
     * 家庭成员职业
     */
    private String familyJob;

    /**
     * 家庭成员地址
     */
    private String familyAddr;

    /**
     * 上级名称
     */
    private String superiorName;

    /**
     * 创建时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 员工评价
     */
    private String evaluate;

    /**
     * 员工简历url
     */
    private String resumeUrl;

    /**
     * 荣耀记录
     */
    private List<StaffHistoryVo> staffHistoryHonor;

    /**
     * 惩罚记录
     */
    private List<StaffHistoryVo> staffHistoryPunishment;

    /**
     * 离职原因
     */
    private String leaveReason;

    /**
     * 企业内部关系信息
     */
    private List<SysStaffRelationVo> staffRelationDtoList;

    /**
     * 是否为企业管理员(true：是；false：否)
     */
    private Boolean isCompanyAdmin;

    /**
     * 是否为部门管理员(true：是；false：否)
     */
    private Boolean isDepartAdmin;

    /**
     * 是否被拉黑(true：是；false：否)
     */
    private Boolean isBlock;

    /** 推荐人id */
    private String recommendId;

    /** 推荐类型 0:公司社招,1伙伴介绍 */
    private Integer recommendType;

    /** 积分 */
    private Integer integral;

    /** 一级推荐人 */
    private RecommendInfoVo recommendInfo;

    /** 二级推荐人 */
    private RecommendInfoVo recommendSecond;
    /**
     * 工作年限
     */
    private Integer workingYears;

    /**
     * 家庭成员信息类
     */
    private List<FamilyMember> familyMembers;

    /**
     * 入职时间
     **/
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date dateJoin;

    /**
     * 最高学历 枚举转换
     *
     * @return
     */
    public String getHighestEducationString() {
        return EducationBackgroundEnum.getTextByCode(this.highestEducation);
    }

    /**
     * im状态
     */
    private String imState;

    /**
     * im状态名称
     */
    private String imStateName;


    private String userAvatar;

    /** 离职时间 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date dateLeft;

    /**
     * 是否钻石会员（0-否 1-是）
     */
    private Integer isDiamondMember;

    /**
     * 个人履历
     */
    private List<UserResume> userResumes;

    /** 身份证照片国徽面(正面) */
    private String idCardFront;

    /** 身份证照片人像面(反面) */
    private String idCardReverse;

    /** 合规承诺书 */
    private List<String> complianceCommitments;

    /** 学历证书 */
    private List<String> diplomas;

    /** 合作申请表 */
    private List<CommonFile> cooperationFiles;

    /**
     *    培训班次
     */
    private String trainNo;
}
