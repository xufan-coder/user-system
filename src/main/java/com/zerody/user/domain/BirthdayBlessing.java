package com.zerody.user.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author DELL
 * 生日祝福语句
 */
@Data
public class BirthdayBlessing {

    private String id;

    /**生日祝福语句*/
    private String blessingContent;

    /**删除  0-删除 1-正常*/
    private Integer deleted;

    /**创建时间*/
    private Date createTime;

}
