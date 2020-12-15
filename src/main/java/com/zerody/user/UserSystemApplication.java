package com.whzc.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableAutoConfiguration//启用自动配置
@EnableDiscoveryClient
@EnableEurekaClient
@ComponentScan(basePackages = { "com.zerody.user.**" })
@MapperScan("com.zerody.user.mapper")
@EnableCaching
public class UserSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserSystemApplication.class, args);
	}
}
