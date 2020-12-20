package com.zerody.user.vo;

import lombok.Data;

/**
 * @author PengQiang
 * @ClassName SysComapnyInfoVo
 * @DateTime 2020/12/18_18:01
 * @Deacription TODO
 */
@Data
public class SysComapnyInfoVo {

    private String companyName;

    private String adminAccount;

    private Integer loginStatus;

    private String remark;

}
