package com.zerody.user.vo;

import lombok.Data;

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
     * 内容
     */
    private String content;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 是否删除0 否1是
     */
    private Integer deleted;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;
}
