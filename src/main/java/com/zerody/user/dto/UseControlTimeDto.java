package com.zerody.user.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author  DaBai
 * @date  2022/3/1 13:57
 */
@Data
public class UseControlTimeDto {

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

}
