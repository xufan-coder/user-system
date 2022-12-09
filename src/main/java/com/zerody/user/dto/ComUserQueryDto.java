package com.zerody.user.dto;

import lombok.Data;

/**
 * @author kuang
 */
@Data
public class ComUserQueryDto {

    private String searchName;

    private Integer isShowLeave;

    /**是否查询离职伙伴   0-否 1-是*/
    private Integer isQuit;
}
