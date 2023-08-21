package com.zerody.user.config;

import com.zerody.common.util.JsonUtils;
import com.zerody.common.util.RSAUtil;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;

import java.lang.reflect.Type;
import java.util.UUID;

@Slf4j
public class ApiEncoder extends SpringEncoder implements Encoder {
	@Autowired
	ApiProperties properties;
	public ApiEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
		super(messageConverters);
	}

	@Override
	public void encode(Object object, Type bodyType, RequestTemplate template) {
		// 随机数
		String nonce = UUID.randomUUID().toString().substring(0, 8);
		//时间戳
		long timestamp= System.currentTimeMillis();
		template.header("clientId", properties.getClientId());
		template.header("nonce", nonce);
		template.header("timestamp", timestamp + "");

		// 2. 参数对象转 json字符串明文
		String paramJson = JsonUtils.toString(object);
		String signStr = "body=" + paramJson + "&nonce=" + nonce + "&timestamp=" + timestamp + "&clientId="
				+ properties.getClientId() + "&clientSecret=" + properties.getClientSecret();
		log.info("待签名串：" + signStr);
		String sign = null;
		try {
			log.debug("PrivateKey():{}", properties.getPrivateKey());
			sign = RSAUtil.sign(signStr, properties.getPrivateKey());
		} catch (Exception e) {
			log.error("签名异常", e);
			throw new EncodeException(e.getMessage());
		}
		log.info("签名结果：" + sign);
		template.header("sign", sign);
		super.encode(object, bodyType, template);
	}
}
