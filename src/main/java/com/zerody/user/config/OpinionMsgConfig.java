package com.zerody.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author kuang
 * @ClassName ConsultingMsgConfig
 * @DateTime 2022/07/12
 */
@ConfigurationProperties(prefix="msg.opinion")
@Component
@RefreshScope
@Data
public class OpinionMsgConfig {

    private String url;

    private String query;

    private String arguments;

    private String title;

    private String title2;

    private String content;
    private String content2;
}
