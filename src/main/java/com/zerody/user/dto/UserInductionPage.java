package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import lombok.Data;

/**
 * @author kuang
 */
@Data
public class UserInductionPage extends PageQueryDto {

    /**入职申请状态*/
    private String approveState;

    /**开始时间*/
    private String  startTime;

    /**结束时间*/
    private String endTime;

    private String userId;

    private String departId;

    private String companyId;

}
