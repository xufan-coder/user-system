package com.zerody.user.dto;

import lombok.Data;

/**
 * @author PengQiang
 * @ClassName SetCompanyAdminAccountDto
 * @DateTime 2021/1/8_12:08
 * @Deacription TODO
 */
@Data
public class SetAdminAccountDto {


    private String id;

    /**
     * 员工id
     */
    private String staffId;
}
