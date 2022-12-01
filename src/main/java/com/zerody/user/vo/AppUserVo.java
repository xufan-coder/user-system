package com.zerody.user.vo;

import lombok.Data;

/**
 * APP端员工信息
 *
 * @author : chenKeFeng
 * @date : 2022/11/28 9:15
 */
@Data
public class AppUserVo {

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 电话号码
     */
    private String phoneNumber;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 员工id
     */
    private String staffId;

}
