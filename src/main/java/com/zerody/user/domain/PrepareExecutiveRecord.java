package com.zerody.user.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author : xufan
 * @create 2023/4/3 11:35
 */
@Data
public class PrepareExecutiveRecord {

    private String id;

    /** 用户id */
    private String userId;

    /** 用户名称 */
    private String userName;

    /** 所属公司id */
    private String companyId;

    /** 所属公司 */
    private String companyName;

    /** 所属角色id */
    private String roleId;

    /** 所属角色 */
    private String roleName;

    /** 入学日期 */
    private Date enterDate;

    /** 退学日期 */
    private Date outDate;

    /** 退学原因 */
    private String outReason;

    /** 创建人id*/
    private String createId;

    /** 创建人*/
    private String createName;

    /** 创建人时间*/
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /** 是否预备高管 0表示否 1表示是 2表示退学*/
    private Integer isPrepareExecutive;
}
