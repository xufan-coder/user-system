package com.zerody.user.dto;

import lombok.Data;

import java.util.List;

/**
 * @author  DaBai
 * @date  2023/9/4 11:46
 */


@Data
public class UsersTokenControlDto {

    private List<String> removeIds;

    /**
    *   企业1/部门2/用户3
    */
    private Integer type;

}
