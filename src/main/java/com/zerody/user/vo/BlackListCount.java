package com.zerody.user.vo;

import lombok.Data;

import java.util.List;

/**
 * @author  DaBai
 * @date  2022/10/10 15:27
 */


@Data
public class BlackListCount {

    /**
    *   企业名称
    */
    private String companyName;

    /**
    *   数量
    */
    private Integer number;

    public BlackListCount() {
        super();
    }

    public BlackListCount(String companyName, Integer number) {
        this.companyName = companyName;
        this.number = number;
    }
}
