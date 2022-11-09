package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import lombok.Data;

/**
 * @author  DaBai
 * @date  2022/11/9 10:50
 */


@Data
public class CallControlRecordPageDto extends PageQueryDto {

    /**
    *   企业ID
    */
    private String companyId;

    /**
     * 部门id
     **/
    private String departId;

    /**
    *   伙伴ID
    */
    private String userId;

}
