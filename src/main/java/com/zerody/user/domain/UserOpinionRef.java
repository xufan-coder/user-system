package com.zerody.user.domain;

import lombok.Data;

import java.util.Date;

/**
 * 问题反馈查看人员表
 *
 * @ClassName CourseColumn
 * @author PengQiang
 * @DateTime 2022/7/8_16:14
 * @Deacription TODO
 */

/**
 * @author kuang
 */
@Data
public class UserOpinionRef {

    private String id;

    /**意见id*/
    private String opinionId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /** 回复人类型 (1 直接回复人 0 协助回复人) */
    private Integer replyType;
}