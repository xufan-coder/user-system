package com.zerody.user.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author yumiaoxia
 * @since 2021-06-29
 */
@Configuration
@ConditionalOnBean(Docket.class)
public class SwaggerApiConfig {

    public static final String APP_VERSION = "APP VERSION";

    public SwaggerApiConfig(Docket docket){
        docket.tags(
                new Tag(APP_VERSION, "APP版本管理")
        );
    }
}
