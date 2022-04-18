package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import lombok.Data;

/**
 * @author DELL
 */
@Data
public class UserOpinionQueryDto extends PageQueryDto {

    /**提交人名称*/
    private String searchName;


}
