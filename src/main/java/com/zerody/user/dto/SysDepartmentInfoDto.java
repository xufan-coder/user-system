package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.common.bean.PageInfo;
import lombok.Data;

/**
 * @author PengQiang
 * @ClassName SysDepartmentInfoDto
 * @DateTime 2020/12/19_13:26
 * @Deacription TODO
 */
@Data
public class SysDepartmentInfoDto extends PageQueryDto {

    /**
     * 企业id
     */
    private String compId;

    /**
     * 部门id
     */
    private String departId;

}
