package com.zerody.user.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author PengQiang
 * @ClassName MybtaisPlusConfig
 * @DateTime 2020/12/21_11:29
 * @Deacription TODO
 */
@Configuration
public class MybtaisPlusConfig {

    /**
     *   mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
