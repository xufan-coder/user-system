package com.zerody.user.dto;

import lombok.Data;

/**
 * @Author : xufan
 * @create 2023/3/10 17:35
 */
@Data
public class BlacklistOperationRecordAddDto {

    /** 手机号码 */
    private String mobile;

    /** 证件号码 **/
    private String IdentityCard;

    /** 操作类型 */
    private Integer type;

    /** 操作备注 */
    private String remarks;
}
