package com.zerody.user.handler;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author  DaBai
 * @date  2022/12/10 11:56
 */
@Data
public class FlowMessage implements Serializable {


	/**
	 * 主键
	 */
	@ApiModelProperty(name = "id", value = "主键")
	private Long id;

	/**
	 * 接收人编号
	 */
	@ApiModelProperty(name = "receiverId", value = "接收人编号")
	private String receiverId;

	/**
	 * 接收人姓名
	 */
	@ApiModelProperty(name = "receiverName", value = "接收人姓名")
	private String receiverName;

	/**
	 * 过期时间
	 */
	@ApiModelProperty(name = "expireTime", value = "过期时间")
	private Date expireTime;

	/**
	 * 发送方式0:APP
	 */
	@ApiModelProperty(name = "sendType", value = "发送方式：1-WEB/APP、2-SMS、3-PHONE、4-EMAIL")
	private String sendType;

	/**
	 * 发送时间
	 */
	@TableField("send_time")
	@ApiModelProperty(name = "sendTime", value = "发送时间")
	private Date sendTime;

	/**
	 * 状态
	 */
	@TableField("status")
	@ApiModelProperty(name = "status", value = "状态：1-正常，2-不正常")
	private Integer status;

	/**
	 * 是否告警消息
	 */
	@ApiModelProperty(name = "isAlert", value = "是否告警消息：1-警告，2-不警告")
	private Integer isAlert;

	/**
	 * 标题
	 */
	@ApiModelProperty(name = "title", value = "标题")
	private String title;

	@ApiModelProperty(name = "content", value = "")
	private String content;

	/**
	 * 模板类型ID
	 */
	@ApiModelProperty(name = "templateTypeId", value = "模板类型ID")
	private String templateTypeId;

	/**
	 * 链接URL
	 */
	@TableField("link_url")
	@ApiModelProperty(name = "linkUrl", value = "链接URL")
	private String linkUrl;

	/**
	 * 消息类型(1：提醒、2：待办、3：抄送）
	 */
	@ApiModelProperty(name = "msgType", value = "消息类型")
	private Integer msgType;

	@ApiModelProperty(name = "processInstanceId", value = "流程实例id")
	private String processInstanceId;
	private String rootProcessInstanceId;

	@ApiModelProperty(name = "flowState", value = "流程状态）")
	private Integer flowState;
	private String processDefKey;
	private String taskId;
	private String taskKey;
	private Integer isRead;
	// 是否过期
	private Integer expired;
	private String icon;
	private String viewUrl;
	private String formUrl;
	private String rootProcessDefKey;
	/**
	 * 消息来源 task:签单待办，concact：未联系跟进，visit:待拜访客户，schedule:日程任务
	 */
	private String messageSource;




}
