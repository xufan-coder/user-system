package com.zerody.user.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class PositionRecordDto {
    /**
     * 任职记录id
     */
    @TableId
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 公司id
     */
    private String companyId;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 任职时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date positionTime;
    /**
     * 离职时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date quitTime;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 离职原因
     */
    private String quitReason;

    /** 创建人id*/
    private String createBy;

    /** 创建人*/
    private String createName;

    /** 创建人时间*/
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
}
