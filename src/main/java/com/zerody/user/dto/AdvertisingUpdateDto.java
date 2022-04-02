package com.zerody.user.dto;


import com.zerody.user.enums.AdvertLocation;
import com.zerody.user.enums.AdvertType;
import com.zerody.user.enums.LinkType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

/**
 * @author yumiaoxia
 * @since 2021-07-07
 */
@ApiModel(value = "AdvertisingUpdateDto", description = "")
@Data
public class AdvertisingUpdateDto {

    @ApiModelProperty(value = "状态")
    private Boolean enable;

    @Range(min = 1, max = 999999)
    @ApiModelProperty(value = "排序位（1-999999）")
    private Integer orderNum;

    @Length(max = 500)
    @ApiModelProperty(value = "图片URL")
    private String imageUrl;

    @Length(min = 1,max = 40)
    @ApiModelProperty(value = "名称")
    private String name;

//    @ApiModelProperty(value = "类型")
//    private Integer type;

    @ApiModelProperty(value = "所属位置")
    private Integer location;

    @ApiModelProperty(value = "链接类型")
    private Integer linkType;

    @Length(max = 500)
    @ApiModelProperty(value = "链接Url")
    private String linkUrl;
    /**有效时间开始*/
    private String effectiveStartTime;
    /**有效时间结束*/
    private String effectiveEndTime;
}
