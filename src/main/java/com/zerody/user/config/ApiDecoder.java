package com.zerody.user.config;

import com.alibaba.fastjson.JSONObject;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.util.JsonUtils;
import com.zerody.common.util.RSAUtil;
import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;

@Slf4j
public class ApiDecoder extends SpringDecoder implements Decoder {
	public ApiDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
		super(messageConverters);
	}

	@Autowired
	ApiProperties properties;

	@Override
	public Object decode(final Response response, Type type) throws IOException, FeignException {
		InputStream is = response.body().asInputStream();
		List<String> lines = IOUtils.readLines(is, Charset.defaultCharset());
		String resultStr = StringUtils.join(lines);
		log.info("请求结果 :{}", resultStr);
		JSONObject resultObj = JSONObject.parseObject(resultStr);
		Integer code = resultObj.getObject("code", Integer.class);
		if (code == null || code != 0) {
			DataResult error = JsonUtils.json2Object(resultStr, DataResult.class);
			return error;
		}
		// 7.验签
		JSONObject datas = resultObj.getObject("data", JSONObject.class);
		String resultString = datas.getString("data");
		log.info("请求结果数据：" + resultString);
		String resultSign = datas.getString("sign");
		log.info("请求结果签名：" + resultSign);

		try {
			if (!RSAUtil.verify(resultString, properties.getTszPublickey(), resultSign)) {
				log.error("结果验签失败");
				DataResult rt = R.error("结果验签失败");
				return rt;
			} else {
				log.info("结果验签成功");
				return super.decode(response, type);
			}
		} catch (Exception e) {
			log.error("返回结果验签失败", e);
			return R.error("结果验签失败");
		}

	}

}