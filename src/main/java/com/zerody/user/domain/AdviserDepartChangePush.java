package com.zerody.user.domain;

import lombok.Data;

import java.util.Date;

/**
 * 伙伴变更部门推送更新顾问
 * @author  DaBai
 * @date  2024/1/18 10:43
 */

@Data
public class AdviserDepartChangePush {
    private String id;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 推送时间
    */
    private Date sendTime;

    /**
    * 部门Id
    */
    private String deptId;

    /**
    * crm用户ID
    */
    private String userId;

    /**
    * 状态(1已推送，0未推送）
    */
    private Integer state;

    /**
    * 重试次数
    */
    private Integer resend;

    /**
    * 删除
    */
    private Integer deleted;

    /**
    * 修改时间
    */
    private Date updateTime;
}
