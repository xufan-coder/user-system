package com.zerody.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.user.constant.CheckCompare;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class InternalControlDto {
    /** 手机号 **/
    private String phoneNumber;

    /** 证件号码 **/
    private String certificateCard;

    /** 公司id**/
    private String companyId;
}
