package com.zerody.user.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author kuang
 */
@Data
public class UserReply {

    private String id;

    /**意见id*/
    private String opinionId;

    /**
     * 内容
     */
    private String content;

    /**视频*/
    private String video;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 是否删除
     */
    private Integer deleted;

}
