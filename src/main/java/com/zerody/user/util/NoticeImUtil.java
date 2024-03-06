package com.zerody.user.util;

import com.alibaba.fastjson.JSONObject;
import com.zerody.common.api.bean.DataResult;
import com.zerody.expression.Expression;
import com.zerody.im.api.dto.SendHighMessageButton;
import com.zerody.im.api.dto.SendRobotMessageDto;
import com.zerody.im.constant.MsgType;
import com.zerody.im.feign.SendMsgFeignService;
import com.zerody.im.util.IM;
import com.zerody.user.config.OpinionAssistantConfig;
import com.zerody.user.config.OpinionReceiveConfig;
import com.zerody.user.config.OpinionReplyConfig;
import com.zerody.user.domain.UserOpinion;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author : xufan
 * @create 2024/2/27 18:10
 */

@Slf4j
@Component
public class NoticeImUtil {
    @Autowired
    private OpinionReceiveConfig opinionReceiveConfig;
    @Autowired
    private OpinionReplyConfig opinionReplyConfig;
    @Autowired
    private OpinionAssistantConfig opinionAssistantConfig;

    @Autowired
    private SendMsgFeignService sendMsgFeignService;

    private static OpinionReceiveConfig opinionReceiveConfigStatic;
    private static OpinionReplyConfig opinionReplyConfigStatic;
    private static OpinionAssistantConfig opinionAssistantConfigStatic;

    private static SendMsgFeignService sendMsgFeignServiceStatic;

    @PostConstruct
    public void init(){
        sendMsgFeignServiceStatic = sendMsgFeignService;
        opinionReceiveConfigStatic = opinionReceiveConfig;
        opinionReplyConfigStatic = opinionReplyConfig;
        opinionAssistantConfigStatic = opinionAssistantConfig;
    }

    /**
    * @Description:         意见接收人收到该消息
    * @Param:               [opinionId 意见id, targetUserId 接收人UserId, opinionSenderInfo 意见发送人信息, content 内容, isCeo 是否是投递给boss信箱]
    */
   public static Long pushOpinionToDirect(String opinionId, String targetUserId, String opinionSenderInfo, String content, Boolean isCeo){
        try {
            String msg = "";
            String arguments = "";
            if (isCeo){
                // 消息内容
                msg =  String.format(opinionReceiveConfigStatic.getContent(), opinionSenderInfo, content);
            }else {
                msg = String.format(opinionReceiveConfigStatic.getContent1(),opinionSenderInfo);
            }
            String query = String.format(opinionReceiveConfigStatic.getQuery(), opinionId);
            Object parse = JSONObject.parse(query);
            Object parse1 = JSONObject.parse(arguments);

            List<SendHighMessageButton> buttons = new ArrayList<>();
            SendHighMessageButton sendHighMessageButton = new SendHighMessageButton();
            sendHighMessageButton.setName("查看详情");
            sendHighMessageButton.setUrl(opinionReceiveConfigStatic.getUrl());
            sendHighMessageButton.setQuery(parse);
            sendHighMessageButton.setArguments(parse1);
            sendHighMessageButton.setMessageSource("extend");

            SendHighMessageButton sendHighMessageButton2 = new SendHighMessageButton();
            sendHighMessageButton2.setName("分配他人回复");
            sendHighMessageButton2.setUrl(opinionReceiveConfigStatic.getUrl2());
            sendHighMessageButton2.setQuery(parse);
            sendHighMessageButton2.setArguments(parse1);
            sendHighMessageButton2.setMessageSource("extend");
            buttons.add(sendHighMessageButton);
            buttons.add(sendHighMessageButton2);

            Map<String,Object> dataMap = new HashMap<>();
            if (isCeo){
                dataMap.put("title", opinionReceiveConfigStatic.getTitle());
            }else {
                dataMap.put("title", opinionReceiveConfigStatic.getTitle1());
            }
            dataMap.put("content",msg);
            dataMap.put("buttons",buttons);

            SendRobotMessageDto data = new SendRobotMessageDto();
            data.setContent(msg);
            data.setSender(IM.ROBOT_XIAOZANG);
            data.setTarget(targetUserId);
            data.setConversationType(0);
            data.setPersistFlag(3);
            data.setContentExtra(JSONObject.toJSONString(dataMap));
            data.setType(1014);
            DataResult<Long> imResult = sendMsgFeignServiceStatic.send(data);
            log.info("意见查看提醒推送IM结果:{}-----------{}", JSONObject.toJSONString(data),JSONObject.toJSONString(imResult));
            return imResult.getData();
        } catch (Exception e) {
            log.error("推送意见查看提醒IM出错:{}", e, e);
        }
        return null;
    }

    /**
    * @Description:         意见协助人收到消息
    * @Param:               [receiveUserId 信息接收人id , opinionSenderInfo 意见发起人信息 , content 消息内容, appointerInfo 指派人信息 , isCeo 是否是投递给boss信箱]
    */
    public static Long pushOpinionToAssistant(String opinionId,String receiveUserId, String opinionSenderInfo, String content,String appointerInfo, Boolean isCeo){
        try {
            String msg = "";
            String arguments = "";
            if (isCeo){
                // 消息内容
                msg =  String.format(opinionAssistantConfigStatic.getContent(), opinionSenderInfo, content);
            }else {
                msg = String.format(opinionAssistantConfigStatic.getContent1(), opinionSenderInfo, appointerInfo);
            }
            String query = String.format(opinionAssistantConfigStatic.getQuery(), opinionId);
            Object parse = JSONObject.parse(query);
            Object parse1 = JSONObject.parse(arguments);

            List<SendHighMessageButton> buttons = new ArrayList<>();
            SendHighMessageButton sendHighMessageButton = new SendHighMessageButton();
            sendHighMessageButton.setName("查看详情");
            sendHighMessageButton.setUrl(opinionAssistantConfigStatic.getUrl());
            sendHighMessageButton.setQuery(parse);
            sendHighMessageButton.setArguments(parse1);
            sendHighMessageButton.setMessageSource("extend");
            buttons.add(sendHighMessageButton);

            Map<String,Object> dataMap = new HashMap<>();
            if (isCeo){
                dataMap.put("title", opinionAssistantConfigStatic.getTitle());
            }else {
                dataMap.put("title", opinionAssistantConfigStatic.getTitle1());
            }
            dataMap.put("content",msg);
            dataMap.put("buttons",buttons);

            SendRobotMessageDto data = new SendRobotMessageDto();
            data.setContent(msg);
            data.setSender(IM.ROBOT_XIAOZANG);
            data.setTarget(receiveUserId);
            data.setConversationType(0);
            data.setPersistFlag(3);
            data.setContentExtra(JSONObject.toJSONString(dataMap));
            data.setType(1014);
            DataResult<Long> imResult = sendMsgFeignServiceStatic.send(data);
            log.info("接收意见协助回复查看提醒推送IM结果:{}-----------{}", JSONObject.toJSONString(data),JSONObject.toJSONString(imResult));

            return imResult.getData();
        } catch (Exception e) {
            log.error("接收意见协助回复查看提醒IM出错:{}", e, e);
        }
        return null;
    }

    /**
    * @Description:         一旦有回复，意见发起人收到该通知
    * @Param:               [receiveUserId 信息接收人id , receiveUserName 意见直接接收人的信息 , content 消息内容 , isCeo 是否是投递给boss信箱]
    */
    public static Long pushReplyToInitiator(String opinionId, String receiveUserId, String receiveUserName, String content, Boolean isCeo){
        try {
            String msg = "";
            String arguments = "";
            if (isCeo){
                // 消息内容
                msg =  String.format(opinionReplyConfigStatic.getContent(),content);
            }else {
                msg = String.format(opinionReplyConfigStatic.getContent1(), receiveUserName);
            }

            String query = String.format(opinionReplyConfigStatic.getQuery(), opinionId);
            Object parse = JSONObject.parse(query);
            Object parse1 = JSONObject.parse(arguments);

            List<SendHighMessageButton> buttons = new ArrayList<>();
            SendHighMessageButton sendHighMessageButton = new SendHighMessageButton();
            sendHighMessageButton.setName("查看详情");
            sendHighMessageButton.setUrl(opinionReplyConfigStatic.getUrl());
            sendHighMessageButton.setQuery(parse);
            sendHighMessageButton.setArguments(parse1);
            sendHighMessageButton.setMessageSource("extend");

            SendHighMessageButton sendHighMessageButton2 = new SendHighMessageButton();
            sendHighMessageButton2.setName("继续反馈");
            sendHighMessageButton2.setUrl(opinionReplyConfigStatic.getUrl2());
            sendHighMessageButton2.setQuery(parse);
            sendHighMessageButton2.setArguments(parse1);
            sendHighMessageButton2.setMessageSource("extend");
            buttons.add(sendHighMessageButton);
            buttons.add(sendHighMessageButton2);

            Map<String,Object> dataMap = new HashMap<>();
            if (isCeo){
                dataMap.put("title", opinionReplyConfigStatic.getTitle());
            }else {
                dataMap.put("title", opinionReplyConfigStatic.getTitle1());
            }
            dataMap.put("content",msg);
            dataMap.put("buttons",buttons);

            SendRobotMessageDto data = new SendRobotMessageDto();
            data.setContent(msg);
            data.setSender(IM.ROBOT_XIAOZANG);
            data.setTarget(receiveUserId);
            data.setConversationType(0);
            data.setPersistFlag(3);
            data.setContentExtra(JSONObject.toJSONString(dataMap));
            data.setType(1014);
            DataResult<Long> imResult = sendMsgFeignServiceStatic.send(data);
            log.info("意见回复查看提醒推送IM结果:{}-----------{}", JSONObject.toJSONString(data),JSONObject.toJSONString(imResult));

            return imResult.getData();
        } catch (Exception e) {
            log.error("意见回复查看提醒IM出错:{}", e, e);
        }
        return null;
    }



    /**反馈意见通知状态变更*/
    public static void sendOpinionStateChange(UserOpinion userOpinion, Integer opinionState, String seeUserId, String messageId) {

        if(StringUtils.isEmpty(userOpinion.getId())){
            return;
        }
        Map<String,Object> map=new HashMap<>();
        map.put("type", 1014);
        map.put("id",userOpinion.getId());
        map.put("msgIds",messageId);
        map.put("status",opinionState);
        SendRobotMessageDto data = new SendRobotMessageDto();
        data.setContentExtra(JSONObject.toJSONString(map));
        data.setPersistFlag(0);
        data.setType(MsgType.CHANGE_STATE);
        data.setSender(userOpinion.getUserId());
        data.setTarget(seeUserId);

        DataResult<Long> imResult = sendMsgFeignServiceStatic.send(data);
        log.info("推送IM结果:{}-----------{}", JSONObject.toJSONString(data),JSONObject.toJSONString(imResult));
    }

}
