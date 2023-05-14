package com.zerody.user.vo.statis;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author PengQiang
 * @ClassName UserSexStatisQueryVo
 * @DateTime 2023/5/3 15:13
 */
@Data
public class UserSexStatisQueryVo {

    public UserSexStatisQueryVo() {

    }

    public UserSexStatisQueryVo(String sex, Integer sexType) {
        this.sex = sex;
        this.sexType = sexType;
    }

    /**
     * 性别中文
     */
    private String sex;

    /**
     * 性别类型 (0.男、1.女、3.未知)
     */
    private Integer sexType;

    /**
     * 人数
     */
    private Integer number;

    /**
     * 占比
     */
    private BigDecimal rate = BigDecimal.ZERO;
}
