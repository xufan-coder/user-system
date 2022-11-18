package com.zerody.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author kuang
 */
@Data
public class UserBirthdayTemplateDto {

    private String id;

    /**生日月份*/
    private List<Integer> monthList;

    /**入职年份*/
    private List<String> yearList;

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

    /**
     * 模板类型 0-生日 1-入职
     */
    private Integer type;
}
