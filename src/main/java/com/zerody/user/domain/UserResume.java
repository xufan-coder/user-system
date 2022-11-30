package com.zerody.user.domain;

import lombok.Data;

import java.util.Date;
/**
 * @author  DaBai
 * @date  2022/11/28 14:05
 */
@Data
public class UserResume {

    private String id;

    private String userId;

    /**
    *   任职企业
    */
    private String companyName;

    /**
    *   任职时间
    */
    private String workDuration;

    /**
    *   任职岗位
    */
    private String post;

    /**
    *   主要工作描述
    */
    private String jobDescription;

    private Date createTime;


}
