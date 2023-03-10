package com.zerody.user.domain;

import com.zerody.user.constant.CheckCompare;
import lombok.Data;

import java.util.Date;

/**
 * 个人履历
 *
 * @author DaBai
 * @date 2022/11/28 14:05
 */
@Data
public class UserResume {

    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 任职企业
     */
    @CheckCompare(value = "companyName", name = "任职企业")
    private String companyName;

    /**
     * 任职时间
     */
    @CheckCompare(value = "workDuration", name = "任职时间")
    private String workDuration;

    /**
     * 任职岗位
     */
    @CheckCompare(value = "post", name = "任职岗位")
    private String post;

    /**
     * 主要工作描述
     */
    @CheckCompare(value = "jobDescription", name = "主要工作描述")
    private String jobDescription;

    /**
     * 创建时间
     */
    private Date createTime;


}
