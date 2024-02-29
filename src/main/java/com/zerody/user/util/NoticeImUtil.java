package com.zerody.user.util;

import com.alibaba.fastjson.JSONObject;
import com.zerody.common.api.bean.DataResult;
import com.zerody.im.api.dto.SendHighMessageButton;
import com.zerody.im.api.dto.SendRobotMessageDto;
import com.zerody.im.feign.SendMsgFeignService;
import com.zerody.im.util.IM;
import com.zerody.user.config.BossReceiveOpinionConfig;
import lombok.extern.slf4j.Slf4j;
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
    private BossReceiveOpinionConfig bossReceiveOpinionConfig;

    @Autowired
    private SendMsgFeignService sendMsgFeignService;

    private static BossReceiveOpinionConfig bossReceiveOpinionConfigStatic;
    private static SendMsgFeignService sendMsgFeignServiceStatic;

    @PostConstruct
    public void init(){
        sendMsgFeignServiceStatic = sendMsgFeignService;
        bossReceiveOpinionConfigStatic = bossReceiveOpinionConfig;
    }

    public static void pushOpinionToBoss(String userId, String sender, String content){
        try {
            // 消息内容
            String msg =  String.format(bossReceiveOpinionConfigStatic.getContent(), sender, content);
            String query = String.format(bossReceiveOpinionConfigStatic.getQuery(), userId);
            Object parse = JSONObject.parse(query);

            String arguments = String.format(bossReceiveOpinionConfigStatic.getArguments(),userId);
            Object parse1 = JSONObject.parse(arguments);

            List<SendHighMessageButton> buttons = new ArrayList<>();
            SendHighMessageButton sendHighMessageButton = new SendHighMessageButton();
            sendHighMessageButton.setName("查看详情");
            sendHighMessageButton.setUrl(bossReceiveOpinionConfigStatic.getUrl());
            sendHighMessageButton.setQuery(parse);
            sendHighMessageButton.setArguments(parse1);
            sendHighMessageButton.setMessageSource("extend");

            SendHighMessageButton sendHighMessageButton2 = new SendHighMessageButton();
            sendHighMessageButton2.setName("继续反馈");
            sendHighMessageButton2.setUrl(bossReceiveOpinionConfigStatic.getUrl2());
            sendHighMessageButton2.setQuery(parse);
            sendHighMessageButton2.setArguments(parse1);
            sendHighMessageButton2.setMessageSource("extend");
            buttons.add(sendHighMessageButton);
            buttons.add(sendHighMessageButton2);

            Map<String,Object> dataMap = new HashMap<>();
            dataMap.put("title", bossReceiveOpinionConfigStatic.getTitle());
            dataMap.put("content",msg);
            dataMap.put("buttons",buttons);

            SendRobotMessageDto data = new SendRobotMessageDto();
            data.setContent(msg);
            data.setSender(IM.ROBOT_XIAOZANG);
            data.setTarget(userId);
            data.setConversationType(0);
            data.setPersistFlag(3);
            data.setContentExtra(JSONObject.toJSONString(dataMap));
            data.setType(1014);
            DataResult<Long> imResult = sendMsgFeignServiceStatic.send(data);
            log.info("boss新信件接收查看提醒推送IM结果:{}-----------{}", JSONObject.toJSONString(data),JSONObject.toJSONString(imResult));
        } catch (Exception e) {
            log.error("推送给boss新信件接收查看提醒IM出错:{}", e, e);
        }
    }



}
