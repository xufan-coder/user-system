package com.zerody.user.dto.statis;

import com.zerody.user.dto.bean.SetTimePeriodPage;
import lombok.Data;

/**
 * @author PengQiang
 * @ClassName UserAgeStatisQueryDto
 * @DateTime 2023/5/3 10:11
 */
@Data
public class UserAgeStatisQueryDto extends SetTimePeriodPage {

    /**
     * 是否仅统计签约中
     */
    private Boolean isSign;
}
