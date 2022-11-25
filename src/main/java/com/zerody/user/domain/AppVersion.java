package com.zerody.user.domain;

import com.zerody.user.enums.OsType;
import com.zerody.user.enums.SystemType;
import com.zerody.user.enums.UpdateType;
import lombok.Data;

import java.util.Date;

/**
 * app版本控制表
 *
 * @author zhangpingping
 * @date 2021年10月13日 17:19
 */
@Data
public class AppVersion {
    /**
     * ID
     */
    private String id;

    /**
     * 产品名称
     */
    private String name;

    /**
     * 系统类型
     */
    private Integer osType;

    /**
     * 版本号
     */
    private String version;

    /**
     * 更新类型
     */
    private Integer updateType;

    /**
     * 更新内容
     */
    private String updateContent;

    /**
     * 下载地址
     */
    private String downloadUrl;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;
    /**
     * 平台系统类型
     */
    private Integer systemType;
    /**
     * 版本信息
     */
    private String versionInfoId;
}
