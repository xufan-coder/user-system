package com.zerody.user.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author  DaBai
 * @date  2022/12/29 14:04
 */

@Data
public class IdCardUpdateDto {
    /**
     * 用户ID
     **/
    @NotNull(message = "用户id不能为空！")
    private String userId;
    /**
     * 身份证照片国徽面(正面)
     */
    @NotNull(message = "身份证照片国徽面不能为空！")
    private String idCardFront;

    /**
     * 身份证照片人像面(反面)
     */
    @NotNull(message = "身份证照片人像面不能为空！")
    private String idCardReverse;
}
