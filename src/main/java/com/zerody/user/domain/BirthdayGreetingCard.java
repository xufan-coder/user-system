package com.zerody.user.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author DELL
 * 生日贺卡管理
 */
@Data
public class BirthdayGreetingCard {

    private String id;

    /**贺卡地址*/
    private String cardUrl;

    /**排序*/
    private Integer sort;

    /**启用状态 0否 1是*/
    private Integer state;

    /**删除  0-删除 1-正常*/
    private Integer deleted;

    /**创建时间*/
    private Date createTime;

    /**创建人*/
    private String createBy;
}
