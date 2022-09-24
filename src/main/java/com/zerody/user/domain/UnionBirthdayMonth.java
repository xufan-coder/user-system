package com.zerody.user.domain;

import lombok.Data;

import java.util.Date;

/**
 *
 * @author               kuang
 * @description          伙伴生日模板关联月份
 * @date                 2022/8/20 14:17
 */
@Data
public class UnionBirthdayMonth {
    private String id;

    /**生日模板id*/
    private String templateId;

    /**月份*/
    private Integer month;

    /**创建时间**/
    private Date createTime;

    /**创建人ID*/
    private String createBy;

    /**创建人名称*/
    private String createUsername;

}