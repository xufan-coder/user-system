package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import lombok.Data;

/**
 * @author PengQiang
 * @ClassName ImportInfoQueryDto
 * @DateTime 2021/12/14_16:49
 * @Deacription TODO
 */
@Data
public class ImportInfoQueryDto extends PageQueryDto {

    private String companyId;

    private Integer type;

}
