/**
 * 
 */
package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import lombok.Data;

/**
 * @author  DaBai
 * @date  2021/1/5 15:06
 */
@Data
public class AdminsPageDto extends PageQueryDto{

    private String userName;
}
