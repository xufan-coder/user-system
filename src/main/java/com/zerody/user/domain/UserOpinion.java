package com.zerody.user.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 *  问题反馈记录表
 *
 * @author kuang
 */
@Data
public class UserOpinion {

    private String id;

    /**
     * 内容
     */
    private String content;

    /**视频*/
    private String video;

    /**分类id*/
    private String typeId;

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

    /** 处理进度(0 待处理 1 处理中 2 已完成) */
    private Integer state;

}
