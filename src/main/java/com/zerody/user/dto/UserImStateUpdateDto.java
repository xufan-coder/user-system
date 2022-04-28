package com.zerody.user.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author PengQiang
 * @ClassName UserImStateUpdateDto
 * @DateTime 2022/4/28_13:28
 * @Deacription TODO
 */
@Data
public class UserImStateUpdateDto {

    /** 用户id */
    @NotEmpty(message = "用户id不能为空")
    private String id;

    /** im状态 */
    private String imState;

}
