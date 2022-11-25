package com.zerody.user.domain;

import lombok.Data;

import java.util.Date;

/**
 * app版本内容信息表
 *
 * @author zhangpingping
 * @date 2021年12月16日 14:10
 */
@Data
public class AppVersionInfo {
    /***/
    private String id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 是否删除0 否1是
     */
    private Integer deleted;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 排序
     */
    private Integer orderNum;
}
