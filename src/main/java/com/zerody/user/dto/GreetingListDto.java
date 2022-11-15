package com.zerody.user.dto;

import lombok.Data;

/**
 * @Author: YeChangWei
 * @Date: 2022/11/12 14:24
 */
@Data
public class GreetingListDto {
    /**
     * 启用状态 0否 1是
     */
    private Integer state;

    /**
     *   贺卡类型 0-生日 1-入职
     */
    private Integer type;
}
