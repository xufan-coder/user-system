package com.zerody.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 离职明细
 *
 * @author : chenKeFeng
 * @date : 2023/5/3 14:14
 */
@Data
public class DepartureDetailsVo {

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 部门名称
     */
    private String departName;

    /**
     * 离职原因
     */
    private String leaveReason;

    /**
     * 入职时间
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date dateJoin;

    /**
     * 离职时间
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date dateLeft;

    /**
     * 员工类型(企业管理员:0、 伙伴:1、 团队长:2、 副总:3)
     */
    private String userType;

    /**
     * 企业ID
     */
    private String compId;

    /**
     * user id
     */
    private String id;

    /**
     * 员工ID
     */
    private String staffId;

}
