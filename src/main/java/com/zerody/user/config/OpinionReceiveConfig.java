package com.zerody.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Author : xufan
 * @create 2024/2/27 17:53
 */
@ConfigurationProperties(prefix="msg.opinion-receive")
@Component
@RefreshScope
@Data
public class OpinionReceiveConfig {

    private String url;

    private String url2;

    private String query;

    private String arguments;

    private String title;

    private String title1;

    private String content;

    private String content1;
}
