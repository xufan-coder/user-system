package com.zerody.user.vo;

import lombok.Data;

import java.util.List;

/**
 * @author  DaBai
 * @date  2022/6/18 12:53
 */

@Data
public class CeoRefVo{

    /**
    *   ceoID
    */
    private String ceoId;

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
