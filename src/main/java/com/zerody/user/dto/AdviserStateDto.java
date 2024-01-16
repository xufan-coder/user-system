package com.zerody.user.dto;

import lombok.Data;

/**
 * @Author : xufan
 * @create 2024/1/16 15:29
 */
@Data
public class AdviserStateDto {

    /**
     *  顾问伙伴id
     */
    private String crmUserId;

    /**
     * 是否启用,1启用，0停用
     */
    private Integer enabled;
}
