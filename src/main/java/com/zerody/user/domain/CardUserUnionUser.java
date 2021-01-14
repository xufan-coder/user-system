package com.zerody.user.domain;

import lombok.Data;

/**
 * @author  DaBai
 * @date  2021/1/14 17:20
 */
@Data
public class CardUserUnionUser {
    private String id;

    //名片小程序用户ID
    private String cardId;

    //CRM用户ID
    private String userId;
    
}