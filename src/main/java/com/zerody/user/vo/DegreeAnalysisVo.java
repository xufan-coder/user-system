package com.zerody.user.vo;

import lombok.Data;

/**
 * 学历分析数据
 *
 * @author : chenKeFeng
 * @date : 2023/4/29 11:29
 */
@Data
public class DegreeAnalysisVo {

    /**
     * 高中以下人数
     */
    private String highHereinafterNum;

    /**
     * 高中以下人数占比
     */
    private String highHereinafterRate;

    /**
     * 大专人数
     */
    private String collegeNum;

    /**
     * 大专占比
     */
    private String collegeRate;

    /**
     * 本科人数
     */
    private String undergraduateNum;

    /**
     * 本科占比
     */
    private String undergraduateRate;

    /**
     * 硕士人数
     */
    private String masterNum;

    /**
     * 硕士占比
     */
    private String masterRate;

    /**
     * 博士人数
     */
    private String doctorNum;

    /**
     * 博士占比
     */
    private String doctorRate;

}
