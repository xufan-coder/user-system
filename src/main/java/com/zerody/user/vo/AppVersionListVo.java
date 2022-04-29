package com.zerody.user.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author yumiaoxia
 * @since 2021-06-29
 */
@ApiModel(value = "AppVersionListVo", description = "")
@Data
public class AppVersionListVo {

    @ApiModelProperty(value = "ID", required = true)
    private String id;

    @ApiModelProperty(value = "产品名称", required = true)
    private String name;

    @ApiModelProperty(value = "手机系统类型", required = true)
    private Integer osType;

    @ApiModelProperty(value = "版本号", required = true)
    private String version;

    @ApiModelProperty(value = "更新类型", required = true)
    private Integer updateType;

    @ApiModelProperty(value = "更新内容", required = true)
    private String updateContent;

    @ApiModelProperty(value = "下载地址", required = true)
    private String downloadUrl;

    @ApiModelProperty(value = "更新时间", required = true)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "创建时间", required = true)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "创建人", required = true)
    private String createBy;

    @ApiModelProperty(value = "平台系统类型", required = true)
    private Integer systemType;

    /**
     * 版本信息
     */
    @ApiModelProperty(value = "版本信息Id", required = true)
    private String versionInfoId;

}
