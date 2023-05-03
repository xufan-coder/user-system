package com.zerody.user.dto.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author PengQiang
 * @ClassName TimePeriodType
 * @DateTime 2023/4/29 11:02
 */

@Data
public class TimePeriodType extends UserPositionPageParam{

    /**
     * day:日、week:周、month:月、year:年
     */
    private String type;

    /**
     * 开始时间
     */
    private Date begin;

    /**
     * 结束时间
     */
    private Date end;
}
