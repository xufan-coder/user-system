package com.zerody.user.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author PengQiang
 * @ClassName SetUpdateAvatarDto
 * @DateTime 2021/3/25_14:45
 * @Deacription TODO
 */
@Data
public class SetUpdateAvatarDto {

    /** 用户id */
    @NotEmpty(message = "用户id不能为空")
    private String userId;

    /** 用户头像 */
    @NotEmpty(message = "头像路径不能为空")
    private String avatar;
}
