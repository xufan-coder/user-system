package com.zerody.user.dto;

import lombok.Data;

/**
 * @author PengQiang
 * @ClassName SubordinateUserQueryVo
 * @DateTime 2021/7/13_11:25
 * @Deacription TODO
 */
@Data
public class SubordinateUserQueryDto {

    private String departId;

    private String userId;

    private String companyId;

    private Boolean isCompanyAdmin;

    private Boolean isDepartAdmin;
}
