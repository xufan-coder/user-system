package com.zerody.user.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author : xufan
 * @create 2024/2/29 10:52
 */
@Data
public class UserOpinionAssistantRef {

    /** id */
    private String id;

    /** boss账号id */
    private String ceoUserId;

    /** boss名称 */
    private String ceoUserName;

    /** 协助人id */
    private String assistantUserId;

    /** 协助人名称 */
    private String assistantUserName;

    /** 创建时间 */
    private Date createTime;
}
