package com.zerody.user.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 学历分析数据
 *
 * @author : chenKeFeng
 * @date : 2023/4/29 11:29
 */
@Data
public class DegreeAnalysisVo {

    /**
     * 总人数
     */
    private Integer allNum;

    /**
     * 高中以下人数
     */
    private Integer highHereinafterNum;

    /**
     * 高中以下人数占比
     */
    private BigDecimal highHereinafterRate;

    /**
     * 大专人数
     */
    private Integer collegeNum;

    /**
     * 大专占比
     */
    private BigDecimal collegeRate;

    /**
     * 本科人数
     */
    private Integer undergraduateNum;

    /**
     * 本科占比
     */
    private BigDecimal undergraduateRate;

    /**
     * 硕士人数
     */
    private Integer masterNum;

    /**
     * 硕士占比
     */
    private BigDecimal masterRate;

    /**
     * 博士人数
     */
    private Integer doctorNum;

    /**
     * 博士占比
     */
    private BigDecimal doctorRate;

}
