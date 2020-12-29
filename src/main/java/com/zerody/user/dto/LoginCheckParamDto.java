package com.zerody.user.dto;

import lombok.Data;
/**
 * @author  DaBai
 * @date  2020/12/29 17:53
 */

@Data
public class LoginCheckParamDto {
    /**
    *   用户名或者手机号
    */
    private String userName;
    /**
    *   解密后的密码参数
    */
    private String pwd;
}
