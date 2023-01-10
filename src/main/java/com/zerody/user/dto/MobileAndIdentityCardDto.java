package com.zerody.user.dto;

import lombok.Data;

@Data
public class MobileAndIdentityCardDto {

    private String mobile;

    /** 证件号码 **/
    private String IdentityCard;
}
