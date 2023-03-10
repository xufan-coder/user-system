package com.zerody.user.dto;

import com.zerody.common.api.bean.PageQueryDto;
import lombok.Data;

import java.util.Date;

/**
 * @Author : xufan
 * @create 2023/3/8 17:23
 */

@Data
public class BlacklistOperationRecordPageDto extends PageQueryDto {

    /**公司id*/
    private String companyId;

    /** 部门id */
    private String deptId;

    /** 内控伙伴名称 */
    private String blackName;

    /** 操作人姓名 */
    private String createName;

    /** 操作类型（0：查询，1：编辑）*/
    private Integer type;

    /** 操作开始时间 */
    private String startTime;

    /** 操作结束时间 */
    private String endTime;
}

