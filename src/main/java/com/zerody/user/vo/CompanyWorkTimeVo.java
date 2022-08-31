package com.zerody.user.vo;

import lombok.Data;

import java.util.Date;

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
    private Integer companyId;
    /**
     * 上午上班时间
     */
    private String morningWorkTime;
    /**
     * 上午下班时间
     */
    private String morningAfterTime;
    /**
     * 下午上班时间
     */
    private String afternoonWorkTime;
    /**
     * 下午下班时间
     */
    private String afternoonAfterTime;
    /**
     * 创建时间
     */
    private Date createTime;

}
