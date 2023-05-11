package com.zerody.user.dto.statis;

import com.zerody.user.dto.bean.SetTimePeriodPage;
import com.zerody.user.dto.bean.TimePeriodType;
import lombok.Data;

/**
 * @author PengQiang
 * @ClassName UserStatisQueryDto
 * @DateTime 2023/4/29 11:09
 */
@Data
public class UserStatisQueryDto  extends SetTimePeriodPage {

    /**
     * 是否仅统计签约中(true是，false否)
     */
    private Boolean isSign;

    /**
     * 性别(1男 0女)
     */
    private Integer sex;

    /**
     * 学历
     */
    private String highestEducation;

    /**
     * 离职原因类型id
     */
    private String type;

}
