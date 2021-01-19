package com.zerody.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;

import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 * @author
 * @description          DELL
 * @date                 2021/1/19 14:50
 * @param
 * @return
 */
@Slf4j
@RefreshScope
@Configuration
public class JobConfig {
	@Value("${xxl.job.admin.addresses}")
	String adminAddresses;
	@Value("${xxl.job.executor.appname}")
	String appname;
	@Value("${xxl.job.executor.ip:}")
	String ip;
	@Value("${xxl.job.executor.port:0}")
	int port;
	@Value("${xxl.job.accessToken:}")
	String accessToken;
	@Value("${xxl.job.executor.logpath:}")
	String logPath;
	@Value("${xxl.job.executor.logretentiondays:10}")
	int logRetentionDays;
	
	@Bean
	public XxlJobSpringExecutor xxlJobExecutor() {
	    log.info(">>>>>>>>>>> xxl-job config init.");
	    XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
	    xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
	    xxlJobSpringExecutor.setAppname(appname);
	    xxlJobSpringExecutor.setIp(ip);
	    xxlJobSpringExecutor.setPort(port);
	    xxlJobSpringExecutor.setAccessToken(accessToken);
	    xxlJobSpringExecutor.setLogPath(logPath);
	    xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
	    return xxlJobSpringExecutor;
	}
}
