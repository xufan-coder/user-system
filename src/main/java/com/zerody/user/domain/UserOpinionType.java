package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 问题反馈类型表
 *
 * @ClassName CourseColumn
 * @author PengQiang
 * @DateTime 2022/7/8_16:14
 * @Deacription TODO
 */

/**
 * @author kuang
 */
@Data
public class UserOpinionType {

    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 删除状态: 0-未删除 1-已删除
     */
    private Integer deleted;

    /**
     * 创建人ID
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人ID
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

}