package com.zerody.user.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 *
 * @author               kuang
 * @description          伙伴生日模板
 * @date                 2022/8/20 14:17
 */
@Data
public class UserBirthdayTemplate {

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

    /**删除状态  0-删除 1-正常*/
    private Integer deleted;

    /**创建时间**/
    private Date createTime;

    /**创建人ID*/
    private String createBy;

    /**创建人名称*/
    private String createUsername;

    /**更新人ID*/
    private String updateBy;

    /**更新时间*/
    private Date updateTime;
    /**
     * 模板类型 0-生日 1-入职
     */
    private Integer type;

}