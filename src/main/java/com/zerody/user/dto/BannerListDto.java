package com.zerody.user.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.user.enums.AdvertLocation;
import com.zerody.user.enums.AdvertType;
import com.zerody.user.enums.LinkType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

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

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    /**有效时间开始*/
    private String effectiveStartTime;
    /**有效时间结束*/

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String effectiveEndTime;
}
