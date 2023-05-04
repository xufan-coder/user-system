package com.zerody.user.vo;

import lombok.Data;

/**
 * 签约汇总数据
 *
 * @author : chenKeFeng
 * @date : 2023/5/3 15:43
 */
@Data
public class SignSummaryVo {

    /**
     * 企业或者部门名称
     */
    private String name;

    /**
     * 历史签约 = 签约中 + 已解约 + 合作中
     */
    private Integer historicalContractNum =0;

    /**
     * 合约中(签约中)
     */
    private Integer contractNum =0;

    /**
     * 已解约
     */
    private Integer cancelledNum =0;

    /**
     * 今日签约
     */
    private Integer todaySignNum =0;

    /**
     * 今日解约
     */
    private Integer todayRescindNum =0;

    /**
     * 昨日签约
     */
    private Integer yesterdaySignNum =0;

    /**
     * 昨日解约
     */
    private Integer yesterdayRescindNum =0;

    /**
     * 本月签约
     */
    private Integer monthSignNum =0;

    /**
     * 本月解约
     */
    private Integer monthRescindNum =0;

    /**
     * 砖石会员
     */
    private Integer masonryMemberNum =0;

    /**
     * 预备高管
     */
    private Integer prospectiveExecutiveNum =0;

    /**
     * 总经理
     */
    private Integer managerNum =0;

    /**
     * 副总经理
     */
    private Integer vicePresidentNum =0;

    /**
     * 团队长
     */
    private Integer teamLeaderNum =0;

    /**
     * 合作伙伴
     */
    private Integer partnerNum =0;

    /**
     * 二次签约
     */
    private Integer secondContractNum =0;

    /**
     * 男性
     */
    private Integer maleNum =0;

    /**
     * 女性
     */
    private Integer femaleNum =0;

    /**
     * 高中以下人数
     */
    private Integer highHereinafterNum =0;

    /**
     * 大专人数
     */
    private Integer collegeNum =0;

    /**
     * 本科人数
     */
    private Integer undergraduateNum =0;

    /**
     * 硕士人数
     */
    private Integer masterNum =0;

    /**
     * 博士人数
     */
    private Integer doctorNum =0;

}
