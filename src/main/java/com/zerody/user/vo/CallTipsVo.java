package com.zerody.user.vo;

import lombok.Data;

/**
 * @author  DaBai
 * @date  2022/11/19 14:48
 */

@Data
public class CallTipsVo {

    /**
     * 描述
     */
    private String message;

    /**
     * 类型 1可拨打 0不可拨打
     */
    private Integer type;

}
