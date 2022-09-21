package com.zerody.user.dto.data;

import com.zerody.common.vo.UserVo;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author PengQiang
 * @ClassName DataAddDto
 * @DateTime 2022/9/15_14:39
 * @Deacription TODO
 */
@Data
public class DataAddDto {

    /** key */
    @NotEmpty(message = "key为空了")
    private String dataKey;

    /** 值 */
    private String dataValue;

    private UserVo user;
}
