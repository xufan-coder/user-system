package com.zerody.user.dto;

import lombok.Data;

/**
 * @author : chenKeFeng
 * @date : 2022/8/31 16:00
 */
@Data
public class UnionCompanyWorkTimeDto {

    /**
     * 公司id
     */
    private String companyId;
    /**
     * 上班打卡时间
     */
    private String workPunchTime;
    /**
     * 下班打卡时间
     */
    private String downPunchTime;

}
