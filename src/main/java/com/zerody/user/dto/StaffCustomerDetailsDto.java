package com.zerody.user.dto;

import lombok.Data;

/**
 * @author zhangpingping
 * @date 2021年09月11日 17:01
 */
@Data
public class StaffCustomerDetailsDto {

    private String userId;
    /**
     * 客户类型
     */
    private String type;
}
