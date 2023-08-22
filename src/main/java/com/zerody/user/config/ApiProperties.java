package com.zerody.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author huanghuasheng
 * @create_time 2021年5月8日
 */
@ConfigurationProperties("supplier.tsz")
@Component
@Data
public class ApiProperties {
	/**
	 * 客户ID
	 */
	String clientId;
	/* 客户密码 */
	String clientSecret;
	/* 客户公钥 */
	String publicKey;
	/* 客户私钥 */
	String privateKey;
	/* 唐三藏公钥 */
	String tszPublickey;


}
