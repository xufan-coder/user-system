package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import lombok.Data;

/**
 * @author kuang
 */
@Data
public class TemplatePageDto extends PageQueryDto {

    /**启用状态 0-否 1-是*/
    private Integer state;

    /**月份 多个月份已，分割*/
    private String month;
}
