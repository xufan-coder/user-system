package com.zerody.user.dto.statis;

import com.zerody.user.dto.bean.SetTimePeriodPage;
import io.swagger.models.auth.In;
import lombok.Data;

/**
 * @author PengQiang
 * @ClassName UserAgeStatisQueryDto
 * @DateTime 2023/5/3 10:11
 */
@Data
public class UserSexStatisQueryDto extends SetTimePeriodPage {

    /**
     * 是否仅统计签约中
     */
    private Boolean isSign;

    /**
     * 性别
     */
    private Integer sex;
}
