package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import lombok.Data;

import java.util.List;

/**
 * @author  DaBai
 * @date  2022/3/1 13:57
 */
@Data
public class UsersUseControlPageDto extends PageQueryDto {

    /**
    *   类型黑1/白 2名单
    */
    private Integer type;

    /**
    *   企业ID
    */
    private String companyId;

}
