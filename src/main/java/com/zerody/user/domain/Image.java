package com.zerody.user.domain;

import lombok.Data;

import java.util.Date;

/**
 *
 *  图片
 * @author               PengQiang
 * @description          DELL
 * @date                 2021/8/3 19:17
 */
@Data
public class Image {
    private String id;

    private String connectId;

    private String imageUrl;

    private String imageType;

    private Date createTime;

    private String createBy;

    private String createUsername;
}