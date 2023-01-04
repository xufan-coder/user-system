package com.zerody.user.domain;

import lombok.Data;

import java.util.Date;

/**
 *
 *  图片表
 *
 * @author               PengQiang
 * @description          DELL
 * @date                 2021/8/3 19:17
 */
@Data
public class Image {
    private String id;

    /**
     * 连接id
     */
    private String connectId;

    /**
     * 图片路径
     */
    private String imageUrl;

    /**
     * 图片类型
     */
    private String imageType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人id
     */
    private String createBy;

    /**
     * 创建人名称
     */
    private String createUsername;
}