package com.zerody.user.dto;


import com.zerody.user.enums.AdvertLocation;
import com.zerody.user.enums.AdvertType;
import com.zerody.user.enums.LinkType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

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
    private Integer type;

    @ApiModelProperty(value = "位置")
    private Integer location;

    @ApiModelProperty(value = "链接类型")
    private Integer linkType;

    @ApiModelProperty(value = "状态")
    private Boolean enable;
    /**有效时间开始*/
    private String effectiveStartTime;
    /**有效时间结束*/
    private String effectiveEndTime;
}
