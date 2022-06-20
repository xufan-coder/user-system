/**
 *
 */
package com.zerody.user.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author  DaBai
 * @date  2022/6/18 12:46
 */

@Data
public class CeoRefDto {

    /**
    *   关联ID
    */
    @NotNull(message = "参数错误，id不能为空！")
    private String ceoId;
    /**
    *    平台企业ID
    */
    List<String> companyIds;
}
