package com.zerody.user.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author : xufan
 * @create 2024/2/26 15:14
 */
@Data
public class UserOpinionAssistantRefDto {

    /** boss账号id */
    public String ceoUserId;

    /** boss名称 */
    public String ceoUserName;

    /** 协助人id */
    public List<String> assistantUserIds;
}
