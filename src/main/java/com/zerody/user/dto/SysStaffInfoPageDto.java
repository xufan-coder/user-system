package com.zerody.user.dto;

import com.zerody.common.bean.PageInfo;
import lombok.Data;

/**
 * @author PengQiang
 * @ClassName SysStaffInfoPageDto
 * @DateTime 2020/12/18_8:42
 * @Deacription TODO
 */
@Data
public class SysStaffInfoPageDto extends PageInfo {

    private String staffId;

    private String staffName;

    private String phoneNumber;

    private Integer status;

    private String roleKey;

    private String departmentKey;

    private String jobKey;
}
