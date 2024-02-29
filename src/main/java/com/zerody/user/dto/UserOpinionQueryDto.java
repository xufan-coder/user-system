package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.user.dto.bean.SetTimePeriodPage;
import lombok.Data;

/**
 * @author kuang
 */
@Data
public class UserOpinionQueryDto extends SetTimePeriodPage {

    /**提交人名称*/
    private String searchName;

    /**用户id*/
    private String userId;

    /**分类id*/
    private String typeId;

    /**是否是boos账号*/
    private boolean isCEO;

    /** 处理进度(0 待处理 1 处理中 2 已完成) */
    private Integer state;

    /** 回复人类型 (1 直接回复人 0 协助回复人) */
    private Integer replyType;
}
