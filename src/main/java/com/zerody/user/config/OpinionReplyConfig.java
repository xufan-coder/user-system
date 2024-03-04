package com.zerody.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Author : xufan
 * @create 2024/3/4 11:39
 */
@ConfigurationProperties(prefix="msg.opinion-reply")
@Component
@RefreshScope
@Data
public class OpinionReplyConfig {
    private String url;

    private String url2;

    private String query;

    private String arguments;

    private String title;

    private String title1;

    private String content;

    private String content1;
}
