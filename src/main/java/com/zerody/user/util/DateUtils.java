package com.zerody.user.util;

import com.alibaba.nacos.api.utils.StringUtils;
import com.zerody.common.constant.TimeDimensionality;
import com.zerody.common.util.DateUtil;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * @author PengQiang
 * @ClassName DateUtils
 * @DateTime 2021/4/23_14:59
 * @Deacription TODO
 */
public class DateUtils {


    /** 秒 */
    public final static Long SECOND = 1000L;

    /** 分 */
    public final static Long MINUTE = SECOND * 60L;

    /** 时 */
    public final static Long HOUR = MINUTE * 60L;

    /** 日 */
    public final static Long DAYS = HOUR * 24L;

    public static Date obtainTodayOrWeekOrMonthDate(String timePeriod){
        LocalDate now = LocalDate.now();
        if (StringUtils.isEmpty(timePeriod)) {
            return null;
        }
        // 本年
        if (TimeDimensionality.YEAR.equals(timePeriod)) {
            Date monthStart = Date
                    .from(LocalDate.now().atStartOfDay().minusDays(now.getDayOfYear() - 1).toInstant(ZoneOffset.of("+8")));
            return monthStart;
        }
        // 季度----> 近三月(前两月+当月)
        if (TimeDimensionality.QUARTER.equals(timePeriod)) {
            Date monthStart = Date
                    .from(LocalDate.now().atStartOfDay().minusMonths(2).minusDays(now.getDayOfMonth() - 1).toInstant(ZoneOffset.of("+8")));
            return monthStart;
        }
        // TODO: 2021/4/23 本月
        if (TimeDimensionality.MONTH.equals(timePeriod)) {
            Date monthStart = Date
                    .from(LocalDate.now().atStartOfDay().minusDays(now.getDayOfMonth() - 1).toInstant(ZoneOffset.of("+8")));
            return monthStart;
        }
        // TODO: 2021/4/23 本周
        if (TimeDimensionality.WEEK.equals(timePeriod)) {
            Date weekStart = Date.from(LocalDate.now().atStartOfDay().minusDays(now.getDayOfWeek().getValue() - 1)
                    .toInstant(ZoneOffset.of("+8")));
            return weekStart;
        }
        // TODO: 2021/4/23 昨日
        if (TimeDimensionality.YESTER.equals(timePeriod)) {
            Date today = Date.from(now.atStartOfDay().minusDays(1).toInstant(ZoneOffset.of("+8")));
            return today;
        }
        // TODO: 2021/4/23 当天
        if (TimeDimensionality.DAY.equals(timePeriod)) {
            Date today = Date.from(now.atStartOfDay().toInstant(ZoneOffset.of("+8")));
            return today;
        }

        // TODO: 2021/4/23 所有都没有的 返回当天
      return  Date.from(now.atStartOfDay().toInstant(ZoneOffset.of("+8")));
    }

    public static void main(String[] args) {
        Date monthStart = Date
                .from(LocalDate.now().atStartOfDay().minusMonths(2).minusDays(LocalDate.now().getDayOfMonth() - 1).toInstant(ZoneOffset.of("+8")));

        System.out.println(monthStart);
    }
}
