package com.zerody.user.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author : xufan
 * @create 2024/2/26 15:14
 */
@Data
public class UserOpinionAssistantRefDto {

    /** 用户id */
    private String userId;

    /** 用户名称 */
    private String userName;

    /** 协助人id */
    private List<String> assistantUserIds;

    /** 反馈意见id */
    private List<String> opinionIds;

    /** 协助类型 ( 1 自动分配 0 手动分配) */
    private Integer type;
}
