package com.zerody.user.handler;

import com.zerody.common.utils.DataUtil;
import com.zerody.flow.api.event.FlowData;
import com.zerody.flow.api.event.FlowEventData;
import com.zerody.flow.api.state.FlowState;
import com.zerody.flow.client.util.JsonUtils;
import com.zerody.im.api.dto.SendRobotMessageDto;
import com.zerody.im.feign.SendMsgFeignService;
import com.zerody.im.util.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * IM流程通知
 * @author  DaBai
 * @date  2022/9/5 16:32
 */


@Component
public class ImMessageHandler {
	//申请对象： ${userName} 签约时间：  ${signTime}  角色：  ${signRole}
	final static String inductionTip="申请对象： %s </br> 签约时间：  %s </br>  角色：  %s ";

	@Autowired
    SendMsgFeignService sendMsgFeignService;
	Logger log = LoggerFactory.getLogger(ImMessageHandler.class);

	public void handle(FlowEventData eventData) {
		log.info("OA通知开始推送IM-FlowEventData={}",JsonUtils.toString(eventData));
		//流程key
		String processDefKey = eventData.getProcessDefKey();
		String processInstId = eventData.getProcessInstId();
		//流程状态
		FlowState flowState = eventData.getFlowData().getFlowState();
		//流程表变量
		Map<String, Object> variables = eventData.getVariables();
		try {
			if ("Induction".equals(processDefKey)) {
				String tip =null;
				tip=String.format(inductionTip,
						variables.get("userName").toString(),
						variables.get("signTime").toString(),
						variables.get("roleName").toString());
				sendImMessage(tip,eventData);
			}
		} catch (Exception e) {
			log.error("推送IM出错:{}", e, e);
		}
	}

	void sendImMessage(String tip, FlowEventData eventData){
		FlowData flowData = eventData.getFlowData();
		FlowState flowState = eventData.getFlowData().getFlowState();
		String startUserName = eventData.getVariables().get("start_user_name") != null
				? eventData.getVariables().get("start_user_name").toString()
				: "";
		if(DataUtil.isNotEmpty(tip)) {
			FlowMessage msg = new FlowMessage();
			msg.setRootProcessInstanceId(flowData.getProcessInstId());
			msg.setRootProcessDefKey(flowData.getProcessDefKey());
			msg.setProcessDefKey(flowData.getProcessDefKey());
			msg.setProcessInstanceId(flowData.getProcessInstId());
			msg.setTitle(eventData.getFlowData().getProcessName());
			msg.setMsgType(1);
			msg.setFlowState(flowState.getState());
			msg.setContent(tip);
			msg.setReceiverId(flowData.getStarter());
			msg.setReceiverName(startUserName);

			SendRobotMessageDto data = new SendRobotMessageDto();
			data.setSender(IM.ROBOT_XIAOZANG);
			data.setContent(msg.getContent());
			data.setTarget(msg.getReceiverId());
			data.setContentPush(msg.getContent());
			data.setContentExtra(JsonUtils.toString(msg));
			data.setType(1010);
			com.zerody.common.api.bean.DataResult<Long> result = this.sendMsgFeignService.send(data);
			log.info("FlowEventData={},推送IM结果:{}",
					JsonUtils.toString(eventData),
					JsonUtils.toString(result));
		}else {
			log.info("无推送内容！flow{}",JsonUtils.toString(eventData));
		}
	}
}
