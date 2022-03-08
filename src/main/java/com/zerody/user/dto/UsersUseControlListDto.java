package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import lombok.Data;

/**
 * @author  DaBai
 * @date  2022/3/8 9:43
 */

@Data
public class UsersUseControlListDto {

    /**
    *   类型黑1/白 2名单
    */
    private Integer type;

    /**
    *   企业ID
    */
    private String companyId;

}
