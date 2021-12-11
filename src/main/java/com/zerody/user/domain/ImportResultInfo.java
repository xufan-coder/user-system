package com.zerody.user.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ImportResultInfo {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 导入id
     */
    private String importId;

    /**
     * 导入内容
     */
    private String importContent;

    /**
     * 导入类型(1.伙伴黑名单-外部)
     */
    private Integer type;

    /**
     * 导入状态(0.成功、1.失败)
     */
    private Integer state;

    /**
     * 导入失败原因
     */
    private String errorCause;

    /**
     * 创建时间&导入时间
     */
    private Date createTime;

    /**
     * 用户id
     */
    private String userId;
}

