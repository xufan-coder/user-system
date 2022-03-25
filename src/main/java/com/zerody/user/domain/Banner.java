package com.zerody.user.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.user.enums.AdvertLocation;
import com.zerody.user.enums.AdvertType;
import com.zerody.user.enums.LinkType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
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
    private Integer type;

    /**
     *  所属位置
     */
    private Integer location;

    /**
     *  链接类型
     */
    private Integer linkType;

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

    /**有效时间开始*/
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveStartTime;
    /**有效时间结束*/
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveEndTime;


}
