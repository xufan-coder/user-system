package com.zerody.user.dto;

import lombok.Data;

import java.util.List;

/**
 * @author  DaBai
 * @date  2022/3/1 13:57
 */

@Data
public class UsersUseControlDto {

    private List<String> userIds;

    /**
    *   禁止1/允许2类型
    */
    private Integer type;

}
