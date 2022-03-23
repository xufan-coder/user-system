package com.zerody.user.domain;


import com.zerody.user.enums.AdvertLocation;
import com.zerody.user.enums.AdvertType;
import com.zerody.user.enums.LinkType;
import lombok.Data;

import java.util.Date;

/**
 * @author yumiaoxia
 * @since 2021-07-06
 */
@Data
public class Banner {

    /**
     * ID
     */
    private String id;

    /**
     * 图片URL
     */
    private String imageUrl;

    /**
     * 名称
     */
    private String name;

    /**
     *  类型
     */
    private AdvertType type;

    /**
     *  所属位置
     */
    private AdvertLocation location;

    /**
     *  链接类型
     */
    private LinkType linkType;

    /**
     * 链接Url
     */
    private String linkUrl;

    /**
     * 状态
     */
    private Boolean enable;

    /**
     * 显示排序位（1~999999）
     */
    private Integer orderNum;

    /**
     * 创建时间
     */
    private Date createTime;


}
