package com.zerody.user.vo.statis;

import lombok.Data;

/**
 * @author PengQiang
 * @ClassName UserTrendQueryVo
 * @DateTime 2023/4/29 11:13
 */
@Data
public class UserTrendQueryVo {

    /**
     * 日期key
     */
    private String key;

    /**
     * 签约数量
     */
    private Integer signNum;

    /**
     * 解约数量
     */
    private Integer unSignNum;
}
