package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.common.bean.PageInfo;
import lombok.Data;

/**
 * @author PengQiang
 * @ClassName SysJobPositionDto
 * @DateTime 2020/12/18_18:17
 * @Deacription TODO
 */
@Data
public class SysJobPositionDto extends PageQueryDto {
    private String compId;
}
