package com.zerody.user.vo;

import lombok.Data;

/**
 * @author kuang
 */
@Data
public class BirthdayGreetingCardVo {

    private String id;

    /**贺卡地址*/
    private String cardUrl;

    /**排序*/
    private Integer sort;

    /**启用状态 0否 1是*/
    private Integer state;
}
