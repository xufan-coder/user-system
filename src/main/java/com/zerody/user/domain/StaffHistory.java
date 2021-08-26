package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author zhangpingping
 * @date 2021年08月25日 16:53
 */
@Data
public class StaffHistory {
    /**
     * ID
     */
    //主键id
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 描述
     */
    @TableField("`describe`")
    private String describe;
    /**
     * 员工ID
     */
    private String staffId;
    /**
     * 类型
     */
    private String type;
    /**
     * 时间
     */
    private Date time;
    /**
     * 创建时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
