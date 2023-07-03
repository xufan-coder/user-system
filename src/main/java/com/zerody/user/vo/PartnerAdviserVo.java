package com.zerody.user.vo;

import lombok.Data;

/**
 * @author : chenKeFeng
 * @date : 2023/6/25 9:18
 */

@Data
public class PartnerAdviserVo {

    /**
     * crm用户ID
     */
    private String crmUserId;

    /**
     * 伙伴名称
     */
    private String userName;

    /**
     * 在职状态（0合约中、 1已解约、 3合作中）
     */
    private Integer status;

    /**
     * 是否是预备高管 （0.否 1.是 2.退学)
     */
    private Integer isPrepareExecutive;

    /**
     * 号码
     */
    private String phoneNumber;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 部门名称
     */
    private String departName;

}
