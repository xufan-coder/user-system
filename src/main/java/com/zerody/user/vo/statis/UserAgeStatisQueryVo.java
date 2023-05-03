package com.zerody.user.vo.statis;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author PengQiang
 * @ClassName UserAgeStatisQueryVo
 * @DateTime 2023/5/3 10:17
 */
@Data
public class UserAgeStatisQueryVo {

    public UserAgeStatisQueryVo(){

    }

    public UserAgeStatisQueryVo(Integer beginAge, Integer endAge) {
        this.beginAge = beginAge;
        this.endAge = endAge;
    }

    /**
     * 起始年龄
     */
    private Integer beginAge;

    /**
     * 结束年龄
     */
    private Integer endAge;

    /**
     * 人数
     */
    private Integer number;

    /**
     * 占比
     */
    private BigDecimal rate = BigDecimal.ZERO;

}
