package com.zerody.user.vo;



import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: YeChangWei
 * @Date: 2022/12/30 11:51
 */
@Data
public class PageStyleVo {

    private String id;
    /**
     * 页面样式名称
     */
    private String name;
    /**
     * 图片路径
     */
    private String pictureUrl;
    /**
     * 生效时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 失效时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 状态（0停用 1启用）
     */
    private Integer state;
    /**
     * 删除状态（0未删除 1已删除）
     */
    private Integer delete;
}
