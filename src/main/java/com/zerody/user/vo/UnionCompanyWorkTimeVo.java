package com.zerody.user.vo;

import lombok.Data;

/**
 * @author : chenKeFeng
 * @date : 2022/8/31 15:27
 */
@Data
public class UnionCompanyWorkTimeVo {

    /**
     * 上班打卡时间
     */
    private String workPunchTime;
    /**
     * 下班打卡时间
     */
    private String downPunchTime;

}
