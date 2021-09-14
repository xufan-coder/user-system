package com.zerody.user.dto;

import com.zerody.user.dto.bean.UserPositionParam;
import lombok.Data;

import java.util.Date;

/**
 * @author zhangpingping
 * @date 2021年09月11日 16:08
 */

@Data
public class PerformanceInfoDto extends UserPositionParam {

    /**
     * 月份
     */
    private String month;

    /**
     * 回款状态
     */
    private String paymentState;

    private Date beginTime;

    private Date endTime;
}
