package com.zerody.user.vo;

import lombok.Data;

/**
 * @author  DaBai
 * @date  2022/11/9 10:35
 */

@Data
public class CallControlTimeVo {

    /**
    *   星期
    */
    private Integer week;

    /**
    *   开始时间段
    */
    private Integer start;

    /**
    *   结束时间段
    */
    private Integer end;
    /**
    *   是否启用1 0
    */
    private Integer enable;

    /**
     *   预警次数
     */
    private Integer tipNum;

    /**
     *   限制最大呼叫次数
     */
    private Integer callNum;

}
