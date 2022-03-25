package com.zerody.user.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.user.enums.AdvertLocation;
import com.zerody.user.enums.AdvertType;
import com.zerody.user.enums.LinkType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author yumiaoxia
 * @since 2021-07-07
 */
@Data
public class BannerListVo {


    @ApiModelProperty(value = "ID", required = true)
    private String id;


    @ApiModelProperty(value = "图片URL", required = true)
    private String imageUrl;

    @ApiModelProperty(value = "名称", required = true)
    private String name;


    @ApiModelProperty(value = "类型", required = true)
    private AdvertType type;


    @ApiModelProperty(value = "所属位置", required = true)
    private AdvertLocation location;


    @ApiModelProperty(value = "链接类型", required = true)
    private LinkType linkType;


    @ApiModelProperty(value = "链接Url", required = true)
    private String linkUrl;


    @ApiModelProperty(value = "状态", required = true)
    private Boolean enable;


    @ApiModelProperty(value = "显示排序位（1~999999）", required = true)
    private Integer orderNum;


    @ApiModelProperty(value = "创建时间", required = true)
    private Date createTime;

    /**有效时间开始*/
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveStartTime;
    /**有效时间结束*/
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveEndTime;

}
