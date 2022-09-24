package com.zerody.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author PengQiang
 * @ClassName ConsultingMsgConfig
 * @DateTime 2022/6/7_11:00
 * @Deacription TODO
 */
@ConfigurationProperties(prefix="msg.birthday")
@Component
@RefreshScope
@Data
public class BirthdayMsgConfig {

    private String url;

    /**生日推送路径*/
    private String birthdayUrl;

    private String query;

    private String arguments;

    private String title;

    private String title2;

    private String content;
    private String content2;
}
