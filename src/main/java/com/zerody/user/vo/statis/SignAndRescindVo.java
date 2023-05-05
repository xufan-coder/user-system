package com.zerody.user.vo.statis;

import lombok.Data;

/**
 * @author : chenKeFeng
 * @date : 2023/5/5 15:54
 */
@Data
public class SignAndRescindVo {

    /**
     * 今日签约
     */
    private Integer todaySignNum;

    /**
     * 本月签约
     */
    private Integer monthSignNum;

    /**
     * 今日解约
     */
    private Integer todayRescindNum;

    /**
     * 本月解约
     */
    private Integer monthRescindNum;

    /**
     * 昨日签约
     */
    private Integer yesterdaySignNum;

    /**
     * 昨日解约
     */
    private Integer yesterdayRescindNum;

}
