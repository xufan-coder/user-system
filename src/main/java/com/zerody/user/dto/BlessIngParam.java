package com.zerody.user.dto;

import lombok.Data;

/**
 * @author kuang
 */
@Data
public class BlessIngParam {

    /**祝福语句*/
    private String blessingText;

    /**贺卡路径*/
    private String greetingUrl;

    /**生日伙伴*/
    private String birthdayUserId;

    private String userId;
}
