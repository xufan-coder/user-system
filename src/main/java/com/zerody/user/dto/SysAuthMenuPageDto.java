package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.common.bean.PageInfo;
import lombok.Data;

/**
 * @author PengQiang
 * @ClassName SysAuthMenuPageDto
 * @DateTime 2020/12/17_9:25
 * @Deacription TODO
 */
@Data
public class SysAuthMenuPageDto extends PageQueryDto {

    private String sysId;

}
