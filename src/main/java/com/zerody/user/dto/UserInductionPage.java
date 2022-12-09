package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.user.dto.bean.SetTimePeriodPage;
import lombok.Data;

/**
 * @author kuang
 */
@Data
public class UserInductionPage extends SetTimePeriodPage {

    /**入职申请状态*/
    private String approveState;
    private String userId;

    private String departId;

    private String companyId;

}
