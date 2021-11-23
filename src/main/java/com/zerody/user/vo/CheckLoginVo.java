package com.zerody.user.vo;

import lombok.Data;

/**
 * @author  DaBai
 * @date  2021/1/8 16:32
 */

@Data
public class CheckLoginVo {

    /**
     *
     *  密码
     */
    private String userPwd;

    /**
     *
     * 员工ID
     */
    private String staffId;


    /** 手机号码 */
    private String phoneNumber;



}
