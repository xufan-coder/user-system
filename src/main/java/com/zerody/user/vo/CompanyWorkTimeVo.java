package com.zerody.user.vo;

import com.zerody.user.domain.CompanyWeek;
import com.zerody.user.domain.CompanyWorkTime;
import com.zerody.user.domain.UnionCompanyWorkTime;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author : chenKeFeng
 * @date : 2022/8/29 14:05
 */
@Data
public class CompanyWorkTimeVo {

    private String id;
    /**
     * 公司id
     */
    private String companyId;
    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 类型(1:用户、0:boss)
     */
    private Integer type;

    /**
     * 上班时间集合
     */
    private List<Integer> workingHours;

    /**
     * 企业打卡时间信息
     */
    private List<UnionCompanyWorkTime> companyWorkTimes;
}
