package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.zerody.user.domain.base.BaseModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
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
    private Date dateJoin;

    /**
     * 转正时间
     **/
    private Date conversionDate;

    /**
     * 离职时间
     **/
    private Date dateLeft;
    /**
     * 员工评价
     */
    private String evaluate;

    /**
     * 员工简历url
     */
    private String resumeUrl;

    /**
     * 是否删除
     */
    private Integer deleted;

    /** 离职原因 */
    private String leaveReason;


    /** 推荐人id */
    private String recommendId;

    /** 推荐类型 0:公司社招,1员工介绍 */
    @NotEmpty(message = "请选择推荐人")
    private Integer recommendType;

    /** 积分 */
    private Integer integral;

    /** 密码 */
    private String password;

    /**
     * 工作年限
     */
    private Integer workingYears;

}