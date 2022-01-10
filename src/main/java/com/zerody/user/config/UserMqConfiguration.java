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

	/** 部门修改同步路由 */
	private static final String DEPT_EDIT_SYNCHRONIZATION_ROUTE = "zerody.dept-synchronization.edit#";

	/** 企业修改同步路由 */
	private static final String COMPAY_EDIT_SYNCHRONIZATION_ROUTE = "zerody.company-synchronization.edit#";

	/** 用户修改同步路由 */
	private static final String USER_EDIT_SYNCHRONIZATION_ROUTE = "zerody.user-synchronization.edit#";


	@Bean
	public Queue userNameMobile() {
		return new Queue(MQ.QUEUE_CARD_MOBILE, true);
	}


	@Bean
	public Queue companyCustomerEdit() {
		return new Queue(MQ.QUEUE_COMPANY_EDIT_CUSTOMER, true);
	}

	@Bean
	public Queue deptCustomerEdit() {
		return new Queue(MQ.QUEUE_DEPT_EDIT_CUSTOMER, true);
	}

	@Bean
	public Queue userCustomerEdit() {
		return new Queue(MQ.QUEUE_USER_EDIT_CUSTOMER, true);
	}


	@Bean
	public Queue companyContractEdit() {
		return new Queue(MQ.QUEUE_COMPANY_EDIT_CONTRACT, true);
	}

	@Bean
	public Queue deptContractEdit() {
		return new Queue(MQ.QUEUE_DEPT_EDIT_CONTRACT, true);
	}

	@Bean
	public Queue userContractEdit() {
		return new Queue(MQ.QUEUE_USER_EDIT_CONTRACT, true);
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
	Binding bindingUserEditCustomerQueue() {
		return BindingBuilder.bind(userCustomerEdit()).to(exchange()).with(USER_EDIT_SYNCHRONIZATION_ROUTE);
	}
	@Bean
	Binding bindingCompanyEditCustomerQueue() {
		return BindingBuilder.bind(companyCustomerEdit()).to(exchange()).with(COMPAY_EDIT_SYNCHRONIZATION_ROUTE);
	}
	@Bean
	Binding bindingDeptCustomerQueue() {
		return BindingBuilder.bind(deptCustomerEdit()).to(exchange()).with(DEPT_EDIT_SYNCHRONIZATION_ROUTE);
	}

	@Bean
	Binding bindingUserEditContractQueue() {
		return BindingBuilder.bind(userContractEdit()).to(exchange()).with(USER_EDIT_SYNCHRONIZATION_ROUTE);
	}
	@Bean
	Binding bindingCompanyEditContractQueue() {
		return BindingBuilder.bind(companyContractEdit()).to(exchange()).with(COMPAY_EDIT_SYNCHRONIZATION_ROUTE);
	}
	@Bean
	Binding bindingDeptContractQueue() {
		return BindingBuilder.bind(deptContractEdit()).to(exchange()).with(DEPT_EDIT_SYNCHRONIZATION_ROUTE);
	}


	@Bean
	public Queue staffDimissionQueue() {
		return new Queue(MQ.QUEUE_STAFF_DIMISSION, true);
	}
	@Bean
	Binding bindingStaffDimissionQueue() {
		return BindingBuilder.bind(staffDimissionQueue()).to(exchange()).with(MQ.QUEUE_STAFF_DIMISSION);
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
