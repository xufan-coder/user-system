package com.zerody.user.config;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zerody.common.constant.MQ;

//import com.zerody.flow.api.event.FlowEventMq;

/**
 *
 * @author HuangHuaSheng
 * @date 2019年5月30日
 *
 */
@Configuration
public class UserMqConfiguration {

	/** 部门名称修改路由 */
	private static final String DEPT_NAME_ROUTE = "zerody.dept-name.#";

	/** 企业名称修改路由 */
	private static final String COMPAY_NAME_ROUTE = "zerody.company-name.#";

	/** 用户名称修改路由 */
	private static final String USER_NAME_ROUTE = "zerody.user-name.#";


	@Bean
	public Queue userNameMobile() {
		return new Queue(MQ.QUEUE_CARD_MOBILE, true);
	}


	@Bean
	public Queue companyEdit() {
		return new Queue(MQ.QUEUE_COMPANY_EDIT, true);
	}

	@Bean
	public Queue deptEdit() {
		return new Queue(MQ.QUEUE_DEPT_EDIT, true);
	}

	@Bean
	public Queue userEdit() {
		return new Queue(MQ.QUEUE_USER_EDIT, true);
	}

	// 交换机 起名：TestDirectExchange
	@Bean
	TopicExchange exchange() {
		return new TopicExchange(MQ.TOPIC_ZERODY);
	}
	@Bean
	Binding bindingUserMobileQueue() {
		return BindingBuilder.bind(userNameMobile()).to(exchange()).with(MQ.QUEUE_CARD_MOBILE);
	}

	@Bean
	Binding bindingUserEditQueue() {
		return BindingBuilder.bind(userEdit()).to(exchange()).with(MQ.QUEUE_USER_EDIT);
	}	@Bean
	Binding bindingCompanyEditQueue() {
		return BindingBuilder.bind(companyEdit()).to(exchange()).with(MQ.QUEUE_COMPANY_EDIT);
	}	@Bean
	Binding bindingDeptQueue() {
		return BindingBuilder.bind(deptEdit()).to(exchange()).with(MQ.QUEUE_DEPT_EDIT);
	}
	/** ==============================================================================================================  */


	@Bean
	public Queue companyNameQueueContract() {
		return new Queue(MQ.QUEUE_COMPANY_NAME_CONTRACT, true);
	}

	@Bean
	public Queue companyNameQueueCustomer() {
		return new Queue(MQ.QUEUE_COMPANY_NAME_CUSTOMER, true);
	}

	@Bean
	public Queue queueDeptNameContract() {
		//		Map<String, Object> arguments = new HashMap<>();
		//		arguments.put("x-dead-letter-exchange", MQ.TOPIC_ZERODY + MQ.DEAD_SUFFIX);
		//		arguments.put("x-dead-letter-routing-key", MQ.QUEUE_CARD_MOBILE + MQ.DEAD_SUFFIX);
		//		return new Queue(MQ.QUEUE_CARD_MOBILE, true, false, false, arguments);
		return new Queue(MQ.QUEUE_DEPT_NAME_CONTRACT, true);
	}


	@Bean
	public Queue queueDeptNameCustomer() {
		return new Queue(MQ.QUEUE_DEPT_NAME_CUSTOMER, true);
	}

	@Bean
	public Queue userNameQueueContract() {
		return new Queue(MQ.QUEUE_USER_NAME_CONTRACT, true);
	}

	@Bean
	public Queue userNameQueueCustomer() {
		return new Queue(MQ.QUEUE_USER_NAME_CUSTOMER, true);
	}




	@Bean
	Binding bindingUserNameQueueContract() {
		return BindingBuilder.bind(userNameQueueContract()).to(exchange()).with(USER_NAME_ROUTE);
	}

	@Bean
	Binding bindingUserNameQueueCustomer() {
		return BindingBuilder.bind(userNameQueueCustomer()).to(exchange()).with(USER_NAME_ROUTE);
	}

	@Bean
	Binding bindingDeptNameCustomer() {
		return BindingBuilder.bind(queueDeptNameCustomer()).to(exchange()).with(DEPT_NAME_ROUTE);
	}

	@Bean
	Binding bindingDeptNameContract() {
		return BindingBuilder.bind(queueDeptNameContract()).to(exchange()).with(DEPT_NAME_ROUTE);
	}

	@Bean
	Binding bindingCompanyNameQueueContract() {
		return BindingBuilder.bind(companyNameQueueContract()).to(exchange()).with(COMPAY_NAME_ROUTE);
	}

	@Bean
	Binding bindingCompanyNameQueueCustomer() {
		return BindingBuilder.bind(companyNameQueueCustomer()).to(exchange()).with(COMPAY_NAME_ROUTE);
	}

	/** 扇形交换机 */
	@Bean
	FanoutExchange exchangeFanout() {
		return new FanoutExchange(MQ.FANOUT_ZERODY);
	}
}
