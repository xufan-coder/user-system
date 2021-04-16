package com.zerody.user.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author  DaBai
 * @date  2021/1/11 10:46
 */

@Data
public class CopyStaffInfoVo {

    private String id;

    /**
    *    用户id
    */
    private String userId;

    /**
    *   用户姓名
    */
    private String userName;

    /**
    *   手机号
    */
    private String phoneNumber;

    /**
    *   头像
    */
    private String avatar;

    /**
    *    员工ID
    */
    private String staffId;

    /**
     *  密码
     */
    private String userPwd;
    /**
     *  角色ID
     */
    private String roleId;

}
