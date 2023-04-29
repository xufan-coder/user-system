package com.zerody.user.vo;

import lombok.Data;

/**
 * @author : chenKeFeng
 * @date : 2023/4/29 10:23
 */
@Data
public class StatisticsDataDetailsVo {

    /**
     * 日期
     */
    private String dateStr;

    /**
     * 新签约
     */
    private String newAgencyNum;

    /**
     * 解约
     */
    private String terminationNum;

    /**
     * 净增伙伴数量
     */
    private String netIncreaseNum;

    /**
     * 签约中
     */
    private String agencyNum;

}
