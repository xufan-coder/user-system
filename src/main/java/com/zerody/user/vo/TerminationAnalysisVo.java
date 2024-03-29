package com.zerody.user.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author : chenKeFeng
 * @date : 2023/4/29 11:00
 */
@Data
public class TerminationAnalysisVo {

    /**
     * 离职原因id
     */
    private String id;

    /**
     * 离职原因
     */
    private String name;

    /**
     * 人数
     */
    private Integer peopleNum;

    /**
     * 人数占比
     */
    private BigDecimal peopleRate;

}
