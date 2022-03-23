package com.zerody.user.dto;


import com.zerody.user.enums.AdvertLocation;
import com.zerody.user.enums.AdvertType;
import com.zerody.user.enums.LinkType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yumiaoxia
 * @since 2021-07-07
 */
@ApiModel(value = "AdvertisingSpaceListDto", description = "")
@Data
public class BannerListDto {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类型")
    private AdvertType type;

    @ApiModelProperty(value = "位置")
    private AdvertLocation location;

    @ApiModelProperty(value = "链接类型")
    private LinkType linkType;

    @ApiModelProperty(value = "状态")
    private Boolean enable;
}
