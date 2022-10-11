/**
 *
 */
package com.zerody.user.dto;

import com.zerody.common.vo.UserVo;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 黑名单统计查询
 * @author  DaBai
 * @date  2022/10/11 9:52
 */

@Data
public class BlackQueryDto {


    private UserVo userVo;

    /**
    *    关联企业IDs
    */
    List<String> companyIds;
}
