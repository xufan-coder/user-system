package com.zerody.user.dto;

import lombok.Data;


/**
 * @author  DaBai
 * @date  2022/5/27 13:59
 */
@Data
public class FlowMessageDto {
	private String content;

	private String title;

	private String icon;
	/**
	 * 消息来源 task:签单待办，concact：未联系跟进，visit:待拜访客户，schedule:日程任务 extend:扩展类型
	 */
	private String messageSource;

	/**
	*   扩展推送类型
	*/
	/**
	*   跳转地址
	*/
	private String url;
	/**
	*   跳转参数
	*/
	private Object query;
	private Object arguments;

}
