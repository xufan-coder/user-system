package com.zerody.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Author : xufan
 * @create 2024/3/15 18:13
 */
@ConfigurationProperties(prefix="msg.opinion-complete")
@Component
@RefreshScope
@Data
public class OpinionCompleteConfig {
    private String url;

    private String query;

    private String arguments;

    private String title;

    private String title1;

    private String content;

    private String content1;
}
