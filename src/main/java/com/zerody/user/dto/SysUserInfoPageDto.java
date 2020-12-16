package com.zerody.user.dto;

import com.zerody.common.bean.PageInfo;
import lombok.Data;

/**
 * @author PengQiang
 * @ClassName SysUserInfoPageDto
 * @DateTime 2020/12/16_17:33
 * @Deacription TODO
 */
@Data
public class SysUserInfoPageDto extends PageInfo {

    private String userName;

    private String phone;

}
