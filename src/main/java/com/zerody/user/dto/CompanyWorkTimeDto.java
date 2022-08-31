package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import lombok.Data;

import java.util.Date;

/**
 * @author : chenKeFeng
 * @date : 2022/8/29 14:03
 */
@Data
public class CompanyWorkTimeDto extends PageQueryDto {

    /**
     * id
     */
    private String id;
    /**
     * 公司id
     */
    private String companyId;
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

}
