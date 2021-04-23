package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import lombok.Data;

/**
 * @author  DaBai
 * @date  2021/4/22 10:25
 */

@Data
public class CeoUserInfoPageDto extends PageQueryDto {

    private String userName;

    private String phone;

}
