package com.zerody.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author DELL
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = { "com.zerody.**"})
@MapperScan("com.zerody.user.mapper")
@EnableCaching
@EnableFeignClients(basePackages= {"com.zerody.**"})
public class UserSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserSystemApplication.class, args);
	}
}
