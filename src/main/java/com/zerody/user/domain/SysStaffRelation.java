package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author zhangpingping
 * @date 2021年09月09日 16:09
 */
@Data
public class SysStaffRelation {
    /**
     * 主键id
     */
    private String id;
    /**
     * 员工ID
     */
    private String staffId;

    /**
     * 员工名称
     */
    private String staffName;
    /**
     * 关系员工ID
     */
    private String relationStaffId;
    /**
     * 关系员工名称
     */
    private String relationStaffName;
    /**
     * 部门ID
     */
    private String departId;
    /**
     * 部门名称
     */
    private String departName;
    /**
     * 关系描述
     */
    @TableField("`describe`")
    private String describe;
    /**
     * 删除 0未 1已
     */
    private Integer deletd;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
