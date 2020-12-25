package com.zerody.user.dto;

import com.zerody.common.bean.PageInfo;
import lombok.Data;

/**
 * @author PengQiang
 * @ClassName SysCompanyInfoDto
 * @DateTime 2020/12/18_17:47
 * @Deacription TODO
 */
@Data
public class SysCompanyInfoDto extends PageInfo {

    private String companyName; //企业名称

    private Integer loginStatus; //企业登录状态

}
