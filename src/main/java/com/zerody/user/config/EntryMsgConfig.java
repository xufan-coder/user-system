package com.zerody.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Author: YeChangWei
 * @Date: 2022/11/10 10:35
 */
@ConfigurationProperties(prefix="msg.entry")
@Component
@RefreshScope
@Data
public class EntryMsgConfig {
    /**
     * 跳转路径
     */
    private String url;
    /**
     * 路径参数
     */
    private String query;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容参数
     */
    private String arguments;
    /**
     * 内容
     */
    private String content;

    private String content2;

}
