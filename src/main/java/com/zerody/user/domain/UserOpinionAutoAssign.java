package com.zerody.user.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author : xufan
 * @create 2024/2/29 16:11
 */
@Data
public class UserOpinionAutoAssign {

    private String id;

    /** 直接回复人id */
    private String userId;

    /** 是否开启自动分配 （0 否 1是) */
    private Integer autoAssign;

    /** 创建时间 */
    private Date createTime;
}
