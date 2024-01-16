package com.zerody.user.vo;

import lombok.Data;

/**
 * @author PengQiang
 * @ClassName UserAdminVo
 * @DateTime 2023/7/13 17:02
 */
@Data
public class UserAdminVo {

    private Boolean isCompanyAdmin;

    private Boolean isDepartAdmin;

    private Boolean isTeamAdmin;
}
