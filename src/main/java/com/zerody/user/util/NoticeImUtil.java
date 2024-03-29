package com.zerody.user.util;

import com.alibaba.fastjson.JSONObject;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.constant.YesNo;
import com.zerody.expression.Expression;
import com.zerody.im.api.dto.SendHighMessageButton;
import com.zerody.im.api.dto.SendRobotMessageDto;
import com.zerody.im.constant.MsgType;
import com.zerody.im.feign.SendMsgFeignService;
import com.zerody.im.util.IM;
import com.zerody.user.config.*;
import com.zerody.user.constant.OpinionStateType;
import com.zerody.user.domain.UserOpinion;
import com.zerody.user.domain.UserOpinionType;
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
    private OpinionAdditionalConfig opinionAdditionalConfig;
    @Autowired
    private OpinionCompleteConfig opinionCompleteConfig;

    @Autowired
    private SendMsgFeignService sendMsgFeignService;

    private static OpinionReceiveConfig opinionReceiveConfigStatic;
    private static OpinionReplyConfig opinionReplyConfigStatic;
    private static OpinionAssistantConfig opinionAssistantConfigStatic;
    private static OpinionAdditionalConfig opinionAdditionalConfigStatic;
    private static OpinionCompleteConfig opinionCompleteConfigStatic;

    private static SendMsgFeignService sendMsgFeignServiceStatic;

    @PostConstruct
    public void init(){
        sendMsgFeignServiceStatic = sendMsgFeignService;
        opinionReceiveConfigStatic = opinionReceiveConfig;
        opinionReplyConfigStatic = opinionReplyConfig;
        opinionAssistantConfigStatic = opinionAssistantConfig;
        opinionAdditionalConfigStatic = opinionAdditionalConfig;
        opinionCompleteConfigStatic = opinionCompleteConfig;
    }

    public final  static  String PENDING = "https://lingdongkeji.oss-cn-guangzhou.aliyuncs.com/scrm/955c0a6f442f9b5ca47522650fc51123/68a7400b480e11bec45a118ed21bb33b.png";
    public final  static  String UNDERWAY = "https://lingdongkeji.oss-cn-guangzhou.aliyuncs.com/scrm/955c0a6f442f9b5ca47522650fc51123/2ce23e91102e6134ec889336cce014e4.png";
    public final  static  String ACCOMPLISH = "https://lingdongkeji.oss-cn-guangzhou.aliyuncs.com/scrm/955c0a6f442f9b5ca47522650fc51123/e86818f423c2db4593e954f178269d89.png";

    // fromPage： 0 提交人 1 收件人 2 协助人
    private final static Integer SUBMITTER = 0;
    private final static Integer DIRECT_RECEIVER = 1;
    private final static Integer ASSISTANT = 2;

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
                msg =  String.format(opinionReceiveConfigStatic.getContent(), opinionSenderInfo, limitContentLength(content));
            }else {
                msg = String.format(opinionReceiveConfigStatic.getContent1(),opinionSenderInfo);
            }
            String query = String.format(opinionReceiveConfigStatic.getQuery(), 1, opinionId);
            String query2 = String.format(opinionReceiveConfigStatic.getQuery2(), targetUserId,opinionId);
            Object parse = JSONObject.parse(query);
            Object parse2 = JSONObject.parse(query2);

            Object argumentParse = JSONObject.parse(arguments);

            List<SendHighMessageButton> buttons = new ArrayList<>();
            SendHighMessageButton sendHighMessageButton = new SendHighMessageButton();
            sendHighMessageButton.setName("查看详情");
            sendHighMessageButton.setUrl(opinionReceiveConfigStatic.getUrl());
            sendHighMessageButton.setQuery(parse);
            sendHighMessageButton.setArguments(argumentParse);
            sendHighMessageButton.setMessageSource("extend");

            SendHighMessageButton sendHighMessageButton2 = new SendHighMessageButton();
            sendHighMessageButton2.setName("分配他人回复");
            sendHighMessageButton2.setUrl(opinionReceiveConfigStatic.getUrl2());
            sendHighMessageButton2.setQuery(parse2);
            sendHighMessageButton2.setArguments(argumentParse);
            sendHighMessageButton2.setMessageSource("extend");
            buttons.add(sendHighMessageButton);
            buttons.add(sendHighMessageButton2);

            SendRobotMessageDto data = new SendRobotMessageDto();

            Map<String,Object> dataMap = new HashMap<>();
            if (isCeo){
                dataMap.put("title", opinionReceiveConfigStatic.getTitle());
                data.setContentPush("您好，您的董事长信箱收到一条新的反馈消息，请前往查看");
            }else {
                dataMap.put("title", opinionReceiveConfigStatic.getTitle1());
                data.setContentPush("您好，您的意见箱收到一条新的反馈消息，请前往查看");
            }
            dataMap.put("content",msg);
            dataMap.put("statusIcon",opinionReceiveConfigStatic.getStatusIcon());
            dataMap.put("buttons",buttons);


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
    * @Param:               [receiveUserId 信息接收人id , opinionSenderInfo 意见发起人信息 , content 消息内容,
     * appointerInfo 指派人信息 注意：该字段只有意见箱的协助信息才显示，由于意见箱查看人目前只支持单选，可展示此字段，多选时会出现同时指派情况
     * , isCeo 是否是投递给boss信箱 opinionState 意见处理状态]
    */
    public static Long pushOpinionToAssistant(String opinionId,String receiveUserId, String opinionSenderInfo, String content,String appointerInfo, Integer source , Integer opinionState){
        try {
            String msg = "";
            String arguments = "";
            if (source == 0){
                // 消息内容
                msg =  String.format(opinionAssistantConfigStatic.getContent(), opinionSenderInfo, limitContentLength(content));
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
            if (source == 0){
                dataMap.put("title", opinionAssistantConfigStatic.getTitle());
            }else {
                dataMap.put("title", opinionAssistantConfigStatic.getTitle1());
            }
            dataMap.put("content",msg);
            dataMap.put("buttons",buttons);

            if (opinionState == OpinionStateType.UNDERWAY.intValue()){
                dataMap.put("statusIcon",UNDERWAY);
            }else if (opinionState == OpinionStateType.PENDING.intValue()){
                dataMap.put("statusIcon",PENDING);
            }

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
    * @Param:               [receiveUserId 信息接收人id , receiveUserName 意见直接接收人的信息 , content 消息内容 ,   source 0 董事长信箱，1 意见箱 ]
    */
    public static Long pushReplyToInitiator(String opinionId, String receiveUserId, String receiveUserName, String content, Integer source){
        try {
            String msg = "";
            String arguments = "";
            String query3 = "";
            if (source == 0){
                // 消息内容
                msg =  String.format(opinionReplyConfigStatic.getContent(),limitContentLength(content));
                // 意见来源 需传递给前端 0 董事长信箱，1 意见箱
                query3 = String.format(opinionReplyConfigStatic.getQuery3(), YesNo.NO);
            }else {
                msg = String.format(opinionReplyConfigStatic.getContent1(), receiveUserName);
                query3 = String.format(opinionReplyConfigStatic.getQuery3(), YesNo.YES);
            }

            String query = String.format(opinionReplyConfigStatic.getQuery(), opinionId);
            String query2 = String.format(opinionReplyConfigStatic.getQuery2(), opinionId);

            Object parse = JSONObject.parse(query);
            Object parse2 = JSONObject.parse(query2);
            Object parse3 = JSONObject.parse(query3);
            Object argumentsParse = JSONObject.parse(arguments);

            List<SendHighMessageButton> buttons = new ArrayList<>();
            SendHighMessageButton sendHighMessageButton = new SendHighMessageButton();
            sendHighMessageButton.setName("查看详情");
            sendHighMessageButton.setUrl(opinionReplyConfigStatic.getUrl());
            sendHighMessageButton.setQuery(parse);
            sendHighMessageButton.setArguments(argumentsParse);
            sendHighMessageButton.setMessageSource("extend");

            SendHighMessageButton sendHighMessageButton2 = new SendHighMessageButton();
            sendHighMessageButton2.setName("补充回复");
            sendHighMessageButton2.setUrl(opinionReplyConfigStatic.getUrl2());
            sendHighMessageButton2.setQuery(parse2);
            sendHighMessageButton2.setArguments(argumentsParse);
            sendHighMessageButton2.setMessageSource("extend");
            buttons.add(sendHighMessageButton);
            buttons.add(sendHighMessageButton2);

            Map<String,Object> dataMap = new HashMap<>();
            if (source == 0){
                dataMap.put("title", opinionReplyConfigStatic.getTitle());
            }else {
                dataMap.put("title", opinionReplyConfigStatic.getTitle1());
            }
            dataMap.put("content",msg);
            dataMap.put("buttons",buttons);
            dataMap.put("statusIcon", opinionReplyConfigStatic.getStatusIcon());
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

    /**
    * @Description:         提交人补充回复，意见接收人和协助人收到此消息
    * @Param:               [opinionId, receiveUserId, opinionSenderInfo, content,  isDirect 是否是直接接收人]
    */
    public static Long pushAdditionalOpinionToHandler(UserOpinion userOpinion,String receiveUserId, String opinionSenderInfo, String content , Boolean isDirect){
        try {
            String msg = "";
            String arguments = "";
            String query = "";
            if (userOpinion.getSource() == 0){
                msg =  String.format(opinionAdditionalConfigStatic.getContent(), limitContentLength(content));
            }else {
                // 消息内容
                msg = String.format(opinionAdditionalConfigStatic.getContent1(), opinionSenderInfo);
            }

            // 判断这个通知是直接接收人 还是 协助人收到 ， 接收人依然可以对此通知分配， 协助人不行 意见详情路径 fromPage： 0 提交人 1 收件人 2 协助人
            if (isDirect){
                query = String.format(opinionAdditionalConfigStatic.getQuery(), DIRECT_RECEIVER ,userOpinion.getId());
            }else {
                query = String.format(opinionAdditionalConfigStatic.getQuery(), ASSISTANT ,userOpinion.getId());
            }

            Object parse = JSONObject.parse(query);
            Object argumentsParse = JSONObject.parse(arguments);

            List<SendHighMessageButton> buttons = new ArrayList<>();
            SendHighMessageButton sendHighMessageButton = new SendHighMessageButton();
            sendHighMessageButton.setName("查看详情");
            sendHighMessageButton.setUrl(opinionAdditionalConfigStatic.getUrl());
            sendHighMessageButton.setQuery(parse);
            sendHighMessageButton.setArguments(argumentsParse);
            sendHighMessageButton.setMessageSource("extend");
            buttons.add(sendHighMessageButton);

            Map<String,Object> dataMap = new HashMap<>();
            if (userOpinion.getSource() == 0){
                dataMap.put("title", opinionAdditionalConfigStatic.getTitle());
            }else {
                dataMap.put("title", opinionAdditionalConfigStatic.getTitle1());
            }
            dataMap.put("content",msg);
            dataMap.put("buttons",buttons);
            if (userOpinion.getState() == OpinionStateType.PENDING.intValue()){
                dataMap.put("statusIcon",PENDING);
            }else if (userOpinion.getState() == OpinionStateType.UNDERWAY.intValue()){
                dataMap.put("statusIcon",UNDERWAY);
            }

            SendRobotMessageDto data = new SendRobotMessageDto();
            data.setContent(msg);
            data.setSender(IM.ROBOT_XIAOZANG);
            data.setTarget(receiveUserId);
            data.setConversationType(0);
            data.setPersistFlag(3);
            data.setContentExtra(JSONObject.toJSONString(dataMap));
            data.setType(1014);
            DataResult<Long> imResult = sendMsgFeignServiceStatic.send(data);
            log.info("补充回复查看提醒查看提醒推送IM结果:{}-----------{}", JSONObject.toJSONString(data),JSONObject.toJSONString(imResult));
            return imResult.getData();
        } catch (Exception e) {
            log.error("补充回复查看提醒查看提醒IM出错:{}", e, e);
        }
            return null;
    }


    /**
    * @Description:         意见已被处理完毕，除点击处理完毕操作人之外其他关联该意见的人都收到该信息
    * @Param:               [userOpinion, receiveUserId, opinionSenderInfo, content, isDirect]
    * @Author:              xufan
    * @Date:                2024/3/15 18:35
    */
    public static void pushOpinionCompleteToOther(UserOpinion userOpinion,String receiveUserId, String opinionSenderInfo, String content , Boolean isDirect){
        try {
            String msg = "";
            String arguments = "";
            String query = "";
            if (userOpinion.getSource() == 0){
                msg =  String.format(opinionCompleteConfigStatic.getContent(), limitContentLength(content));
            }else {
                // 消息内容
                msg = String.format(opinionCompleteConfigStatic.getContent1(), opinionSenderInfo);
            }

            // 判断这个通知是直接接收人 还是 协助人收到 ，只有查看人可看到 协助人字段， 意见详情路径 fromPage： 0 提交人 1 收件人 2 协助人
            if (isDirect){
                query = String.format(opinionCompleteConfigStatic.getQuery(), DIRECT_RECEIVER ,userOpinion.getId());
            }else {
                query = String.format(opinionCompleteConfigStatic.getQuery(), ASSISTANT ,userOpinion.getId());
            }

            Object parse = JSONObject.parse(query);
            Object argumentsParse = JSONObject.parse(arguments);

            List<SendHighMessageButton> buttons = new ArrayList<>();
            SendHighMessageButton sendHighMessageButton = new SendHighMessageButton();
            sendHighMessageButton.setName("查看详情");
            sendHighMessageButton.setUrl(opinionCompleteConfigStatic.getUrl());
            sendHighMessageButton.setQuery(parse);
            sendHighMessageButton.setArguments(argumentsParse);
            sendHighMessageButton.setMessageSource("extend");
            buttons.add(sendHighMessageButton);

            Map<String,Object> dataMap = new HashMap<>();
            if (userOpinion.getSource() == 0){
                dataMap.put("title", opinionCompleteConfigStatic.getTitle());
            }else {
                dataMap.put("title", opinionCompleteConfigStatic.getTitle1());
            }
            dataMap.put("content",msg);
            dataMap.put("buttons",buttons);
            dataMap.put("statusIcon",ACCOMPLISH);


            SendRobotMessageDto data = new SendRobotMessageDto();
            data.setContent(msg);
            data.setSender(IM.ROBOT_XIAOZANG);
            data.setTarget(receiveUserId);
            data.setConversationType(0);
            data.setPersistFlag(3);
            data.setContentExtra(JSONObject.toJSONString(dataMap));
            data.setType(1014);
            DataResult<Long> imResult = sendMsgFeignServiceStatic.send(data);
            log.info("补充回复查看提醒查看提醒推送IM结果:{}-----------{}", JSONObject.toJSONString(data),JSONObject.toJSONString(imResult));
        } catch (Exception e) {
            log.error("补充回复查看提醒查看提醒IM出错:{}", e, e);
        }
    }



    /**
    * @Description:         反馈意见通知状态变更  (此消息通知可传递 list 形式的 messageId 集合，后续可优化)
    * @Param:               userOpinion 用户意见 , opinionState 意见状态 , targetUserId  接收人userId , senderUserId 引起意见变更的userId, messageId 意见id  source 来源
     *  isDirect 是否是意见直接查看人 ，只有意见查看人能看到协助人字段
     *
    */
    /**反馈意见通知状态变更*/
    public static void sendOpinionStateChange(UserOpinion userOpinion, Integer opinionState, String targetUserId, String senderUserId, String messageId , Integer source , Boolean isDirect) {

        if(StringUtils.isEmpty(userOpinion.getId())){
            return;
        }

        SendRobotMessageDto data = new SendRobotMessageDto();

        Map<String,Object> map=new HashMap<>();
        map.put("type", 1014);
        map.put("id",userOpinion.getId());
        map.put("msgIds",messageId);
        map.put("status",opinionState);
        if (opinionState.intValue() == OpinionStateType.ACCOMPLISH){

            List<SendHighMessageButton> buttons = new ArrayList<>();

            // 如果 目标user 和 意见发起人 相同, 可以继续反馈
            if (userOpinion.getUserId().equals(targetUserId)){

                String query = String.format(opinionReplyConfigStatic.getQuery(), userOpinion.getId());
                Object parse = JSONObject.parse(query);
                SendHighMessageButton sendHighMessageButton = new SendHighMessageButton();
                sendHighMessageButton.setName("查看详情");
                sendHighMessageButton.setUrl(opinionReplyConfigStatic.getUrl());
                sendHighMessageButton.setQuery(parse);
                sendHighMessageButton.setArguments(null);
                sendHighMessageButton.setMessageSource("extend");

                String query3 = String.format(opinionReplyConfigStatic.getQuery3(), source);
                Object parse3 = JSONObject.parse(query3);
                SendHighMessageButton sendHighMessageButton2 = new SendHighMessageButton();
                sendHighMessageButton2.setName("继续反馈");
                sendHighMessageButton2.setUrl(opinionReplyConfigStatic.getUrl3());
                sendHighMessageButton2.setQuery(parse3);
                sendHighMessageButton2.setArguments(null);
                sendHighMessageButton2.setMessageSource("extend");
                buttons.add(sendHighMessageButton);
                buttons.add(sendHighMessageButton2);

            }else {
                String query = "";
                // 判断这个详情是谁查看， 只有查看人可看到 协助人  fromPage： 0 提交人 1 收件人 2 协助人
                if (isDirect){
                    query = String.format(opinionReceiveConfigStatic.getQuery(), DIRECT_RECEIVER ,userOpinion.getId());
                }else {
                    query = String.format(opinionReceiveConfigStatic.getQuery(), ASSISTANT ,userOpinion.getId());
                }

                Object parse = JSONObject.parse(query);
                SendHighMessageButton sendHighMessageButton = new SendHighMessageButton();
                sendHighMessageButton.setName("查看详情");
                sendHighMessageButton.setUrl(opinionReceiveConfigStatic.getUrl());
                sendHighMessageButton.setQuery(parse);
                sendHighMessageButton.setArguments(null);
                sendHighMessageButton.setMessageSource("extend");
                buttons.add(sendHighMessageButton);
            }


            JSONObject extraJson = new JSONObject();
            extraJson.put("buttons",buttons);
            extraJson.put("statusIcon",ACCOMPLISH);

            map.put("extra",extraJson);
        }else if (opinionState.intValue() == OpinionStateType.UNDERWAY){

            JSONObject extraJson = new JSONObject();
            extraJson.put("statusIcon",UNDERWAY);
            map.put("extra",extraJson);
        }
        data.setContentExtra(JSONObject.toJSONString(map));
        data.setPersistFlag(0);
        data.setType(1104);
        data.setSender(IM.ROBOT_XIAOZANG);
        data.setTarget(targetUserId);

        DataResult<Long> imResult = sendMsgFeignServiceStatic.send(data);
        log.info("推送IM结果:{}-----------{}", JSONObject.toJSONString(data),JSONObject.toJSONString(imResult));


        if (imResult.isSuccess()){
            //推送状态拉取
            sendUserNotice(targetUserId);
        }
    }

    /**推送用户通知拉取消息*/
    public static void sendUserNotice(String target) {
        SendRobotMessageDto data = new SendRobotMessageDto();
        // 推送用户消息拉取通知
        data.setSender(IM.ROBOT_XIAOZANG);
        data.setTarget(target);
        data.setContentExtra(null);
        data.setConversationType(0);
        data.setPersistFlag(0);
        data.setType(MsgType.USER_NOTIC_PUSH);
        DataResult<Long> imResult = sendMsgFeignServiceStatic.send(data);
        log.info("推送用户消息拉取通知IM结果:{}-----------{}", JSONObject.toJSONString(data),JSONObject.toJSONString(imResult));
    }


    private static String limitContentLength(String content){
        return content.length() > 42 ? content.substring(0,42) + "...." : content;
    }
}
