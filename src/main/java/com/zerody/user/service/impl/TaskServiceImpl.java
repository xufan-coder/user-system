package com.zerody.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.customer.api.dto.CustomerStatisUnContactMsgDto;
import com.zerody.customer.api.dto.UnContactMsg;
import com.zerody.user.domain.Msg;
import com.zerody.user.feign.CustomerStatisFeignService;
import com.zerody.user.service.MsgService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.service.TaskService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author  DaBai
 * @date  2021/1/11 17:43
 */
@Slf4j
@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private CustomerStatisFeignService customerStatisFeignService;
    @Autowired
    private SysUserInfoService sysUserInfoService;
    @Autowired
    private MsgService msgService;

    @Override
	public void buildVisitNoticeInfo(Map<String, String> user) {
        //获取所有用户ID
        List<Msg> dtos= new ArrayList<>();
        //统计客户跟进提醒三种类型的，客户数
            DataResult<CustomerStatisUnContactMsgDto> result = customerStatisFeignService.uncontactInner(user.get("id"), user.get("companyId"));
            if(!result.isSuccess()){
			log.error("获取用户消息出错,userId={},{}", user.get("id"), result.getMessage());
			return;
            }
            CustomerStatisUnContactMsgDto dto = result.getData();
            if (DataUtil.isEmpty(dto)) {
			return;
            }
            List<UnContactMsg> contactMsgs = dto.getMsgs();
            if (CollectionUtils.isEmpty(contactMsgs)) {
			return;
            }
            contactMsgs.stream().forEach(c -> {
                dtos.add(buildNotice(user.get("id"), c.getMsg(), String.valueOf(c.getNum()).concat("天未联系提醒")));
            });

        log.info("客户未跟进消息:"+JSONObject.toJSONString(dtos));
		this.msgService.deleteExpiredMessage(user.get("id"));
        msgService.saveBatch(dtos);
    }

    public Msg buildNotice(String userId, String msg, String title){
        Msg dto=new Msg();
        dto.setId(UUIDutils.getUUID32());
        dto.setCreateTime(new Date());
        dto.setMessageContent(msg);
        dto.setUserId(userId);
        dto.setMessageTile(title);
        return dto;
    }
}
