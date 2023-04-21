package com.zerody.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.user.domain.CommonFile;
import com.zerody.user.domain.UserResume;

import java.util.Date;
import java.util.List;

/**
 * @Author : xufan
 * @create 2023/3/29 11:22
 */
public class SysUserInfo2 {

    /**
     * userId
     */
    private String id;

    /**
     * 员工id
     */
    private String staffId;


    /**
     * 昵称
     */
    private String nickname;


    /**
     * 是否被拉黑(true：是；false：否)
     */
    private Boolean isBlock;

    /**
     * im状态
     */
    private String imState;

    /**
     * 头像(相对路径)
     */
    private String avatar;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 伙伴手机号
     */
    private String phoneNumber;

    /**
     * 身份证 证件号码
     */
    private String identityCardNum;


    /** 身份证照片国徽面(正面) */
    private String idCardFront;

    /** 身份证照片人像面(反面) */
    private String idCardReverse;

    /**
     * 性别(0:男，1:女，3:未知)
     */
    private Integer gender;

    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 企业ID
     */
    private String companyId;

    /**
     * 部门名称
     */
    private String departName;

    /**
     * 部门ID
     */
    private String departId;

    /**
     * 岗位ID
     */
    private String positionId;


    /**
     * 岗位名称
     */
    private String positionName;

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 入职时间
     **/
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date dateJoin;

    /**
     *    培训班次
     */
    private String trainNo;

    //签约年限

    /**
     * 状态: 用户: 1.enable,0. disable ,-1 deleted；
     * 员工:0.生效、1.离职、2.删除、3.合作
     */
    private Integer status;

    /**
     *    账号状态
     */
    private Integer useState;

    /**
     * 企业内部关系信息
     */
    private List<SysStaffRelationVo> staffRelationDtoList;

    /** 推荐类型 0:公司社招,1伙伴介绍 */
    private Integer recommendType;

    /**
     * 是否钻石会员（0-否 1-是）
     */
    private Integer isDiamondMember;

    /** 合规承诺书 */
    private List<String> complianceCommitments;

    /**
     * 籍贯
     */
    private String ancestral;

    /**
     * 名族
     */
    private String nation;


    /**
     * 婚姻状态
     */
    private Integer maritalStatus;

    /**
     * 出生日期
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 身份证地址
     */
    private String certificateCardAddress;

    /**
     * 联系地址
     */
    private String contactAddress;

    /**
     * 邮箱
     */
    private String email;

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
     * 工作年限
     */
    private Integer workingYears;

    /** 学历证书 */
    private List<String> diplomas;

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
     * 个人履历
     */
    private List<UserResume> userResumes;

    /**
     * 员工评价
     */
    private String evaluate;

    /**
     * 荣耀记录
     */
    private List<StaffHistoryVo> staffHistoryHonor;

    /**
     * 惩罚记录
     */
    private List<StaffHistoryVo> staffHistoryPunishment;

    /** 合作申请表 */
    private List<CommonFile> cooperationFiles;

    /**
     * 合作申请图
     */
    private List<String> cooperationImages;

    public static void main(String[] args) {
        System.out.println(SysUserInfo2.class.getDeclaredFields().length);
    }
}
