package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.common.bean.PageInfo;
import lombok.Data;

/**
 * @author PengQiang
 * @ClassName SysAuthRoleInfoPageDto
 * @DateTime 2020/12/17_18:23
 * @Deacription TODO
 */
@Data
public class SysAuthRolePageDto extends PageQueryDto {

    private String staffId;
}
