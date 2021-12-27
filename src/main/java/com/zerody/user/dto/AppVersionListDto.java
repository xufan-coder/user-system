package com.zerody.user.dto;

import com.zerody.user.enums.OsType;
import com.zerody.user.enums.SystemType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author yumiaoxia
 * @since 2021-06-29
 */
@ApiModel(value = "AppVersionListDto", description = "")
@Data
public class AppVersionListDto {

    @ApiModelProperty(value = "系统类型")
    @NotNull(message = "系统类型不能为空")
    private Integer osType;

    @ApiModelProperty(value = "版本号")
    @NotEmpty(message = "版本号不能为空")
    private String version;

    @ApiModelProperty(value = "产品名称")
    private String name;

    @ApiModelProperty(value = "平台系统类型", required = true)
    @NotNull(message = "平台系统类型不能为空")
    private Integer systemType;

}
