package com.zerody.user.vo.statis;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author : chenKeFeng
 * @date : 2023/5/8 11:41
 */
@Data
public class DegreeVo {

    /**
     * 人数
     */
    private Integer num;

    /**
     * 学历
     *  SENIOR_HIGH("高中及以下"),
     *  JUNIOR_COLLEGE("大专"),
     *  REGULAR_COLLEGE("本科"),
     *  MASTER("硕士"),
     *  DOCTOR("博士");
     */
    private String degree;

    /**
     * 占比
     */
    private BigDecimal rate;

}
