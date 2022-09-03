package com.zerody.user.dto;

import com.zerody.user.domain.UnionCompanyWorkTime;
import lombok.Data;

import java.util.List;

/**
 * @author : chenKeFeng
 * @date : 2022/8/31 15:55
 */
@Data
public class CompanyWorkTimeAddDto {

    /**
     * 公司id
     */
    private String companyId;
    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 上班时间
     */
    private List<Integer> workingHours;

    /**
     * 打卡时间
     */
    private List<UnionCompanyWorkTimeDto> companyWorkTimes;

}
