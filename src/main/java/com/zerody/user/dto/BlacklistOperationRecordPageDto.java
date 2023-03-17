package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import com.zerody.user.dto.bean.UserPositionPageParam;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author : xufan
 * @create 2023/3/8 17:23
 */

@Data
public class BlacklistOperationRecordPageDto extends UserPositionPageParam {

    /** 内控伙伴名称 */
    private String blackName;

    /** 操作人姓名 */
    private String createName;

    /** 操作类型（0：查询，1：编辑）*/
    private Integer type;

    /** 操作开始时间 */
    private Date startTime;

    /** 操作结束时间 */
    private Date endTime;

    /** 操作记录id*/
    private List<String> id;
}

