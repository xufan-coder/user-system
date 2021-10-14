package com.zerody.user.dto;

import com.zerody.user.enums.OsType;
import com.zerody.user.enums.SystemType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yumiaoxia
 * @since 2021-06-29
 */
@ApiModel(value = "AppVersionListDto", description = "")
@Data
public class AppVersionListDto {

    @ApiModelProperty(value = "系统类型")
    private OsType osType;

    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "产品名称")
    private String name;

    @ApiModelProperty(value = "平台系统类型", required = true)
    private SystemType systemType;

}
