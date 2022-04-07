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
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author yumiaoxia
 * @since 2021-07-07
 */
@ApiModel(value = "AdvertisingSpaceCreateDto", description = "")
@Data
public class BannerCreateDto {

    @NotBlank
    @Length(max = 500)
    @ApiModelProperty(value = "图片URL", required = true)
    private String imageUrl;

    @NotBlank
    @Length(min = 1,max = 40)
    @ApiModelProperty(value = "名称", required = true)
    private String name;

//    @NotNull
//    @ApiModelProperty(value = "类型", required = true)
//    private Integer type;

    @NotNull
    @ApiModelProperty(value = "所属位置", required = true)
    private Integer location;

    @NotNull
    @ApiModelProperty(value = "链接类型", required = true)
    private Integer linkType;

    @Length(max = 500)
    @ApiModelProperty(value = "链接Url", required = true)
    private String linkUrl;

    @NotNull
    @ApiModelProperty(value = "状态", required = true)
    public Boolean enable;

    @NotNull
    @Range(min = 1, max = 999999)
    @ApiModelProperty(value = "显示排序位（1~999999）, 默认999999", required = true)
    private Integer orderNum = 999999;
    /**有效时间开始*/
    private String effectiveStartTime;
    /**有效时间结束*/
    private String effectiveEndTime;
}
