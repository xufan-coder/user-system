package com.zerody.user.dto.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author PengQiang
 * @ClassName SetTimePeriod
 * @DateTime 2021/4/23_15:40
 * @Deacription TODO
 */
@Data
public class SetTimePeriodPage extends UserPositionPageParam {
    /** 今日/昨日/本周/本月/自定义 */
    private String timePeriod;

    /** 起始时间 */
    private String beginTime;

    /** 结束时间 */
    private String endTime;

    /** 起始时间 */
    private Date begin;

    /** 结束时间 */
    private Date end;

    /** 日期月 */
    private String month;
}
