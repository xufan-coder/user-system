package com.zerody.user.dto;


import com.zerody.user.enums.OsType;
import com.zerody.user.enums.SystemType;
import com.zerody.user.enums.UpdateType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author yumiaoxia
 * @since 2021-06-29
 */
@ApiModel(value = "AppVersionCreateDto", description = "")
@Data
public class AppVersionCreateDto {

    @ApiModelProperty(value = "产品名称", required = true)
    private String name;

    @ApiModelProperty(value = "系统类型", required = true)
    private OsType osType;

    @ApiModelProperty(value = "版本号",required = true)
    private String version;

    @Length(max = 500)
    @ApiModelProperty(value = "下载地址")
    private String downloadUrl;

    @ApiModelProperty(value = "更新类型")
    private UpdateType updateType;

    @Length(max = 1000)
    @ApiModelProperty(value = "更新内容")
    private String updateContent;

    @ApiModelProperty(value = "平台系统类型", required = true)
    private SystemType systemType;
}
