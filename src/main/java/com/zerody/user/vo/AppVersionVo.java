package com.zerody.user.vo;

import com.zerody.user.enums.OsType;
import com.zerody.user.enums.SystemType;
import com.zerody.user.enums.UpdateType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author yumiaoxia
 * @since 2021-06-29
 */
@Data
public class AppVersionVo {

    @ApiModelProperty(value = "ID", required = true)
    private String id;

    @ApiModelProperty(value = "产品名称", required = true)
    private String name;

    @ApiModelProperty(value = "系统类型", required = true)
    private OsType osType;

    @ApiModelProperty(value = "版本号", required = true)
    private String version;

    @ApiModelProperty(value = "更新类型", required = true)
    private UpdateType updateType;

    @ApiModelProperty(value = "更新内容", required = true)
    private String updateContent;

    @ApiModelProperty(value = "下载地址", required = true)
    private String downloadUrl;

    @ApiModelProperty(value = "更新时间", required = true)
    private Date updateTime;

    @ApiModelProperty(value = "创建时间", required = true)
    private Date createTime;

    @ApiModelProperty(value = "创建人", required = true)
    private String createBy;

    @ApiModelProperty(value = "平台系统类型", required = true)
    private SystemType systemType;
}
