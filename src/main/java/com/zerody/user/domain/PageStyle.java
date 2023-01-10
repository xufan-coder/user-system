package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @Author: YeChangWei
 * @Date: 2022/12/30 9:37
 * app页面风格管理表
 */
@Data
public class PageStyle {
    /**
     * id
     */
    @TableId(type = IdType.UUID)
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
    private Date startTime;
    /**
     * 失效时间
     */
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
    private Integer deleted;
    /**
     * 风格类型
     */
    private String type;
}
