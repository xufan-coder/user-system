package com.zerody.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author : xufan
 * @create 2023/4/3 11:51
 */

@Data
public class PrepareExecutiveRecordDto {

    /** 用户id */
    private String userId;

    /** 是否预备高管 0表示否 1表示是 2表示退学*/
    private int isPrepareExecutive;

    /** 入学日期 */
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date enterDate;

    /** 退学日期 */
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date outDate;

    /** 退学原因 */
    private String outReason;
}
