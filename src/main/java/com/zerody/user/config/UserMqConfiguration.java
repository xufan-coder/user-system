package com.zerody.user.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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


	@Bean
	public Queue queue() {
//		Map<String, Object> arguments = new HashMap<>();
//		arguments.put("x-dead-letter-exchange", MQ.TOPIC_ZERODY + MQ.DEAD_SUFFIX);
//		arguments.put("x-dead-letter-routing-key", MQ.QUEUE_CARD_MOBILE + MQ.DEAD_SUFFIX);
//		return new Queue(MQ.QUEUE_CARD_MOBILE, true, false, false, arguments);
		return new Queue(MQ.QUEUE_DEPT_NAME, true);
	}

	// 交换机 起名：TestDirectExchange
	@Bean
	TopicExchange exchange() {
		return new TopicExchange(MQ.TOPIC_ZERODY);
	}
//
	// 绑定 将队列和交换机绑定, 并设置用于匹配键
	@Bean
	Binding bindingDirect() {
		return BindingBuilder.bind(queue()).to(exchange()).with(MQ.QUEUE_DEPT_NAME);
	}
}
