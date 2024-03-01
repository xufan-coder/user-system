package com.zerody.user.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author : xufan
 * @create 2024/2/29 16:40
 */
@Data
public class UserOpinionAutoAssignDto {
    /** 用户id */
    private String userId;


    /** 是否开启自动分配 （0 否 1是) */
    private Integer autoAssign;
}
