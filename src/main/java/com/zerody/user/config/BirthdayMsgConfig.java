package com.zerody.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 生日推送配置类
 *
 * @author PengQiang
 * @ClassName ConsultingMsgConfig
 * @DateTime 2022/6/7_11:00
 * @Deacription TODO
 */
@ConfigurationProperties(prefix = "msg.birthday")
@Component
@RefreshScope
@Data
public class BirthdayMsgConfig {

    /**
     * 跳转路径
     */
    private String url;

    /**
     * 生日推送路径
     */
    private String birthdayUrl;

    /**
     * 路径参数
     */
    private String query;

    /**
     * 内容参数
     */
    private String arguments;

    /**
     * 标题1
     */
    private String title;

    /**
     * 标题1
     */
    private String title2;

    /**
     * 内容1
     */
    private String content;

    /**
     * 内容2
     */
    private String content2;

}
