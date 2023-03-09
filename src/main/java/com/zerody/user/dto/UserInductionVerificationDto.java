package com.zerody.user.dto;

import lombok.Data;

/**
 * @author kuang
 * @description 伙伴二次入职校验参数
 **/
@Data
public class UserInductionVerificationDto {

    public String userId;

    /**公司id*/
    private String companyId;

    /**身份证号*/
    private String certificateCard;

    /**手机号码*/
    private String mobile;


}
