package com.zerody.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix="msg.superior")
@Component
@RefreshScope
@Data
public class LoanSuperiorConfig {
    private String url;

    private String h5Url;

    private String query;

    private String arguments;

    private String content;

    private String title;
}
