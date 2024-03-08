package com.zerody.user.domain;

import lombok.Data;

import java.util.Date;

/**
 * @Author : xufan
 * @create 2024/2/29 10:52
 */
@Data
public class UserOpinionAssistantRef {

    /** id */
    private String id;

    /** 用户id */
    private String userId;

    /** 用户名称 */
    private String userName;

    /** 协助人id */
    private String assistantUserId;

    /** 协助人名称 */
    private String assistantUserName;

    /** 创建时间 */
    private Date createTime;
}
