package com.zerody.user.domain;

import lombok.Data;

import java.util.Date;

/**
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


}