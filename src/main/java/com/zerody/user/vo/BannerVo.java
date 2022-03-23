package com.zerody.user.vo;


import com.zerody.user.enums.AdvertLocation;
import com.zerody.user.enums.AdvertType;
import com.zerody.user.enums.LinkType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yumiaoxia
 * @since 2021-07-07
 */
@Data
public class BannerVo {

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

    @ApiModelProperty(value = "显示排序位（1~999999）", required = true)
    private Integer orderNum;

}
