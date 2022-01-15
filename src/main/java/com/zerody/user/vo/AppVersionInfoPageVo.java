package com.zerody.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author zhangpingping
 * @date 2021年12月16日 15:32
 */
@Data
public class AppVersionInfoPageVo {
    /***/
    private String id;
    /**
     * 标题
     */
    private String title;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 排序
     */
    private Integer orderNum;
}
