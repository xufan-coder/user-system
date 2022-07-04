package com.zerody.user.vo;

import lombok.Data;

import java.util.List;

/**
 * @author  DaBai
 * @date  2022/6/18 12:53
 */

@Data
public class BackUserRefVo {

    /**
    *   后台用户ID
    */
    private String backUserId;

    /**
    *   用户名称
    */
    private String userName;

    /**
    *   手机号码
    */
    private String phoneNumber;

    /**
    *   关联企业
    */
    private List<CompanyRefVo> companys;
}
