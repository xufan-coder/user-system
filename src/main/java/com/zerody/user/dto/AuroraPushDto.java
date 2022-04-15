package com.zerody.user.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author zhangpingping
 * @date 2021年12月22日 14:06
 */
 @Data
public class AuroraPushDto {
    /**用户ID*/
    private String userId;
    /**设备号*/
    private String deviceId;
    /**登录时间*/
    private Date loginTime;
    /**有效时间*/
    private String valideTime;
    /**用户设备*/
    private String userDevice;
}
