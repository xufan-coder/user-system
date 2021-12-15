package com.zerody.user.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author PengQiang
 * @ClassName ExportDto
 * @DateTime 2021/12/14_18:04
 * @Deacription TODO
 */
@Data
public class ExportDto {
    /** 导入id */
    @NotEmpty(message = "导入id不能为空")
    private String  id;
}
