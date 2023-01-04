package com.zerody.user.dto;

import lombok.Data;

/**
 * @author kuang
 */
@Data
public class BirthdayGreetingCardDto {

    private String id;

    private String cardUrl;

    private Integer sort;

    private Integer state;

    /**
     *   贺卡类型 0-生日 1-入职
     */
    private Integer type;

}
