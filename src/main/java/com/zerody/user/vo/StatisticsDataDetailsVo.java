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
     * 新签约(包括合作中)
     */
    private Integer newAgencyNum;

    /**
     * 解约(包括合作中)
     */
    private Integer terminationNum;

    /**
     * 净增伙伴数量(签约减去解约)
     */
    private Integer netIncreaseNum;

    /**
     * 签约中(累计签约，包括合作中)
     */
    private Integer agencyNum;

}
