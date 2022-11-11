package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import lombok.Data;

/**
 * @author  DaBai
 * @date  2022/11/9 10:39
 */

@Data
public class CallControlPageDto extends PageQueryDto {

    /**
    *   企业ID
    */
    private String companyId;

}
