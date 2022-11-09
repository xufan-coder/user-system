package com.zerody.user.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
    * 呼叫控制配置
    */
@Data
public class CallControl {
    /**
    * id
    */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
    * 企业id
    */
    private String companyId;

    /**
    * 星期几
    */
    private Integer week;

    /**
    * 开始时
    */
    private Integer start;

    /**
    * 结束时
    */
    private Integer end;

    /**
    * 是否启用 1 0
    */
    private Integer enable;

    /**
    * 预警次数
    */
    private Integer tipNum;

    /**
    * 限制最大呼叫次数
    */
    private Integer callNum;

    /**
    * 修改时间
    */
    private Date updateTime;
}