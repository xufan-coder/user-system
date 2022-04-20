package com.zerody.user.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author HuangHuaSheng
 * @date 2019年9月10日
 *
 */
@Configuration
public class RestConfig {
	@ConditionalOnMissingBean(RestTemplate.class)
	@Primary
	@LoadBalanced
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	@Bean
	public RestTemplate restTemplateSimple() {
		return new RestTemplate();
	}
}
