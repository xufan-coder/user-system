package com.zerody.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author kuang
 */
@Data
public class UserBirthdayTemplateVo {

    private String id;

    /**生日祝福*/
    private String blessing;

    /**推送时间*/
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "HH:mm")
    private Date pushTime;

    /**海报背景图*/
    private String posterUrl;

    /**是否通知同事  0-否 1-是*/
    private Integer noticeColleague;

    /**通知同事文本内容*/
    private String noticeText;

    /**同事海报背景图*/
    private String noticePosterUrl;

    /**是否通知管理层  0-否 1-是*/
    private Integer adminColleague;

    /**通知管理层文本内容*/
    private String adminText;

    /**管理层海报背景图*/
    private String adminPosterUrl;

    /**启用状态  0-否 1-是*/
    private Integer state;

    /**模板月份*/
    private String monthText;

    /**模板年份*/
    private String yearText;

    /**模板月份*/
    private List<String> monthList;

    public List<String> getMonthList(){

        if(StringUtils.isEmpty(monthText)){
            return new ArrayList<>();
        }
        return Arrays.asList(monthText.split(","));
    }
    /**模板年份*/
    private List<String> yearList;
    public List<String> getYearList(){

        if(StringUtils.isEmpty(yearText)){
            return new ArrayList<>();
        }
        return Arrays.asList(yearText.split(","));
    }
}
