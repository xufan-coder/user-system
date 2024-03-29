package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.user.constant.CheckCompare;
import com.zerody.user.domain.base.BaseModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * 员工信息表
 *
 * @param
 * @author PengQiang
 * @description DELL
 * @date 2021/1/19 14:55
 * @return
 */
@Data
public class SysStaffInfo extends BaseModel {

    /**
     * 企业id
     **/
    private String compId;

    /**
     * 头像
     */
    @CheckCompare(value = "avatar",name = "头像")
    private String avatar;

    /**
     * 用户id
     **/
    private String userId;

    /**
     * 用户姓名
     **/
    private String userName;

    /**
     * 工作地点
     **/
    private String workPlace;

    /**
     * 工号
     **/
    private String jobNumber;

    /**
     *  入职时间
     **/
    @CheckCompare(value = "dateJoin", name = "签约时间")
    private Date dateJoin;

    /**
     * 转正时间
     **/
    private Date conversionDate;

    /**
     * 离职时间
     **/
    @CheckCompare(value = "dateLeft",name = "离职时间")
    private Date dateLeft;
    /**
     * 员工评价
     */
    @CheckCompare(value = "evaluate",name = "员工评价")
    private String evaluate;

    /**
     * 员工简历url
     */
    private String resumeUrl;

    /**
     * 是否删除
     */
    private Integer deleted;

    /**
     * 离职类型
     */
    private String leaveType;

    /** 离职原因 */
    @CheckCompare(value = "leaveReason",name = "离职原因")
    private String leaveReason;


    /** 推荐人id */
    private String recommendId;

    /** 推荐类型 0:公司社招,1员工介绍 */
    @NotEmpty(message = "请选择推荐人")
    @CheckCompare(value = "recommendType",name = "推荐类型")
    private Integer recommendType;

    /** 积分 */
    @CheckCompare(value = "integral",name = "积分")
    private Integer integral;

    /** 密码 */
    private String password;

    /**
     * 工作年限
     */
    @CheckCompare(value = "workingYears",name = "工作年限")
    private Integer workingYears;

    /** 用户类型 企业管理员:0、伙伴：1、团队长：2、副总：3 */
    private Integer userType;

    /**
     * 是否钻石会员（0-否 1-是）
     */
    @CheckCompare(value = "isDiamondMember",name = "是否钻石会员")
    private Integer isDiamondMember;

    /**
     *  合约结束时间
     **/
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date expireTime;

    /** 是否同步顾问 (0.否 1.是) */
    private Integer isSyncAdvisor;

    /** 是否唐叁藏顾问 (0.否 1.是) */
    private Integer isTszAdvisor;

}
